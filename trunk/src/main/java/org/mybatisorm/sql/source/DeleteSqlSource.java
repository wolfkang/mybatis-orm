package org.mybatisorm.sql.source;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.mybatisorm.sql.builder.DynamicSqlBuilder;
import org.mybatisorm.util.AnnotationUtil;
import org.mybatisorm.util.StringUtil;

public class DeleteSqlSource extends DynamicSqlBuilder {

	public DeleteSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser);
		staticSql = "DELETE FROM "+AnnotationUtil.getTableName(clazz);
	}

	public BoundSql getBoundSql(Object parameterObject) {
		String sql = staticSql + " WHERE " +
				StringUtil.join(AnnotationUtil.getNotNullColumnNames(parameterObject),"%1$s = #{%1$s}"," AND ");
		return getBoundSql(sql,parameterObject);
	}

}
