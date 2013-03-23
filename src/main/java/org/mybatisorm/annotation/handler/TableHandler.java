/* Copyright 2012 The MyBatisORM Team
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
package org.mybatisorm.annotation.handler;

import java.lang.reflect.Field;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.Table;
import org.mybatisorm.annotation.TypeHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class TableHandler {

	private static final Log logger = LogFactory.getLog(TableHandler.class);

	protected Class<?> targetClass;

	private List<Field> columnFields;

	private List<Field> primaryKeyFields;

	protected String name;

	public TableHandler(Class<?> clazz) {
		this.targetClass = clazz;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public static boolean hasAnnotation(Class<?> clazz) {
		return clazz.getAnnotation(Table.class) != null;
	}

	public synchronized String getName() {
		if (name != null) return name;

		Table t = targetClass.getAnnotation(Table.class);
		name = t.value();
		if ("".equals(name)) name = targetClass.getSimpleName();
		return name;
	}

	public FieldList getNonAutoIncrementFieldList() {
		FieldList fieldList = new FieldList();
		for (Field field : getColumnFields()) {
			Column column = field.getAnnotation(Column.class);
			if (!column.autoIncrement()) fieldList.add(field.getName(), ColumnAnnotation.getName(field, column));
		}
		return fieldList;
	}

	protected synchronized List<Field> getColumnFields() {
		if (columnFields != null) return columnFields;
		columnFields = getFields(targetClass);
		for (int i = columnFields.size() - 1; i >= 0; i--) {
			if (!columnFields.get(i).isAnnotationPresent(Column.class)) columnFields.remove(i);
		}
		return columnFields;
	}

	protected synchronized List<Field> getPrimaryKeyFields() {
		if (primaryKeyFields != null) return primaryKeyFields;
		primaryKeyFields = new LinkedList<Field>();
		for (Field field : getColumnFields()) {
			if (field.getAnnotation(Column.class).primaryKey()) primaryKeyFields.add(field);
		}
		return primaryKeyFields;
	}

	public static List<Field> getFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<Field>();
		Class<?> superClass = clazz.getSuperclass();
		if (TableHandler.hasAnnotation(superClass)) {
			fields.addAll(getFields(clazz.getSuperclass()));
		}
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		return fields;
	}

	public String getNonPrimaryKeyComma() {
		StringBuilder sb = new StringBuilder();
		for (Field field : getColumnFields()) {
			Column column = field.getAnnotation(Column.class);
			if (!column.primaryKey()) {
				if (sb.length() > 0) sb.append(",");
				sb.append(ColumnAnnotation.getName(field, column));
			}
		}
		return sb.toString();
	}

	public String getPrimaryKeyComma() {
		StringBuilder sb = new StringBuilder();
		for (Field field : getPrimaryKeyFields()) {
			if (sb.length() > 0) sb.append(",");
			sb.append(ColumnAnnotation.getName(field));
		}
		return sb.toString();
	}

	public String getNonPrimaryKeyEqualFieldComma() {
		StringBuilder sb = new StringBuilder();
		for (Field field : getColumnFields()) {
			Column column = field.getAnnotation(Column.class);
			if (!column.primaryKey()) {
				if (sb.length() > 0) sb.append(",");
				// sb.append(ColumnAnnotation.getName(field, column)).append(" = #{").append(field.getName()).append("}");
				sb.append(TokenMaker.fieldEqual(field));
			}
		}
		return sb.toString();
	}

	public String getPrimaryKeyEqualFieldAnd() {
		StringBuilder sb = new StringBuilder();
		for (Field field : getPrimaryKeyFields()) {
			if (sb.length() > 0) sb.append(" AND ");
			if (field.isAnnotationPresent(TypeHandler.class)) {

			} else {
				// sb.append(ColumnAnnotation.getName(field)).append(" = #{").append(field.getName()).append("}");
				sb.append(TokenMaker.fieldEqual(field, null));
			}
		}
		return sb.toString();
	}

	public String getNotNullColumnEqualFieldComma(Object object) {
		return getNotNullColumnEqualField(object, "", "", ", ");
	}

	public String getNotNullColumnEqualFieldAnd(Object object) {
		return getNotNullColumnEqualField(object, "", "", " AND ");
	}

	public String getNotNullColumnEqualFieldAnd(Object object, String prefix) {
		return getNotNullColumnEqualField(object, "", prefix, " AND ");
	}

	protected String getNotNullColumnEqualField(Object object, String columnPrefix, String fieldPrefix, String delimiter) {
		StringBuilder sb = new StringBuilder();
		BeanWrapper bean = new BeanWrapperImpl(object);
		for (Field field : getColumnFields()) {
			Object value = bean.getPropertyValue(field.getName());
			if (value != null) {
				Column column = field.getAnnotation(Column.class);
				if (sb.length() > 0) sb.append(delimiter);
				// sb.append(columnPrefix).append(ColumnAnnotation.getName(field, column)).append(" = ").append(" #{").append(fieldPrefix).append(field.getName()).append("}");
				sb.append(TokenMaker.fieldEqual(field, column, fieldPrefix, columnPrefix));
			}
		}
		return sb.toString();
	}

	public String getNotNullNonPrimaryKeyEqualFieldComma(Object object) {
		StringBuilder sb = new StringBuilder();
		BeanWrapper bean = new BeanWrapperImpl(object);
		for (Field field : getColumnFields()) {
			Column column = field.getAnnotation(Column.class);
			if (!column.primaryKey()) {
				Object value = bean.getPropertyValue(field.getName());
				if (value != null) {
					if (sb.length() > 0) sb.append(", ");
					// sb.append(ColumnAnnotation.getName(field, column)).append(" = ").append(" #{").append(field.getName()).append("}");
					sb.append(TokenMaker.fieldEqual(field, column));
				}
			}
		}
		return sb.toString();
	}

	public String getNotNullNonPrimaryKeyEqualPlusFieldComma(Object object) {
		String columnName = null;
		StringBuilder sb = new StringBuilder();
		BeanWrapper bean = new BeanWrapperImpl(object);
		for (Field field : getColumnFields()) {
			Column column = field.getAnnotation(Column.class);
			if (!column.primaryKey()) {
				Object value = bean.getPropertyValue(field.getName());
				if (value != null) {
					if (sb.length() > 0) sb.append(", ");
					columnName = ColumnAnnotation.getName(field, column);
					// sb.append(columnName).append(" = ").append(columnName).append(" + #{").append(field.getName()).append("}");
					sb.append(columnName).append(" = ").append(columnName).append(" + ").append(TokenMaker.mybatisToken(field));
				}
			}
		}
		return sb.toString();
	}

	public String getColumnComma() {
		StringBuilder sb = new StringBuilder();
		for (Field field : getColumnFields()) {
			Column column = field.getAnnotation(Column.class);
			if (sb.length() > 0) sb.append(",");
			sb.append(ColumnAnnotation.getName(field, column));
		}
		return sb.toString();
	}

	/**
	 * @param clazz
	 * @return
	 */
	public String getColumnAsFieldComma() {
		StringBuilder sb = new StringBuilder();
		for (Field field : getColumnFields()) {
			Column column = field.getAnnotation(Column.class);
			if (sb.length() > 0) sb.append(",");

			String columnName = ColumnAnnotation.getName(field, column);
			if (!"".equals(columnName)) {
				sb.append(columnName).append(" ");
			}
			sb.append(field.getName());
		}
		return sb.toString();
	}

	public FieldList getNotNullFieldList(Object object) {
		FieldList fieldList = new FieldList();
		BeanWrapper bean = new BeanWrapperImpl(object);
		for (Field field : getColumnFields()) {
			Object value = bean.getPropertyValue(field.getName());
			if (value != null) {
				Column column = field.getAnnotation(Column.class);
				if (field.isAnnotationPresent(TypeHandler.class)) {
					fieldList.add(field.getName(), ColumnAnnotation.getName(field, column), field.getAnnotation(TypeHandler.class));
				} else {
					fieldList.add(field.getName(), ColumnAnnotation.getName(field, column));
				}
			}
		}
		return fieldList;
	}

	public FieldList getNotNullNonAutoIncrementFieldList(Object object) {
		FieldList fieldList = new FieldList();
		BeanWrapper bean = new BeanWrapperImpl(object);
		for (Field field : getColumnFields()) {
			Column column = field.getAnnotation(Column.class);
			if (!column.autoIncrement()) {
				Object value = bean.getPropertyValue(field.getName());
				if (value != null) {
					fieldList.add(field.getName(), ColumnAnnotation.getName(field, column));
				}
			}
		}
		return fieldList;
	}

	public List<ResultMapping> getResultMappingList(Configuration configuration) {
		List<ResultMapping> list = new ArrayList<ResultMapping>();
		for (Field field : getColumnFields()) {
			String columnName = ColumnAnnotation.getName(field);

			if (!"".equals(columnName)) {
				ResultMapping.Builder builder = new ResultMapping.Builder(configuration, field.getName(), columnName, field.getType());
				// add typeHandler
				if (field.isAnnotationPresent(TypeHandler.class)) {
					TypeHandler typeHandlerAnnotation = field.getAnnotation(TypeHandler.class);
					// if (logger.isDebugEnabled()) logger.debug("add typeHandler [" + typeHandlerAnnotation.value().getName() + "] for " + columnName + ":" + typeHandlerAnnotation.jdbcType());
					builder.typeHandler(BeanUtils.instantiate(typeHandlerAnnotation.value())).jdbcType(typeHandlerAnnotation.jdbcType());

				}
				list.add(builder.build());
			}
		}
		return list;
	}

	public List<Field> getReferenceFields() {
		List<Field> fields = getColumnFields(), rfields = new ArrayList<Field>();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if (!"".equals(column.references())) {
				rfields.add(field);
			}
		}
		return rfields;
	}

	public List<Field> getReferenceFields(Class<?> clazz) {
		List<Field> fields = getColumnFields(), rfields = new ArrayList<Field>();
		for (Field field : fields) {
			String references = field.getAnnotation(Column.class).references();
			if (references.startsWith(clazz.getSimpleName() + ".")) {
				rfields.add(field);
			}
		}
		return rfields;
	}

	public String findColumnName(String fieldName) {
		String columnName = null;
		try {
			columnName = ColumnAnnotation.getName(targetClass.getDeclaredField(fieldName));
		} catch (Exception e) {}
		return columnName;
	}

	public String buildOrderBy(String orderBy) {
		QueryTokenizer tokenizer = new QueryTokenizer(orderBy);
		QueryTokenizer.TokenType type;
		String text;
		StringBuilder sb = new StringBuilder();

		while ((type = tokenizer.next()) != QueryTokenizer.TokenType.EOS) {
			text = tokenizer.getText();
			switch (type) {
				case IDENTIFIER:
					String name = findColumnName(text);
					if (name == null) sb.append(text);
					else sb.append(name);
					break;
				default:
					sb.append(text);
					break;
			}
		}

		return sb.toString();
	}

	static class QueryTokenizer {

		public enum TokenType {
			NONE, IDENTIFIER, CLASS_FIELD, ETC, ON, WS, EOS
		};

		private static Set<String> KEYWORDS = new HashSet<String>(Arrays.asList(new String[] { "JOIN", "INNER", "OUTER", "LEFT", "RIGHT", "FULL", "CROSS", "NATURAL", "DESC", "ASC" }));

		private StringBuilder sb;

		private String text;

		private CharacterIterator it;

		private char ch;

		private TokenType type;

		public QueryTokenizer(String joinHint) {
			it = new StringCharacterIterator(joinHint.trim());
			ch = it.first();
			sb = new StringBuilder();
			text = "";
			type = TokenType.NONE;
		}

		public TokenType next() {
			if (type == TokenType.EOS) return type;

			for (; ch != CharacterIterator.DONE; ch = it.next()) {
				if (Character.isJavaIdentifierPart(ch)) {
					if (type == TokenType.WS || type == TokenType.ETC) return transit(TokenType.IDENTIFIER);
					if (type != TokenType.CLASS_FIELD) type = TokenType.IDENTIFIER;
					sb.append(ch);
				} else if (ch == '.') {
					if (type == TokenType.IDENTIFIER) {
						type = TokenType.CLASS_FIELD;
						sb.append(ch);
					} else {
						return transit(TokenType.ETC);
					}
				} else if (Character.isWhitespace(ch)) {
					if (type != TokenType.WS) return transit(TokenType.WS);
					type = TokenType.WS;
					sb.append(ch);
				} else {
					if (type != TokenType.ETC) return transit(TokenType.ETC);
					type = TokenType.ETC;
					sb.append(ch);
				}
			}

			TokenType oldType = type;
			type = TokenType.EOS;
			text = sb.toString();
			return oldType;
		}

		private TokenType transit(TokenType newType) {
			text = sb.toString();
			sb.setLength(0);
			sb.append(ch);
			ch = it.next();

			if (type == TokenType.IDENTIFIER) {
				String upper = text.toUpperCase();
				if ("ON".equals(upper)) type = TokenType.ON;
				else if (KEYWORDS.contains(upper)) type = TokenType.ETC;
			}
			TokenType oldType = type;
			type = newType;
			return oldType;
		}

		public String getText() {
			return text;
		}
	}

	public String getNotNullPrimaryKeyEqualFieldAnd(Object object, String fieldPrefix) {
		StringBuilder sb = new StringBuilder();
		BeanWrapper bean = new BeanWrapperImpl(object);
		for (Field field : getPrimaryKeyFields()) {
			Object value = bean.getPropertyValue(field.getName());
			if (value != null) {
				if (sb.length() > 0) sb.append(" AND ");
				// sb.append(ColumnAnnotation.getName(field)).append(" = ").append(" #{").append(fieldPrefix).append(field.getName()).append("}");
				sb.append(TokenMaker.fieldEqual(field));
			}
		}
		return sb.toString();
	}

	public String getNotNullPrimaryKeyComma(Object object) {
		StringBuilder sb = new StringBuilder();
		BeanWrapper bean = new BeanWrapperImpl(object);
		for (Field field : getPrimaryKeyFields()) {
			Object value = bean.getPropertyValue(field.getName());
			if (value != null) {
				if (sb.length() > 0) sb.append(", ");
				sb.append(ColumnAnnotation.getName(field));
			}
		}
		return sb.toString();
	}

}
