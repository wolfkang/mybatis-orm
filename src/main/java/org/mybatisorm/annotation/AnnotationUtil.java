package org.mybatisorm.annotation;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.log4j.Logger;
import org.mybatisorm.exception.AnnotationNotFoundException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class AnnotationUtil {

	private static Logger logger = Logger.getLogger(AnnotationUtil.class);
	
	public static String getTableName(Class<?> clazz) {
		Table t = clazz.getAnnotation(Table.class);
		if (t == null || "".equals(t.value()))
			throw new AnnotationNotFoundException(clazz.getName() + " has no @Table annotation.");
		return t.value();
	}

	public static SqlCommandType getSqlCommandType(Class<?> sqlSourceClass) {
		SqlCommand cmd = sqlSourceClass.getAnnotation(SqlCommand.class); 
		if (cmd == null)
			throw new AnnotationNotFoundException(sqlSourceClass.getName() + " has no @SqlCommand annotation.");
		return cmd.value();
	}
	
	public static FieldList getNonAutoIncrementFieldList(Class<?> clazz) {
		FieldList fieldList = new FieldList();
		for (Field field : getDeclaredFields(clazz)) {
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class); 
				if (!column.autoIncrement())
					fieldList.add(field.getName(), getName(column,field));
			}
		}
		return fieldList;
	}

	public static List<Field> getDeclaredFields(Class<?> clazz) {
		List<Field> fields = new LinkedList<Field>();
		Class<?> superClass = clazz.getSuperclass(); 
		if (superClass.isAnnotationPresent(Table.class)) {
			if (getTableName(superClass).equals(getTableName(clazz)))
				fields.addAll(getDeclaredFields(clazz.getSuperclass()));
		}
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		return fields;
	}
	
	public static String getNonPrimaryKeyColumnComma(Class<?> clazz) {
		StringBuilder sb = new StringBuilder();
		for (Field field : getDeclaredFields(clazz)) {
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class); 
				if (!column.primaryKey()) {
					if (sb.length() > 0)
						sb.append(",");
					sb.append(getName(column,field));
				}
			}
		}
		return sb.toString();
	}

	public static String getNonPrimaryKeyColumnEqualFieldComma(Class<?> clazz) {
		StringBuilder sb = new StringBuilder();
		for (Field field : getDeclaredFields(clazz)) {
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class); 
				if (!column.primaryKey()) {
					if (sb.length() > 0)
						sb.append(",");
					sb.append(getName(column,field)).append(" = #{").append(field.getName()).append("}");
				}
			}
		}
		return sb.toString();
	}
	
	public static String getPrimaryKeyColumnEqualFieldAnd(Class<?> clazz) {
		StringBuilder sb = new StringBuilder();
		for (Field field : getDeclaredFields(clazz)) {
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class); 
				if (column.primaryKey()) {
					if (sb.length() > 0)
						sb.append(" AND ");
					sb.append(getName(column,field)).append(" = #{").append(field.getName()).append("}");
				}
			}
		}
		return sb.toString();
	}

	public static String getNotNullColumnEqualFieldComma(Object object) {
		return getNotNullColumnEqualField(object, "", ", ");
	}

	public static String getNotNullColumnEqualFieldAnd(Object object) {
		return getNotNullColumnEqualField(object, "", " AND ");
	}
	
	public static String getNotNullColumnEqualFieldAnd(Object object, String prefix) {
		return getNotNullColumnEqualField(object, prefix, " AND ");
	}
	
	private static String getNotNullColumnEqualField(Object object, String prefix, String delimiter) {
		StringBuilder sb = new StringBuilder();
		BeanWrapper bean = new BeanWrapperImpl(object);
		for (Field field : getDeclaredFields(object.getClass())) {
			if (field.isAnnotationPresent(Column.class)) {
				Object value = bean.getPropertyValue(field.getName());
				if (value != null) {
					Column column = field.getAnnotation(Column.class);
					if (sb.length() > 0)
						sb.append(delimiter);
					sb.append(getName(column,field)).append(" = ").append(" #{").append(prefix).append(field.getName()).append("}");
				}
			}
		}
		return sb.toString();
	}
	
	public static String getNonPrimaryKeyNotNullColumnEqualFieldComma(Object object) {
		StringBuilder sb = new StringBuilder();
		BeanWrapper bean = new BeanWrapperImpl(object);
		for (Field field : getDeclaredFields(object.getClass())) {
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class); 
				if (!column.primaryKey()) {
					Object value = bean.getPropertyValue(field.getName());
					if (value != null) {
						if (sb.length() > 0)
							sb.append(", ");
						sb.append(getName(column,field)).append(" = ").append(" #{").append(field.getName()).append("}");
					}
				}
			}
		}
		return sb.toString();
	}
	
	public static String getNonPrimaryKeyNotNullColumnEqualColumnPlusFieldComma(Object object) {
		String columnName = null;
		StringBuilder sb = new StringBuilder();
		BeanWrapper bean = new BeanWrapperImpl(object);
		for (Field field : getDeclaredFields(object.getClass())) {
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class); 
				if (!column.primaryKey()) {
					Object value = bean.getPropertyValue(field.getName());
					if (value != null) {
						if (sb.length() > 0)
							sb.append(", ");
						columnName = getName(column,field); 
						sb.append(columnName).append(" = ").append(columnName).append(" + #{").append(field.getName()).append("}");
					}
				}
			}
		}
		return sb.toString();
	}
	
	private static String getName(Column column, Field field) {
		return "".equals(column.value()) ?
				"".equals(column.name()) ? field.getName() : column.name()
						: column.value();
	}
	
	public static String getColumnComma(Class<?> clazz) {
		StringBuilder sb = new StringBuilder();
		for (Field field : getDeclaredFields(clazz)) {
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);
				if (sb.length() > 0)
					sb.append(",");
				sb.append(getName(column,field));
			}
		}
		return sb.toString();
	}

	public static String getColumnAsFieldComma(Class<?> clazz) {
		StringBuilder sb = new StringBuilder();
		for (Field field : getDeclaredFields(clazz)) {
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);
				if (sb.length() > 0)
					sb.append(",");
				
				String columnName = getName(column,field);
				if (!"".equals(columnName)) {
					sb.append(columnName).append(" ");
				}
				sb.append(field.getName());
			}
		}
		return sb.toString();
	}
	
	public static FieldList getNotNullFieldList(Object object) {
		FieldList fieldList = new FieldList();
		BeanWrapper bean = new BeanWrapperImpl(object);
		for (Field field : getDeclaredFields(object.getClass())) {
			if (field.isAnnotationPresent(Column.class)) {
				Object value = bean.getPropertyValue(field.getName());
				if (value != null) {
					Column column = field.getAnnotation(Column.class);
					fieldList.add(field.getName(), getName(column,field));
				}
			}
		}
		return fieldList;
	}

	public static <T>String join(final Collection<T> objs, final String delimiter) {
		if (objs == null || objs.isEmpty())
			return "";
		Iterator<T> iter = objs.iterator();
		StringBuilder buffer = new StringBuilder(iter.next().toString());
		while (iter.hasNext())
			buffer.append(delimiter).append(iter.next().toString());
		return buffer.toString();
	}

	public static <T>String join(final Iterable<T> objs, final String format, final String delimiter) {
		if (objs == null)
			return "";
		Iterator<T> iter = objs.iterator();
		if (!iter.hasNext())
			return "";
		
		StringBuilder buffer = new StringBuilder(String.format(format, iter.next().toString()));
		while (iter.hasNext())
			buffer.append(delimiter).append(String.format(format, iter.next().toString()));
		return buffer.toString();
	}


}
