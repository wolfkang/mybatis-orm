package org.mybatisorm.sql.source.oracle;

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
		return generated == null ? new NoKeyGenerator() : newSelectKeyGenerator(generated,parentId);
	}
	
	protected GeneratedField getGeneratedField(Class<?> clazz) {
		for (Field field : ColumnHandler.getDeclaredFields(clazz)) {
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class); 
				String sequence = column.sequence(); 
				if (!"".equals(sequence)) {
					return new GeneratedField(field,"SELECT "+sequence + ".NEXTVAL " + field.getName() + " FROM DUAL");
				}
			}
		}
		return null;
	}
	
	public boolean getExecuteBefore() {
		return true;
	}
}
