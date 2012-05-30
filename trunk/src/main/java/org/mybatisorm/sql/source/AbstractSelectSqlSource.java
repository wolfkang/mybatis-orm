package org.mybatisorm.sql.source;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.SqlCommandType;
import org.mybatisorm.annotation.AnnotationUtil;
import org.mybatisorm.annotation.SqlCommand;
import org.mybatisorm.sql.builder.DynamicSqlBuilder;

@SqlCommand(SqlCommandType.SELECT)
public abstract class AbstractSelectSqlSource extends DynamicSqlBuilder {

	public AbstractSelectSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser);
		staticSql = "SELECT " + AnnotationUtil.getColumnAsFieldComma(clazz) +
				" FROM " + AnnotationUtil.getTableName(clazz); 
	}
}
