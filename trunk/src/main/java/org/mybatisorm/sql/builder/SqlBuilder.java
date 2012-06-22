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

import java.util.List;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.mybatisorm.annotation.handler.HandlerFactory;
import org.mybatisorm.annotation.handler.TableHandler;

public abstract class SqlBuilder implements SqlSource {

	protected SqlSourceBuilder sqlSourceBuilder;

	protected TableHandler handler;
	
	public SqlBuilder(SqlSourceBuilder sqlSourceBuilder, Class<?> targetClass) {
		this.sqlSourceBuilder = sqlSourceBuilder;
		this.handler = HandlerFactory.getHandler(targetClass);
	}

	public abstract BoundSql getBoundSql(Object parameterObject);

	public SqlSourceBuilder getSqlSourceParser() {
		return sqlSourceBuilder;
	}

	public List<ResultMapping> getResultMappingList() {
		return null;
	}

	public Class<?> getResultType() {
		return null;
	}
}
