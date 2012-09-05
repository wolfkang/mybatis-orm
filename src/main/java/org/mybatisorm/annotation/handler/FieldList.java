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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FieldList implements Iterable<String[]> {
	private List<String> columnNames;
	private List<String> fieldNames;
	
	private static class FieldIterator implements Iterator<String[]> {
		private FieldList fieldList;
		private int size;
		private int index;
		
		public FieldIterator(FieldList fieldList) {
			this.fieldList = fieldList;
			this.size = fieldList.getFieldNames().size();
			this.index = 0;
		}
		public boolean hasNext() {
			return (index < size);
		}
		public String[] next() {
			return new String[] {fieldList.getFieldNames().get(index), fieldList.getColumnNames().get(index++)};
		}
		public void remove() {
		}
	}
	public FieldList() {
		this.setColumnNames(new LinkedList<String>());
		this.setFieldNames(new LinkedList<String>());
	}
	
	public void add(String fieldName, String columnName) {
		this.getColumnNames().add(columnName);
		this.getFieldNames().add(fieldName);
	}
	
	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public List<String> getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
	}

	public Iterator<String[]> iterator() {
		return new FieldIterator(this);
	}
	
	public boolean isEmpty() {
		return fieldNames.isEmpty();
	}
	
}
