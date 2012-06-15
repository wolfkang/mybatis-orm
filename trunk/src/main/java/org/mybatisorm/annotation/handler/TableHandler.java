package org.mybatisorm.annotation.handler;

import java.util.Hashtable;

import org.mybatisorm.annotation.Table;
import org.mybatisorm.exception.AnnotationNotFoundException;

public class TableHandler {
	
	private static Hashtable<Class<?>,String> tableName = new Hashtable<Class<?>,String>();

	public static String getName(Class<?> clazz) {
		if (tableName.contains(clazz))
			return tableName.get(clazz);
		
		Table t = clazz.getAnnotation(Table.class);
		if (t == null || "".equals(t.value()))
			throw new AnnotationNotFoundException(clazz.getName() + " has no @Table annotation.");
		
		tableName.put(clazz, t.value());
		return t.value();
	}


}
