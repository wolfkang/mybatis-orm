package org.mybatisorm.sql.source;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlCommandType;
import org.mybatisorm.Query;
import org.mybatisorm.annotation.AnnotationUtil;
import org.mybatisorm.annotation.SqlCommand;
import org.mybatisorm.sql.builder.DynamicSqlBuilder;

@SqlCommand(SqlCommandType.SELECT)
public class CountSqlSource extends DynamicSqlBuilder {
	
	public CountSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser);
		staticSql = "SELECT COUNT(*) FROM " + AnnotationUtil.getTableName(clazz);
	}

	public BoundSql getBoundSql(Object parameter) {
		String where = (parameter instanceof Query) ? ((Query)parameter).getCondition() :
			AnnotationUtil.getNotNullColumnEqualFieldAnd(parameter);
		return makeWhere(where,parameter);
	}
}
