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
package org.mybatisorm.sql.source.oracle;

import java.lang.reflect.Field;

import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.handler.TableHandler;
import org.mybatisorm.sql.source.ValueGeneratorImpl;

public class SequenceValueGenerator extends ValueGeneratorImpl {

	protected KeyGenerator keyGenerator(Builder builder, String parentId, Class<?> clazz) {
		GeneratedField generated = getGeneratedField(clazz);
		return generated == null ? new NoKeyGenerator() : newSelectKeyGenerator(generated,parentId);
	}
	
	protected GeneratedField getGeneratedField(Class<?> clazz) {
		for (Field field : TableHandler.getFields(clazz)) {
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
