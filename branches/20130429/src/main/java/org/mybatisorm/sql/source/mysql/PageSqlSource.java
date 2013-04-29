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
package org.mybatisorm.sql.source.mysql;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.log4j.Logger;
import org.mybatisorm.Query;
import org.mybatisorm.sql.source.AbstractSelectSqlSource;

public class PageSqlSource extends AbstractSelectSqlSource {

	private static Logger logger = Logger.getLogger(PageSqlSource.class);
	
	public PageSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser, clazz);
	}

	public BoundSql getBoundSql(final Object queryParam) {
		Query query = (Query)queryParam;
		String where = null;

		StringBuilder sb = new StringBuilder(staticSql);
		where = (query.getCondition() != null) ? query.getCondition() :
			handler.getNotNullColumnEqualFieldAnd(query.getParameter());
		if (where.length() > 0) {
			sb.append(" WHERE ").append(where);
		}
		if (query.hasOrderBy())
			sb.append(" ORDER BY ").append(query.buildOrderBy());

		query.setStart((query.getPageNumber()-1)*query.getRows());
		sb.append(" LIMIT #{start}, #{rows}");

		return getBoundSql(sb.toString(),queryParam);
	}
}
