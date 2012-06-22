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
package org.mybatisorm;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Condition {
	public enum Seperator {AND, OR};
	
	private Seperator seperator;
	private List<Item> list;
	
	static class Item { 
		private String column; 
		private String operator; 
		private Object value; 

		public Item(String column, String operator, Object value){ 
			this.setColumn(column); 
			this.setOperator(operator); 
			this.value = value; 
		}
		public Object getValue() {
			return value;
		}
		public String getColumn() {
			return column;
		}
		public void setColumn(String column) {
			this.column = column;
		}
		public String getOperator() {
			return operator;
		}
		public void setOperator(String operator) {
			this.operator = operator;
		}
	}
	
	public Condition() {
		 this(Seperator.AND);
	}
	public Condition(Seperator seperator) {
		this.seperator = seperator;
		this.list = new LinkedList<Item>();
	}
	public String getSeperator() {
		return Seperator.AND.equals(seperator) ? " AND " : " OR ";
	}
	public void setSeperator(Seperator seperator) {
		this.seperator = seperator;
	}
	public void add(String column, String operator, Object ... value) {
		Object v = value;
		if (operator != null)
			operator = operator.trim();
		if (value != null && value.length == 1)
			v = value[0];
		list.add(new Item(column,operator,v));
	}
	public Iterator<Item> iterator() {
		return list.iterator();
	}
}
