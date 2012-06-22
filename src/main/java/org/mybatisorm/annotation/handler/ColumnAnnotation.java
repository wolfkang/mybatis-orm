package org.mybatisorm.annotation.handler;

import java.lang.reflect.Field;
import java.util.Hashtable;

import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.ColumnNaming;
import org.mybatisorm.annotation.NamingRule;

public class ColumnAnnotation {
	private static Hashtable<Field,String> nameTable = new Hashtable<Field,String>();
	
	public static synchronized String getName(Column column, Field field) {
		String name = nameTable.get(field);
		if (name != null)
			return name;
		
		ColumnNaming columnNaming = field.getDeclaringClass().getAnnotation(ColumnNaming.class); 
		NamingRule naming;
		if (columnNaming == null || (naming = columnNaming.value()) == NamingRule.NONE) {
			name = "".equals(column.value()) ?
					"".equals(column.name()) ? field.getName() : column.name()
							: column.value();
		} else {
			name = field.getName();
			if (naming.has(NamingRule.UNDERSCORE)) {
				name = name.replaceAll("(?<=[A-Z])(?=[A-Z][a-z])|(?<=[^A-Z])(?=[A-Z])|(?<=[A-Za-z])(?=[^A-Za-z])","_");
			}
			if (naming.has(NamingRule.UPPERCASE)) {
				name = name.toUpperCase();
			}
			if (naming.has(NamingRule.LOWERCASE)) {
				name = name.toLowerCase();
			}
		}
		nameTable.put(field, name);
		return name;
	}
	
	public static String getName(Field field) {
		return getName(field.getAnnotation(Column.class),field);
	}
	
	public static void main(String[] args) {
	}
}
