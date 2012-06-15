package org.mybatisorm.annotation.handler;

import org.apache.ibatis.mapping.SqlCommandType;
import org.mybatisorm.annotation.SqlCommand;
import org.mybatisorm.exception.AnnotationNotFoundException;

public class SqlCommandHandler {
	public static SqlCommand getSqlCommand(Class<?> sqlSourceClass) {
		SqlCommand cmd = sqlSourceClass.getAnnotation(SqlCommand.class); 
		if (cmd == null)
			throw new AnnotationNotFoundException(sqlSourceClass.getName() + " has no @SqlCommand annotation.");
		return cmd;
	}
	
	public static SqlCommandType getSqlCommandType(Class<?> sqlSourceClass) {
		return getSqlCommand(sqlSourceClass).value();
	}
	
}
