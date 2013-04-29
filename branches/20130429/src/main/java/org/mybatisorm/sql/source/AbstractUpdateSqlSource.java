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
import org.apache.ibatis.mapping.SqlCommandType;
import org.mybatisorm.annotation.SqlCommand;
import org.mybatisorm.sql.builder.DynamicSqlBuilder;

@SqlCommand(SqlCommandType.UPDATE)
public abstract class AbstractUpdateSqlSource extends DynamicSqlBuilder {

	protected String where;
	
	public AbstractUpdateSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser,clazz);
		staticSql = "UPDATE " + handler.getName() + " SET ";
		where = handler.getPrimaryKeyEqualFieldAnd();
	}
	
	protected BoundSql makeSet(String set, Object parameter) {
		StringBuilder sb = new StringBuilder(staticSql);
		sb.append(set).append(" WHERE ").append(where);
		return getBoundSql(sb.toString(),parameter);
	}
}
