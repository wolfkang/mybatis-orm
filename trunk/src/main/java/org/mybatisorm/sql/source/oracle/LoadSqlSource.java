package org.mybatisorm.sql.source.oracle;

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
		sb.append(" WHERE ");
		String cf = AnnotationUtil.getNotNullColumnEqualFieldAnd(parameter);
		if (cf.length() > 0)
			sb.append(cf).append(" AND ");
		sb.append("ROWNUM = 1");
		return getBoundSql(sb.toString(),parameter);
	}
}
