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
	public void add(String column, String operator, Object value) {
		list.add(new Item(column,operator,value));
	}
	public Iterator<Item> iterator() {
		return list.iterator();
	}
}
