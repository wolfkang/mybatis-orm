package org.mybatisorm.sql.source;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.mybatisorm.sql.builder.DynamicSqlBuilder;
import org.mybatisorm.util.AnnotationUtil;
import org.mybatisorm.util.StringUtil;

public class SelectCountSqlSource extends DynamicSqlBuilder {
	
	public SelectCountSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser);
		staticSql = String.format("SELECT COUNT(*) FROM %1$s ",
				AnnotationUtil.getTableName(clazz));
	}

	public BoundSql getBoundSql(Object parameterObject) {
		String sql = staticSql + " WHERE " + 
				StringUtil.join(AnnotationUtil.getNotNullColumnNames(parameterObject),"%1$s = #{%1$s}"," AND ") +
				" LIMIT 1";
		return getBoundSql(sql,parameterObject);
	}
}
