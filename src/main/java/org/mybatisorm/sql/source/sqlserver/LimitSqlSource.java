/*
 *    Copyright 2012 The MyBatisORM Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatisorm.sql.source.sqlserver;

import java.util.List;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.log4j.Logger;
import org.mybatisorm.Query;
import org.mybatisorm.annotation.SqlCommand;
import org.mybatisorm.exception.MyBatisOrmException;
import org.mybatisorm.sql.builder.DynamicSqlBuilder;

@SqlCommand(SqlCommandType.SELECT)
public class LimitSqlSource extends DynamicSqlBuilder {

	private static Logger logger = Logger.getLogger(LimitSqlSource.class);
	
	public LimitSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser,clazz);
		staticSql = 
				"SELECT " + handler.getColumnAsFieldComma() + " FROM " +
				handler.getName() + " %s ORDER BY %s " +  // where + order by
				" OFFSET (#{start}-1) ROWS FETCH NEXT #{rows} ROWS ONLY";
	}

	public BoundSql getBoundSql(Object queryParam) {
		Query query = (Query)queryParam;

		if (!query.hasOrderBy())
			throw new MyBatisOrmException("The 'order by' clause is required.");

		String where = "";
		where = query.hasCondition() ? query.getCondition() : query.getNotNullColumnEqualFieldAndVia(handler);
		if (where.length() > 0) {
			where = "WHERE " + where;
		}
		
		return getBoundSql(
				String.format(staticSql, where, query.buildOrderBy()),
				query);
	}

	public List<ResultMapping> getResultMappingList() {
		return handler.getResultMappingList(sqlSourceBuilder.getConfiguration());
	}
	
	public Class<?> getResultType() {
		return handler.getTargetClass();
	}
}
