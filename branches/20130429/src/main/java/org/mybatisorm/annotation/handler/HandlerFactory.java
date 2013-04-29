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

	public static TableHandler getHandler(Object parameter) {
		return getHandler(parameter.getClass());
	}
}
