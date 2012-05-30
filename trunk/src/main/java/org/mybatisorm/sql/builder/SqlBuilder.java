package org.mybatisorm.sql.builder;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;

public abstract class SqlBuilder implements SqlSource {

	private SqlSourceBuilder sqlSourceParser;

	public SqlBuilder(SqlSourceBuilder sqlSourceParser) {
		this.sqlSourceParser = sqlSourceParser;
	}

	public abstract BoundSql getBoundSql(Object parameterObject);

	public SqlSourceBuilder getSqlSourceParser() {
		return sqlSourceParser;
	}
}
