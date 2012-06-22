package org.mybatisorm.sql.builder;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.log4j.Logger;

public abstract class DynamicSqlBuilder extends SqlBuilder {

	private static Logger logger = Logger.getLogger(DynamicSqlBuilder.class);

	protected String staticSql;

	public DynamicSqlBuilder(SqlSourceBuilder sqlSourceParser, Class<?> targetClass) {
		super(sqlSourceParser,targetClass);
	}

	public abstract BoundSql getBoundSql(Object parameterObject);

	protected BoundSql getBoundSql(String sql, Object parameterObject) {
		return getSqlSourceParser().parse(sql, parameterObject.getClass()).getBoundSql(parameterObject);
	}

	protected BoundSql makeWhere(String where, Object parameter) {
		return getBoundSql(
				where != null && where.length() > 0 ?
						staticSql + " WHERE " + where : staticSql,
						parameter);
	}
}
