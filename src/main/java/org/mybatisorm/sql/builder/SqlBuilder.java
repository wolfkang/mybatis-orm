package org.mybatisorm.sql.builder;

import java.util.List;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.mybatisorm.annotation.handler.HandlerFactory;
import org.mybatisorm.annotation.handler.TableHandler;

public abstract class SqlBuilder implements SqlSource {

	protected SqlSourceBuilder sqlSourceBuilder;

	protected TableHandler handler;
	
	public SqlBuilder(SqlSourceBuilder sqlSourceBuilder, Class<?> targetClass) {
		this.sqlSourceBuilder = sqlSourceBuilder;
		this.handler = HandlerFactory.getHandler(targetClass);
	}

	public abstract BoundSql getBoundSql(Object parameterObject);

	public SqlSourceBuilder getSqlSourceParser() {
		return sqlSourceBuilder;
	}

	public List<ResultMapping> getResultMappingList() {
		return null;
	}

	public Class<?> getResultType() {
		return null;
	}
}
