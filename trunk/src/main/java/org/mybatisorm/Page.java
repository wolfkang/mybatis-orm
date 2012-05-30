package org.mybatisorm;

import java.util.Collections;
import java.util.List;

public class Page<T> {

	private int pageNumber;
	private int rows;
	private int count;
	private List<T> list;

	public Page(int pageNumber, int rows) {
		this(pageNumber,rows,0);
	}
	
	public Page(int pageNumber, int rows, int totalCount) {
		this.pageNumber = pageNumber;
		this.rows = rows;
		this.count = totalCount;
		this.list = Collections.emptyList();
	}
	
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int totalCount) {
		this.count = totalCount;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public int getLast() {
		return count == 0 ? 1 : ((int)(count-1)/rows+1);
	}
}
