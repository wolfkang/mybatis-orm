package org.mybatisorm.sql.source;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlCommandType;
import org.mybatisorm.Query;
import org.mybatisorm.annotation.SqlCommand;
import org.mybatisorm.sql.builder.DynamicSqlBuilder;

@SqlCommand(SqlCommandType.DELETE)
public class DeleteSqlSource extends DynamicSqlBuilder {

	public DeleteSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser,clazz);
		staticSql = "DELETE FROM "+handler.getName();
	}

	public BoundSql getBoundSql(Object parameter) {
		String where = (parameter instanceof Query) ? ((Query)parameter).getCondition() :
			handler.getNotNullColumnEqualFieldAnd(parameter);
		return makeWhere(where,parameter);
	}

}
