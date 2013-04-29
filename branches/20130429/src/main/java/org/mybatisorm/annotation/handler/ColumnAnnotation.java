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
import java.util.Hashtable;

import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.ColumnNaming;
import org.mybatisorm.annotation.NamingRule;

public class ColumnAnnotation {
	private static Hashtable<Field, String> nameTable = new Hashtable<Field, String>();

	public static synchronized String getName(Field field, Column column) {
		if (column == null) return null;

		String name = nameTable.get(field);
		if (name != null) return name;

		ColumnNaming columnNaming = field.getDeclaringClass().getAnnotation(ColumnNaming.class);
		NamingRule naming;
		if (columnNaming == null || (naming = columnNaming.value()) == NamingRule.NONE) {
			name = "".equals(column.value()) ? "".equals(column.name()) ? field.getName() : column.name() : column.value();
		} else {
			name = field.getName();
			if (naming.has(NamingRule.UNDERSCORE)) {
				name = name.replaceAll("(?<=[A-Z])(?=[A-Z][a-z])|(?<=[^A-Z])(?=[A-Z])|(?<=[A-Za-z])(?=[^A-Za-z])", "_");
			}
			if (naming.has(NamingRule.UPPERCASE)) {
				name = name.toUpperCase();
			}
			if (naming.has(NamingRule.LOWERCASE)) {
				name = name.toLowerCase();
			}

			if (columnNaming.prefix() != null && columnNaming.prefix().length() > 0) name = columnNaming.prefix() + name;
		}

		nameTable.put(field, name);
		return name;
	}

	public static String getName(Field field) {
		return getName(field, field.getAnnotation(Column.class));
	}

	public static void main(String[] args) {}
}
