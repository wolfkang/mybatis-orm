package org.mybatisorm.sql.source.mysql;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.mybatisorm.annotation.AnnotationUtil;
import org.mybatisorm.sql.source.AbstractSelectSqlSource;

public class LoadSqlSource extends AbstractSelectSqlSource {
	
	public LoadSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser, clazz);
	}

	public BoundSql getBoundSql(Object parameter) {
		StringBuilder sb = new StringBuilder(staticSql);
		String cf = AnnotationUtil.getNotNullColumnEqualFieldAnd(parameter);
		if (cf.length() > 0)
			sb.append(" WHERE ").append(cf);
		sb.append(" LIMIT 1");
		return getBoundSql(sb.toString(),parameter);
	}
}
