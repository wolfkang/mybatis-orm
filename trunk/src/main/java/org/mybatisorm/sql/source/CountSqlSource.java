package org.mybatisorm.sql.source;

import java.util.Collections;
import java.util.List;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.mybatisorm.Query;
import org.mybatisorm.annotation.SqlCommand;
import org.mybatisorm.sql.builder.DynamicSqlBuilder;

@SqlCommand(SqlCommandType.SELECT)
public class CountSqlSource extends DynamicSqlBuilder {
	
	public CountSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser,clazz);
		staticSql = "SELECT COUNT(*) FROM " + handler.getName();
	}

	public BoundSql getBoundSql(Object parameter) {
		String where = (parameter instanceof Query) ? ((Query)parameter).getCondition() :
			handler.getNotNullColumnEqualFieldAnd(parameter);
		return makeWhere(where,parameter);
	}

	public Class<?> getResultType() {
		return Integer.class;
	}
	
	public List<ResultMapping> getResultMappingList() {
		return Collections.emptyList();
	}

}
