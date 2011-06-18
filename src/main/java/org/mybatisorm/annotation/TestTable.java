package org.mybatisorm.annotation;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.Table;


@Table("test")
public class TestTable {

	@Column(name="column1")
	private String column1;
	@Column
	private String column2;
	@Column(primaryKey=true)
	private Integer columnpk;

	public String getColumn1() {
		return column1;
	}

	public void setColumn1(String column1) {
		this.column1 = column1;
	}

	public String getColumn2() {
		return column2;
	}

	public void setColumn2(String column2) {
		this.column2 = column2;
	}

	public void setColumnpk(Integer columnpk) {
		this.columnpk = columnpk;
	}

	public Integer getColumnpk() {
		return columnpk;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

}
