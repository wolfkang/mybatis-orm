package org.mybatisorm.sql.source;

import java.util.List;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.mybatisorm.annotation.SqlCommand;
import org.mybatisorm.sql.builder.DynamicSqlBuilder;

@SqlCommand(value=SqlCommandType.SELECT)
public abstract class AbstractSelectSqlSource extends DynamicSqlBuilder {

	public AbstractSelectSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser,clazz);
		staticSql = "SELECT " + handler.getColumnAsFieldComma() +
				" FROM " + handler.getName(); 
	}
	
	public AbstractSelectSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz, String staticSql) {
		super(sqlSourceParser,clazz);
		this.staticSql = staticSql; 
	}
	
	public List<ResultMapping> getResultMappingList() {
		return handler.getResultMappingList(sqlSourceBuilder.getConfiguration());
	}

	public Class<?> getResultType() {
		return handler.getTargetClass();
	}
}
