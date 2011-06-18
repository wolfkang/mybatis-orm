package org.mybatisorm.sql.source;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.mybatisorm.util.AnnotationUtil;
import org.mybatisorm.util.StringUtil;

public class SelectOneSqlSource extends SelectSqlSource {
	
	public SelectOneSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser, clazz);
	}

	public BoundSql getBoundSql(Object parameterObject) {
		String sql = staticSql + " WHERE " + 
				StringUtil.join(AnnotationUtil.getNotNullColumnNames(parameterObject),"%1$s = #{%1$s}"," AND ") +
				" LIMIT 1";
		return getBoundSql(sql,parameterObject);
	}
}
