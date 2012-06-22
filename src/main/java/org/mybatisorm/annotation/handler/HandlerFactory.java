package org.mybatisorm.annotation.handler;

import java.util.Hashtable;

import org.mybatisorm.annotation.Join;
import org.mybatisorm.annotation.Table;
import org.mybatisorm.exception.AnnotationNotFoundException;

public class HandlerFactory {

	private static Hashtable<Class<?>,TableHandler> handlerMap = new Hashtable<Class<?>,TableHandler>();
	
	public static synchronized TableHandler getHandler(Class<?> clazz) {
		TableHandler handler = handlerMap.get(clazz);
		if (handler != null)
			return handler;
		
		if (clazz.getAnnotation(Table.class) != null)
			handler = new TableHandler(clazz);
		else if (clazz.getAnnotation(Join.class) != null)
			handler = new JoinHandler(clazz);
		else 
			throw new AnnotationNotFoundException("The class " + clazz.getName() +
				" has neither @Table nor @Join annotation.");
		handlerMap.put(clazz, handler);
		return handler;
	}
}
