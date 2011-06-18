package org.mybatisorm.sql.source;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.mybatisorm.sql.builder.StaticSqlBuilder;
import org.mybatisorm.util.AnnotationUtil;
import org.mybatisorm.util.StringUtil;

public class UpdateSqlSource extends StaticSqlBuilder {

	public UpdateSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super();
		String sql = String.format("UPDATE %1$s SET %2$s WHERE %3$s",
				AnnotationUtil.getTableName(clazz),
				StringUtil.join(AnnotationUtil.getNonPrimaryKeyColumnNames(clazz),"%1$s = #{%1$s}",", "),
				StringUtil.join(AnnotationUtil.getPrimaryKeyColumnNames(clazz),"%1$s = #{%1$s}"," AND "));
		parse(sqlSourceParser, sql, clazz);
	}

}
