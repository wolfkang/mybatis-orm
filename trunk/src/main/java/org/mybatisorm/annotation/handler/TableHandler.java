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
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.Join;
import org.mybatisorm.annotation.Table;
import org.mybatisorm.exception.AnnotationNotFoundException;
import org.mybatisorm.exception.InvalidAnnotationException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class TableHandler {

	protected Class<?> targetClass;

	private List<Field> columnFields;

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
		if (name != null)
			return name;

		Table t = targetClass.getAnnotation(Table.class);
		name = t.value();
		if ("".equals(name))
			name = targetClass.getSimpleName();
		return name;
	}

	public FieldList getNonAutoIncrementFieldList() {
		FieldList fieldList = new FieldList();
		for (Field field : getColumnFields()) {
			Column column = field.getAnnotation(Column.class); 
			if (!column.autoIncrement())
				fieldList.add(field.getName(), ColumnAnnotation.getName(column,field));
		}
		return fieldList;
	}

	protected synchronized List<Field> getColumnFields() {
		if (columnFields != null)
			return columnFields;
		columnFields = getFields(targetClass);
		for (int i = columnFields.size() - 1; i >= 0; i--) {
			if (!columnFields.get(i).isAnnotationPresent(Column.class))
				columnFields.remove(i);
		}
		return columnFields;
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

	public String getNonPrimaryKeyColumnComma() {
		StringBuilder sb = new StringBuilder();
		for (Field field : getColumnFields()) {
			Column column = field.getAnnotation(Column.class); 
			if (!column.primaryKey()) {
				if (sb.length() > 0)
					sb.append(",");
				sb.append(ColumnAnnotation.getName(column,field));
			}
		}
		return sb.toString();
	}

	public String getNonPrimaryKeyColumnEqualFieldComma() {
		StringBuilder sb = new StringBuilder();
		for (Field field : getColumnFields()) {
			Column column = field.getAnnotation(Column.class); 
			if (!column.primaryKey()) {
				if (sb.length() > 0)
					sb.append(",");
				sb.append(ColumnAnnotation.getName(column,field)).append(" = #{").append(field.getName()).append("}");
			}
		}
		return sb.toString();
	}

	public String getPrimaryKeyColumnEqualFieldAnd() {
		StringBuilder sb = new StringBuilder();
		for (Field field : getColumnFields()) {
			Column column = field.getAnnotation(Column.class); 
			if (column.primaryKey()) {
				if (sb.length() > 0)
					sb.append(" AND ");
				sb.append(ColumnAnnotation.getName(column,field)).append(" = #{").append(field.getName()).append("}");
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
				if (sb.length() > 0)
					sb.append(delimiter);
				sb.append(columnPrefix).append(ColumnAnnotation.getName(column,field)).append(" = ")
					.append(" #{").append(fieldPrefix).append(field.getName()).append("}");
			}
		}
		return sb.toString();
	}
	
	public String getNonPrimaryKeyNotNullColumnEqualFieldComma(Object object) {
		StringBuilder sb = new StringBuilder();
		BeanWrapper bean = new BeanWrapperImpl(object);
		for (Field field : getColumnFields()) {
			Column column = field.getAnnotation(Column.class); 
			if (!column.primaryKey()) {
				Object value = bean.getPropertyValue(field.getName());
				if (value != null) {
					if (sb.length() > 0)
						sb.append(", ");
					sb.append(ColumnAnnotation.getName(column,field)).append(" = ").append(" #{").append(field.getName()).append("}");
				}
			}
		}
		return sb.toString();
	}

	public String getNonPrimaryKeyNotNullColumnEqualColumnPlusFieldComma(Object object) {
		String columnName = null;
		StringBuilder sb = new StringBuilder();
		BeanWrapper bean = new BeanWrapperImpl(object);
		for (Field field : getColumnFields()) {
			Column column = field.getAnnotation(Column.class); 
			if (!column.primaryKey()) {
				Object value = bean.getPropertyValue(field.getName());
				if (value != null) {
					if (sb.length() > 0)
						sb.append(", ");
					columnName = ColumnAnnotation.getName(column,field); 
					sb.append(columnName).append(" = ").append(columnName).append(" + #{").append(field.getName()).append("}");
				}
			}
		}
		return sb.toString();
	}

	public String getColumnComma() {
		StringBuilder sb = new StringBuilder();
		for (Field field : getColumnFields()) {
			Column column = field.getAnnotation(Column.class);
			if (sb.length() > 0)
				sb.append(",");
			sb.append(ColumnAnnotation.getName(column,field));
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
			if (sb.length() > 0)
				sb.append(",");

			String columnName = ColumnAnnotation.getName(column,field);
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
				fieldList.add(field.getName(), ColumnAnnotation.getName(column,field));
			}
		}
		return fieldList;
	}

	public FieldList getNonAutoIncrementNotNullFieldList(Object object) {
		FieldList fieldList = new FieldList();
		BeanWrapper bean = new BeanWrapperImpl(object);
		for (Field field : getColumnFields()) {
			Column column = field.getAnnotation(Column.class); 
			if (!column.autoIncrement()) {
				Object value = bean.getPropertyValue(field.getName());
				if (value != null) {
					fieldList.add(field.getName(), ColumnAnnotation.getName(column,field));
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
				list.add((new ResultMapping.Builder(configuration, field.getName(), columnName, field.getType())).build());
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
	
	public List<Field> getReferenceFields(Class clazz) {
		List<Field> fields = getColumnFields(), rfields = new ArrayList<Field>();
		for (Field field : fields) {
			String references = field.getAnnotation(Column.class).references();
			if (references.startsWith(clazz.getSimpleName()+".")) {
				rfields.add(field);
			}
		}
		return rfields;
	}
}
