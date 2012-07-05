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
package org.mybatisorm.sql.source;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.log4j.Logger;
import org.mybatisorm.Query;

public class ListSqlSource extends AbstractSelectSqlSource {

	private static Logger logger = Logger.getLogger(ListSqlSource.class);
	
	public ListSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser, clazz);
	}

	public BoundSql getBoundSql(final Object parameter) {
		String where = null;
		StringBuilder sb = new StringBuilder(staticSql);
		if (parameter instanceof Query) {
			Query query = (Query)parameter;
			where = query.hasCondition() ? query.getCondition() : query.getNotNullColumnEqualFieldAnd(handler);
			if (where != null && where.length() > 0) {
				sb.append(" WHERE ").append(where);
			}
			if (query.makeOrderBy() != null)
				sb.append(" ORDER BY ").append(query.makeOrderBy());
		} else {
			where = handler.getNotNullColumnEqualFieldAnd(parameter);
			if (where.length() > 0) {
				sb.append(" WHERE ").append(where);
			}
		}
		return getBoundSql(sb.toString(),parameter);
	}
}
