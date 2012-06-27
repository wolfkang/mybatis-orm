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
package org.mybatisorm.sql.builder;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.log4j.Logger;

public abstract class DynamicSqlBuilder extends SqlBuilder {

	private static Logger logger = Logger.getLogger(DynamicSqlBuilder.class);

	protected String staticSql;

	public DynamicSqlBuilder(SqlSourceBuilder sqlSourceParser, Class<?> targetClass) {
		super(sqlSourceParser,targetClass);
	}

	public abstract BoundSql getBoundSql(Object parameterObject);

	protected BoundSql getBoundSql(String sql, Object parameterObject) {
		logger.debug(sql);
		return getSqlSourceParser().parse(sql, parameterObject.getClass()).getBoundSql(parameterObject);
	}

	protected BoundSql makeWhere(String where, Object parameter) {
		return getBoundSql(
				where != null && where.length() > 0 ?
						staticSql + " WHERE " + where : staticSql,
						parameter);
	}
}
