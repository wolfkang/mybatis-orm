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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.mybatisorm.annotation.handler.HandlerFactory;
import org.mybatisorm.annotation.handler.TableHandler;

public class Condition {
	public enum Seperator {AND, OR};
	
	private Seperator seperator;
	private List<Object> list;
	private Query query;
	
	static class Item { 
		private String field; 
		private String operator; 
		private Object value; 

		public Item(String field, String operator, Object value){ 
			this.field = field;
			this.operator = operator;
			this.value = value; 
		}
		public Object getValue() {
			return value;
		}
		public String getField() {
			return field;
		}
		public String getOperator() {
			return operator;
		}
	}
	
	public Condition() {
		 this(Seperator.AND);
	}

	public Condition(Seperator seperator) {
		this.seperator = seperator;
		this.list = new LinkedList<Object>();
	}
	
	public String getSeperator() {
		return Seperator.AND.equals(seperator) ? " AND " : " OR ";
	}
	
	public void setSeperator(Seperator seperator) {
		this.seperator = seperator;
	}
	
	public Condition add(String field, String operator, Object ... value) {
		list.add(new Item(field,
				(operator == null) ? null : operator.trim(),
						(value != null && value.length == 1) ? value[0] : value));
		return this;
	}
	
	public Condition add(String operator, Object ... value) {
		add(null,operator,value);
		return this;
	}
	
	public Condition add(String condition) {
		list.add(condition);
		return this;
	}
	
	public Condition add(Condition condition) {
		list.add(condition);
		return this;
	}
	
	public String build(Query query) {
		this.query = query;
		TableHandler handler = HandlerFactory.getHandler(query.getParameter());
		StringBuilder sb = new StringBuilder();
		Iterator<Object> it = list.iterator();
		String operator = null, column = null;
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof Item) {
				Item item = (Item)o;
				operator = item.getOperator();
				if (item.getField() != null) {
					column = handler.findColumnName(item.getField());
					sb.append(column==null?item.getField():column).append(" ");
				}
				sb.append(item.getOperator()).append(" ");
				if ("IN".equalsIgnoreCase(operator) || "NOT IN".equalsIgnoreCase(operator)) {
					sb.append("(").append(addProperties(item.getValue(),",")).append(")");
				} else if ("BETWEEN".equalsIgnoreCase(operator)) {
					sb.append(addProperties(item.getValue()," AND "));
				} else if ("EXISTS".equalsIgnoreCase(operator) || "NOT EXISTS".equalsIgnoreCase(operator)) {
					sb.append("(").append(addProperty(item.getValue())).append(")");
				} else {
					sb.append(query.addProperty(item.getValue()));
				}
			} else if (o instanceof String) {
				sb.append("(").append(o).append(")");
			} else if (o instanceof Condition) {
				sb.append("(").append(((Condition)o).build(query)).append(")");
			}
			if (it.hasNext())
				sb.append(" ").append(seperator).append(" ");
		}
		return sb.toString();
	}
	
	
	public String addProperties(Object values, String delimiter) {
		if (values.getClass().isArray()) {
			String name = values.getClass().getComponentType().getName();
			if ("int".equals(name)) {
				return addProperties((int[])values,delimiter);
			} else if ("long".equals(name)) {
				return addProperties((long[])values,delimiter);
			} else if ("float".equals(name)) {
				return addProperties((float[])values,delimiter);
			} else if ("double".equals(name)) {
				return addProperties((double[])values,delimiter);
			}
			return addProperties((Object[])values,delimiter);
		}
		return (values instanceof Collection) ? addProperties((Collection<?>)values,delimiter) :
				addProperty(values);
	}
	
	private String addProperties(int[] values, String delimiter) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			if (i > 0)
				sb.append(delimiter);
			sb.append(addProperty(values[i]));
		}
		return sb.toString();
	}
	
	private String addProperties(long[] values, String delimiter) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			if (i > 0)
				sb.append(delimiter);
			sb.append(addProperty(values[i]));
		}
		return sb.toString();
	}
	
	private String addProperties(float[] values, String delimiter) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			if (i > 0)
				sb.append(delimiter);
			sb.append(addProperty(values[i]));
		}
		return sb.toString();
	}
	
	private String addProperties(double[] values, String delimiter) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			if (i > 0)
				sb.append(delimiter);
			sb.append(addProperty(values[i]));
		}
		return sb.toString();
	}
	
	private String addProperties(Object[] values, String delimiter) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			if (i > 0)
				sb.append(delimiter);
			sb.append(addProperty(values[i]));
		}
		return sb.toString();
	}
	
	private String addProperty(Object value) {
		return query.addProperty(value);
	}
	
}
