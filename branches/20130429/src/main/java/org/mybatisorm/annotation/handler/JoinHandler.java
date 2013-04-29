/*
 *    Copyright 2012 The MyBatisORM Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatisorm.annotation.handler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.Fields;
import org.mybatisorm.annotation.Join;
import org.mybatisorm.exception.AnnotationNotFoundException;
import org.mybatisorm.exception.InvalidAnnotationException;
import org.mybatisorm.exception.InvalidJoinException;
import org.mybatisorm.util.StringUtil;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @author k
 *
 */
public class JoinHandler extends TableHandler {

	public JoinHandler(Class<?> clazz) {
		super(clazz);
	}

	private List<PropertyField> propertyFields;
	
	public synchronized String getName() {
		if (name != null)
			return name;
		
		Join join = targetClass.getAnnotation(Join.class);
		String joinHint = join.value();
		List<Field> fields = Arrays.asList(targetClass.getDeclaredFields());
		if (fields.size() < 2)
			throw new InvalidAnnotationException("The class " + targetClass.getName() +
					" doesn't have enough properties to make the join statement.");
		name = ("".equals(joinHint)) ? getImplicitJoin(fields) : getExplicitJoin(joinHint,fields);
		return name;
	}
	
	private Field getField(String fieldName) {
		return getField(targetClass,fieldName);
	}
	
	private Field getField(Class<?> clazz, String fieldName) {
		Field field;
		try {
			field = clazz.getDeclaredField(fieldName);
		} catch (Exception e) {
			throw new InvalidJoinException("The class "+clazz.getSimpleName()+" has no '"+fieldName+"' property.");
		}
		return field;
	}
	
	private String getExplicitJoin(String joinHint, List<Field> fields) {
		StringBuilder sb = new StringBuilder();

		TableHandler handler;
		List<Field> fieldList = new ArrayList<Field>();
		Class<?> clazz;
		Field field;

		Hashtable<Set<Class<?>>, LinkedList<Field[]>> refMap = null;
		if (!Pattern.compile(Pattern.quote(" ON "), Pattern.CASE_INSENSITIVE).matcher(joinHint).find()) {
			refMap = getRefMap(fields);
			if (refMap.isEmpty())
				throw new InvalidAnnotationException("The references for join has not been found");
		}
		
		QueryTokenizer tokenizer = new QueryTokenizer(joinHint);
		QueryTokenizer.TokenType type;
		String text;
		boolean onRequired = false;
		
		// processing the first class
		type = tokenizer.next();
		if (type != QueryTokenizer.TokenType.IDENTIFIER)
			throw new InvalidJoinException("The join hint must start with the property name.");
		text = tokenizer.getText();
		field = getField(text);
		fieldList.add(field);
		clazz = field.getType();
		handler = HandlerFactory.getHandler(clazz);
		sb.append(handler.getName()).append(" ").append(text).append("_");

		while ((type = tokenizer.next()) != QueryTokenizer.TokenType.EOS) {
			text = tokenizer.getText();
			switch (type) {
			case IDENTIFIER:
				field = getField(text);
				fieldList.add(field);
				clazz = field.getType();
				handler = HandlerFactory.getHandler(clazz);
				
				sb.append(handler.getName()).append(" ").append(text).append("_");
				onRequired = true; 
				break;
			case CLASS_FIELD:
				String[] s = text.split("\\.");
				sb.append(s[0]).append("_.");
				field = getField(getField(s[0]).getType(),s[1]);
				sb.append(ColumnAnnotation.getName(field));
				break;
			case ON:
				onRequired = false;
				sb.append(text);
				break;
			case WS:
				sb.append(text);
				break;
			case ETC:
				if (onRequired) {
					sb.append(getOnPhrase(fieldList, refMap));
					onRequired = false;
				}
				sb.append(text);
				break;
			}
		}
		if (onRequired) {
			sb.append(getOnPhrase(fieldList, refMap));
		}
		return sb.toString();
	}

	private String getOnPhrase(List<Field> fieldList, Hashtable<Set<Class<?>>, LinkedList<Field[]>> refMap) {
		StringBuilder sb = new StringBuilder();
		Field field = fieldList.get(fieldList.size()-1), field2;
		Class<?> clazz = field.getType(), clazz2;
		List<String> cnames = new LinkedList<String>();
		int j;
		for (j = fieldList.size() - 2; j >= 0; j--) {
			field2 = fieldList.get(j);
			clazz2 = field2.getType();
			cnames.add(0, clazz2.getSimpleName()); 
			LinkedList<Field[]>  refs = refMap.get(pair(clazz,clazz2));
			if (refs == null)
				continue;
			
			sb.append(" ON (");
			Iterator<Field[]> it = refs.iterator();
			while(it.hasNext()) {
				Field[] fs = it.next();
				if (field.getType().equals(fs[0].getDeclaringClass())) {
					sb.append(field.getName()).append("_.").append(ColumnAnnotation.getName(fs[0])).append(" = ")
						.append(field2.getName()).append("_.").append(ColumnAnnotation.getName(fs[1]));
				} else {
					sb.append(field2.getName()).append("_.").append(ColumnAnnotation.getName(fs[0])).append(" = ")
						.append(field.getName()).append("_.").append(ColumnAnnotation.getName(fs[1]));
				}						
				if (it.hasNext())
					sb.append(" AND ");
			}
			sb.append(") ");
			break;
		}
		if (j < 0)
			throw new InvalidJoinException("The class "+clazz.getSimpleName() +
					" doesn't refer to the former classes("+StringUtil.join(cnames, ",")+"), nor vice versa.");
		
		return sb.toString();
	}

	private String getImplicitJoin(List<Field> fields) {
		Hashtable<Set<Class<?>>, LinkedList<Field[]>> refMap = getRefMap(fields);
		if (refMap.isEmpty())
			throw new InvalidAnnotationException("References for join not found");
		
		Field field = fields.get(0);
		Class<?> clazz = field.getType(), clazz2;
		LinkedList<Field[]> refs = null;
		StringBuilder sb = new StringBuilder();
		TableHandler handler = HandlerFactory.getHandler(clazz);
		String table = handler.getName(), alias = field.getName()+"_";
		Map<String,String> aliases = new HashMap<String,String>();
		aliases.put(table,alias);
		sb.append(table).append(" ").append(alias);
		List<String> cnames = new LinkedList<String>();
		for (int i = 1; i < fields.size(); i++) {
			field = fields.get(i);
			clazz = field.getType();
			table = HandlerFactory.getHandler(clazz).getName();
			alias = field.getName()+"_";
			aliases.put(table,alias);
			int j;
			for (j = i - 1; j >= 0; j--) {
				clazz2 = fields.get(j).getType();
				cnames.add(0, clazz2.getSimpleName()); 
				refs = refMap.get(pair(clazz,clazz2));
				if (refs == null)
					continue;
				
				sb.append(" INNER JOIN ").append(table).append(" ").append(alias).append(" ON (");		
				Iterator<Field[]> it = refs.iterator();
				while(it.hasNext()) {
					Field[] fs = it.next();
					table = HandlerFactory.getHandler(fs[0].getDeclaringClass()).getName();
					alias = aliases.get(table);
					sb.append(alias).append(".").append(ColumnAnnotation.getName(fs[0])).append(" = ");
					
					table = HandlerFactory.getHandler(fs[1].getDeclaringClass()).getName();
					alias = aliases.get(table);
					sb.append(alias).append(".").append(ColumnAnnotation.getName(fs[1]));
					if (it.hasNext())
						sb.append(" AND ");
				}
				sb.append(")");
				break;
			}
			if (j < 0)
				throw new InvalidJoinException("The class "+clazz.getSimpleName() +
						" doesn't refer to the former classes. ("+StringUtil.join(cnames, ",")+")");
			cnames.clear();
		}
		
		return sb.toString();
	}

	private Hashtable<Set<Class<?>>, LinkedList<Field[]>> getRefMap(List<Field> fields) {
		Map<String,Class<?>> classMap = new HashMap<String,Class<?>>();
		List<Field> refFields = new ArrayList<Field>();
		TableHandler handler = null;
		for (Field field : fields) {
			Class<?> clazz = field.getType();
			if (!TableHandler.hasAnnotation(clazz))
				throw new AnnotationNotFoundException("The property class " + clazz.getName() +
						" has no @Table annotation.");
			classMap.put(clazz.getSimpleName(),clazz);
			
			handler = HandlerFactory.getHandler(clazz);
			refFields.addAll(handler.getReferenceFields());
		}

		Hashtable<Set<Class<?>>, LinkedList<Field[]>> refMap = new Hashtable<Set<Class<?>>, LinkedList<Field[]>>();
		for (Field field : refFields) {
			Class<?> clazz = field.getDeclaringClass();
			String[] ref = field.getAnnotation(Column.class).references().split("\\.");
			if (ref.length < 2)
				throw new InvalidAnnotationException(clazz.getSimpleName()+"."+field.getName()+" has invalid references property in @Column annotation.");
			
			Class<?> refClazz = classMap.get(ref[0]);
			if (refClazz == null)
				continue;
			
			Set<Class<?>> pair = pair(clazz,refClazz);
			LinkedList<Field[]> list = refMap.get(pair);
			if (list == null) {
				list = new  LinkedList<Field[]>();
				refMap.put(pair, list);
			}
			Field refField = null;
			try {
				refField = refClazz.getDeclaredField(ref[1]);
			} catch (Exception e) {
				throw new InvalidAnnotationException("The "+ref[0]+" class has no "+ref[1]+" property.");
			}
			list.add(new Field[] {refField,field});
		}
		return refMap;
	}
	
	private Set<Class<?>> pair(Class<?> c1, Class<?> c2) {
		Set<Class<?>> set = new HashSet<Class<?>>();
		set.add(c1);
		set.add(c2);
		return set;
	}

	private synchronized List<PropertyField> getPropertyFields() {
		if (propertyFields != null)
			return propertyFields;
		
		propertyFields = new ArrayList<PropertyField>();
		for (Field clazzField : targetClass.getDeclaredFields()) {
			Fields fields = clazzField.getAnnotation(Fields.class);
			if (fields == null)
				continue;
			String value = fields.value();
			if ("".equals(value))
				continue;
			List<Field> fieldList = HandlerFactory.getHandler(clazzField.getType()).getColumnFields();
			if ("*".equals(value)) {
				propertyFields.add(new PropertyField(clazzField,fieldList));
			} else {
				List<Field> columnFields = new ArrayList<Field>();
				Set<String> fieldSet = new HashSet<String>(Arrays.asList(value.replaceAll("\\s", "").split(",")));
				for (Field field : fieldList) {
					if (fieldSet.contains(field.getName()))
						columnFields.add(field);
				}
				propertyFields.add(new PropertyField(clazzField,columnFields));
			}
		}

		return propertyFields;
	}

	public String getColumnAsFieldComma() {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for (PropertyField property : getPropertyFields()) {
			for (Field field : property.fieldList) {
				if (sb.length() > 0)
					sb.append(", ");
				sb.append(property.name).append("_.").append(ColumnAnnotation.getName(field))
					.append(" ").append(getAlias(property.name,field.getName(),index++));
					//property.name).append("_").append(field.getName());
			}
		}
		return sb.toString();
	}

	public String getPrimaryKeyComma() {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for (PropertyField property : getPropertyFields()) {
			for (Field field : HandlerFactory.getHandler(property.type).getPrimaryKeyFields()) {
				if (sb.length() > 0)
					sb.append(", ");
				sb.append(property.name).append("_.").append(ColumnAnnotation.getName(field))
				.append(" ").append(getAlias(property.name,field.getName(),index++));
			}
		}
		return sb.toString();
	}
	
	public String getNotNullPrimaryKeyEqualFieldAnd(Object object, String fieldPrefix) {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		Object value;
		BeanWrapper bean = new BeanWrapperImpl(object), pbean;
		for (PropertyField property : getPropertyFields()) {
			value = bean.getPropertyValue(property.name);
			if (value == null)
				continue;
			
			pbean =  new BeanWrapperImpl(value);
			for (Field field : HandlerFactory.getHandler(value).getPrimaryKeyFields()) {
				if (pbean.getPropertyValue(field.getName()) != null) {
					if (sb.length() > 0)
						sb.append(" AND ");
					sb.append(getAlias(property.name,field.getName(),index)).append(" = ")
						.append(" #{").append(fieldPrefix).append(property.name).append(".").append(field.getName()).append("}");
				}
				index++;
			}
		}
		return sb.toString();
	}

	public String getNotNullColumnEqualFieldAnd(Object object) {
		return getNotNullColumnEqualField(object, "", " AND ");
	}

	public String getNotNullColumnEqualFieldAnd(Object object, String prefix) {
		return getNotNullColumnEqualField(object, prefix, " AND ");
	}

	private String getNotNullColumnEqualField(Object object, String prefix, String delimiter) {
		StringBuilder sb = new StringBuilder();
		BeanWrapper bean = new BeanWrapperImpl(object);
		for (Field clazzField : targetClass.getDeclaredFields()) {
			Object property = bean.getPropertyValue(clazzField.getName());
			if (property != null) {
				TableHandler handler = HandlerFactory.getHandler(clazzField.getType()); 
				String phrase = handler.getNotNullColumnEqualField(property,
						clazzField.getName()+"_.",
						prefix+clazzField.getName()+".",
						delimiter);
				if (phrase.length() > 0) {
					if (sb.length() > 0)
						sb.append(delimiter);
					sb.append(phrase);
				}
			}
		}
		return sb.toString();
	}
	
	public List<ResultMapping> getResultMappingList(Configuration configuration) {
		List<ResultMapping> list = new ArrayList<ResultMapping>();
		int index = 0;
		for (PropertyField property : getPropertyFields()) {
			for (Field field : property.fieldList) {
				list.add((new ResultMapping.Builder(configuration,
						property.name+"."+field.getName(),
						getAlias(property.name,field.getName(),index++),
						field.getType())).build());
			}
		}
		return list;
	}
	
	/*
	 * Oracle has a limit of 30 characters for names.
	 */
	private String getAlias(String property, String field, int index) {
		String alias = property+"_"+field;
		if (alias.length() <= 30)
			return alias;
		String s = StringUtil.convertTo26Digit(index);
		return alias.substring(0, 29-s.length()) + "_" + s;
	}

	static class PropertyField {
		String name;
		Class<?> type;
		List<Field> fieldList;
		public PropertyField(Field clazzField, List<Field> list) {
			this.name = clazzField.getName();
			this.type = clazzField.getType();
			this.fieldList = list;
		}
	}

	public String findColumnName(String fieldName) {
		String[] fieldNames = fieldName.split("\\.");
		if (fieldNames.length != 2)
			return null;
		Field field; 
		try {
			field = targetClass.getDeclaredField(fieldNames[0]);
		} catch(Exception e) {
			return null;
		}
		String columnName = HandlerFactory.getHandler(field.getType()).findColumnName(fieldNames[1]);
		
		return columnName != null ? field.getName() + "_." + columnName : null;
	}
	
	public String buildOrderBy(String orderBy) {
		StringBuilder sb = new StringBuilder();
		QueryTokenizer tokenizer = new QueryTokenizer(orderBy);
		QueryTokenizer.TokenType type;
		String text;
		
		while ((type = tokenizer.next()) != QueryTokenizer.TokenType.EOS) {
			text = tokenizer.getText();
			switch (type) {
			case CLASS_FIELD:
				String name = findColumnName(text);
				if (name == null)
					sb.append(text);
				else
					sb.append(name);
				break;
			default:
				sb.append(text);
				break;
			}
		}
		return sb.toString();
	}

}
