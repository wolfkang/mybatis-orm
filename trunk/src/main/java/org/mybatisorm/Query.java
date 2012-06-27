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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.mybatisorm.Condition.Item;
import org.mybatisorm.annotation.handler.TableHandler;

public class Query {

	private static final String PARAMETER_RE = "#\\{([0-9a-zA-Z_\\.]+)\\}";

	private static final Pattern PARAMETER_PATTERN = Pattern.compile(PARAMETER_RE);
	
	public static final String PARAMETER_PREFIX = "parameter.";

	private static final String CONDITION_REPLACEMENT = "#{" + PARAMETER_PREFIX + "$1}";
	
	private static Logger logger = Logger.getLogger(Query.class);
	
	private String orderBy;
	private String condition;
	private Object parameter;
	private Map<String,Object> properties;
	
	private int pageNumber;
	private int rows;
	private int start;
	private int end;
	
	public Query() {
	}
	
	public Query(Object parameter) {
		this.parameter = parameter;
	}
	
	public Query(Object parameter, String orderBy) {
		this.parameter = parameter;
		this.orderBy = orderBy;
	}
	
	public Query(Object parameter, String condition, String orderBy) {
		this.parameter = parameter;
		this.orderBy = orderBy;
		setCondition(condition);
	}
	
	public Query(Object parameter, Condition condition, String orderBy) {
		this.parameter = parameter;
		this.orderBy = orderBy;
		setCondition(condition);
	}
	
	public Query(Object parameter, String orderBy, int pageNumber, int rows) {
		this.parameter = parameter;
		this.orderBy = orderBy;
		this.pageNumber = pageNumber;
		this.rows = rows;
	}
	
	public Query(Object parameter, String condition, String orderBy, int pageNumber, int rows) {
		this.parameter = parameter;
		this.orderBy = orderBy;
		this.pageNumber = pageNumber;
		this.rows = rows;
		setCondition(condition);
	}

	public Query(Object parameter, Condition condition, String orderBy, int pageNumber, int rows) {
		this.parameter = parameter;
		this.orderBy = orderBy;
		this.pageNumber = pageNumber;
		this.rows = rows;
		setCondition(condition);
	}
	
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public boolean hasOrderBy() {
		return orderBy != null;
	}
	public Object getParameter() {
		return parameter;
	}
	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}
	public String getCondition() {
		return condition;
	}
	public boolean hasCondition() {
		return condition != null;
	}
	private void setCondition(String condition) {
		if (condition != null)
			this.condition = replaceParam(condition);
	}
	private static String replaceParam(String value) {
		return value.replaceAll(PARAMETER_RE, CONDITION_REPLACEMENT);
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Map<String,Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String,Object> properties) {
		this.properties = properties;
	}
	
	private void setCondition(Condition condition) {
		StringBuilder sb = new StringBuilder();
		Iterator<Item> it = condition.iterator();
		String operator = null;
		while (it.hasNext()) {
			Item item = it.next();
			operator = item.getOperator();
			sb.append(item.getColumn()).append(" ").append(item.getOperator()).append(" ");
			if ("IN".equalsIgnoreCase(operator) || "NOT IN".equalsIgnoreCase(operator)) {
				sb.append("(").append(addProperties(item.getValue(),",")).append(")");
			} else if ("BETWEEN".equalsIgnoreCase(operator)) {
				sb.append(addProperties(item.getValue()," AND "));
			} else {
				sb.append(addProperty(item.getValue()));
			}
			if (it.hasNext())
				sb.append(condition.getSeperator());
		}
		this.condition = sb.toString();
	}
	
	private String addProperty(Object value) {
		if (value instanceof String) {
			Matcher m = PARAMETER_PATTERN.matcher((String)value);
			if (m.find())
				return replaceParam((String)value);
		}
		if (properties == null)
			properties = new HashMap<String,Object>();
		int index = properties.size();
		properties.put(String.valueOf(index),value);
		return "#{properties."+index+"}";
	}
	
	private String addProperties(Object values, String delimiter) {
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
	
	private String addProperties(Collection<?> values, String delimiter) {
		StringBuilder sb = new StringBuilder();
		Iterator<?> it = values.iterator();
		while(it.hasNext()) {
			Object o = it.next();
			sb.append(addProperty(o));
			if (it.hasNext())
				sb.append(delimiter);
		}
		return sb.toString();
	}
	
	public String getNotNullColumnEqualFieldAnd(TableHandler handler) {
		return parameter == null ? "" :
			handler.getNotNullColumnEqualFieldAnd(parameter,PARAMETER_PREFIX);
	}
	
	public static void main(String[] args) {
		Matcher m = PARAMETER_PATTERN.matcher("'%' || #{board.messageId} || '%'");
//		Matcher m = PARAMETER_PATTERN.matcher("'%' || #{name} || '%'#{board.messageId}");
		System.out.println(m.find());
	}
}