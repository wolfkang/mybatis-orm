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

import java.util.Collections;
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
public class RownumSqlSource extends DynamicSqlBuilder {
/*
	SELECT rnum FROM (
		SELECT message_key, row_number() OVER (ORDER BY message_key desc) rnum FROM
		NBBS_message
	    WHERE board_key = 123
	)
	WHERE message_key = 103
*/
	private static Logger logger = Logger.getLogger(RownumSqlSource.class);
	
	public RownumSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser,clazz);
		staticSql = "SELECT rnum FROM " +
				"(SELECT " + handler.getPrimaryKeyComma() + ", ROW_NUMBER() OVER (ORDER BY %s) rnum " +
				" FROM " + handler.getName() + " WHERE %s ) WHERE %s";
	}

	public BoundSql getBoundSql(Object queryParam) {
		Query query = (Query)queryParam;
		
		if (!query.hasCondition())
			throw new MyBatisOrmException("The condition object is required.");
		
		if (!query.hasOrderBy())
			throw new MyBatisOrmException("The 'order by' clause is required.");
		
		String sql = String.format(staticSql,
				query.buildOrderBy(),
				query.getCondition(),
				handler.getNotNullPrimaryKeyEqualFieldAnd(query.getParameter(),Query.PARAMETER_PREFIX));
		return getBoundSql(sql,queryParam);
	}
	
	public Class<?> getResultType() {
		return Integer.class;
	}
	
	public List<ResultMapping> getResultMappingList() {
		return Collections.emptyList();
	}
}
