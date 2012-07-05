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
package org.mybatisorm.sql.source.oracle;

import java.util.List;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.log4j.Logger;
import org.mybatisorm.Query;
import org.mybatisorm.annotation.SqlCommand;
import org.mybatisorm.sql.builder.DynamicSqlBuilder;

@SqlCommand(SqlCommandType.SELECT)
public class PageSqlSource extends DynamicSqlBuilder {

	private static Logger logger = Logger.getLogger(PageSqlSource.class);
	
	public PageSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser,clazz);
		staticSql = "SELECT * FROM " +
				"(SELECT " + handler.getColumnAsFieldComma() + ", ROWNUM rnum FROM " +
				handler.getName() + " WHERE ";
	}

	public BoundSql getBoundSql(Object queryParam) {
		Query query = (Query)queryParam;
		String where = null;

		StringBuilder sb = new StringBuilder(staticSql);
		where = query.hasCondition() ? query.getCondition() : query.getNotNullColumnEqualFieldAnd(handler);
		if (where.length() > 0) {
			sb.append("(").append(where).append(") AND ");
		}
		sb.append("ROWNUM <= #{end} ");
		
		if (query.makeOrderBy() != null)
			sb.append(" ORDER BY ").append(query.makeOrderBy());

		sb.append(") WHERE rnum >= #{start}");
		
		query.setStart((query.getPageNumber()-1)*query.getRows()+1);
		query.setEnd(query.getPageNumber()*query.getRows());

		return getBoundSql(sb.toString(),queryParam);
	}
	
	public List<ResultMapping> getResultMappingList() {
		return handler.getResultMappingList(sqlSourceBuilder.getConfiguration());
	}
	
	public Class<?> getResultType() {
		return handler.getTargetClass();
	}
}
