package org.mybatisorm.sql.source.mysql;

import java.lang.reflect.Field;

import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.handler.ColumnHandler;
import org.mybatisorm.sql.source.ValueGeneratorImpl;

public class ValueGenerator extends ValueGeneratorImpl {

	protected KeyGenerator keyGenerator(Builder builder, String parentId, Class<?> clazz) {
		GeneratedField generated = getGeneratedField(clazz);
		return generated == null ? new NoKeyGenerator() : newJdbc3KeyGenerator(builder,generated);
	}

	protected GeneratedField getGeneratedField(Class<?> clazz) {
		for (Field field : ColumnHandler.getDeclaredFields(clazz)) {
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class); 
				if (column.autoIncrement()) {
					return new GeneratedField(field);
				}
			}
		}
		return null;
	}
	
	public boolean getExecuteBefore() {
		return true;
	}
}
