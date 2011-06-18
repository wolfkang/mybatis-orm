package org.mybatisorm.sql.source;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.mybatisorm.sql.builder.DynamicSqlBuilder;
import org.mybatisorm.util.AnnotationUtil;
import org.mybatisorm.util.StringUtil;

public abstract class SelectSqlSource extends DynamicSqlBuilder {

	public SelectSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser);
		staticSql = String.format("SELECT %1$s FROM %2$s ",
				StringUtil.join(AnnotationUtil.getColumnNames(clazz),", "),
				AnnotationUtil.getTableName(clazz));
	}

}
