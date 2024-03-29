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

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.mybatisorm.sql.source.AbstractSelectSqlSource;


public class LoadSqlSource extends AbstractSelectSqlSource {
	
	public LoadSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser, clazz);
	}

	public BoundSql getBoundSql(Object parameter) {
		StringBuilder sb = new StringBuilder(staticSql);
		sb.append(" WHERE ");
		String cf = handler.getNotNullColumnEqualFieldAnd(parameter);
		if (cf.length() > 0)
			sb.append(cf).append(" AND ");
		sb.append("ROWNUM = 1");
		System.out.println(sb);

		return getBoundSql(sb.toString(),parameter);
	}
}
