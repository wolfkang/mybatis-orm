package org.mybatisorm.sql.source;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.log4j.Logger;

public class IncrementSqlSource extends AbstractUpdateSqlSource {

	private static Logger logger = Logger.getLogger(IncrementSqlSource.class);
	
	public IncrementSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser,clazz);
	}

	public BoundSql getBoundSql(final Object parameter) {
		return makeSet(
				handler.getNonPrimaryKeyNotNullColumnEqualColumnPlusFieldComma(parameter),
				parameter);
	}
}
