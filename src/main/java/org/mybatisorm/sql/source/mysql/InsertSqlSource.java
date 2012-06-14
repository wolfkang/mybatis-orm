package org.mybatisorm.sql.source.mysql;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.log4j.Logger;
import org.mybatisorm.annotation.AnnotationUtil;
import org.mybatisorm.annotation.FieldList;
import org.mybatisorm.sql.source.AbstractInsertSqlSource;

public class InsertSqlSource extends AbstractInsertSqlSource {

	private static Logger logger = Logger.getLogger(InsertSqlSource.class);
	
	public InsertSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser,clazz);
	}

	public BoundSql getBoundSql(final Object parameter) {
		return getBoundSql(parameter,AnnotationUtil.getNonAutoIncrementNotNullFieldList(parameter));
	}
}
