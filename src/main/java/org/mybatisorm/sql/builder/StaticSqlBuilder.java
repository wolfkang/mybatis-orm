package org.mybatisorm.sql.builder;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.log4j.Logger;

public class StaticSqlBuilder extends SqlBuilder {

	private static Logger logger = Logger.getLogger(StaticSqlBuilder.class);
	
	private SqlSource sqlSource;
	
	public StaticSqlBuilder(SqlSourceBuilder sqlSourceParser) {
		super(sqlSourceParser);
	}

	public BoundSql getBoundSql(Object parameterObject) {
		return sqlSource.getBoundSql(parameterObject);
	}

	protected void parse(String sql, Class<?> clazz) {
		sqlSource = getSqlSourceParser().parse(sql, clazz);
	}
}
