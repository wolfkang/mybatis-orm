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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.session.Configuration;


public abstract class ValueGeneratorImpl implements ValueGenerator {
	
	private Configuration configuration;

	protected abstract GeneratedField getGeneratedField(Class<?> clazz);

	protected abstract KeyGenerator keyGenerator(Builder builder, String parentId, Class<?> clazz);

	public abstract boolean getExecuteBefore();
	
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	
	public void generate(Builder builder, String parentId, Class<?> clazz) {
		KeyGenerator keygen = keyGenerator(builder,parentId,clazz);
		builder.keyGenerator(keygen);
	}

	protected KeyGenerator newSelectKeyGenerator(GeneratedField generated, String parentId) {
		SqlSource sqlSource = new StaticSqlSource(configuration,generated.getSql(),null); 
		String id = parentId + SelectKeyGenerator.SELECT_KEY_SUFFIX;
		Class<?> resultTypeClass = generated.getField().getType();
		MappedStatement.Builder builder = new MappedStatement.Builder(configuration,id,sqlSource,SqlCommandType.SELECT);
		builder.timeout(configuration.getDefaultStatementTimeout());
		
		List<ResultMap> resultMaps = new ArrayList<ResultMap>();
		ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(
				configuration,
				builder.id() + "-Inline",
				resultTypeClass,
				new ArrayList<ResultMapping>());
		resultMaps.add(inlineResultMapBuilder.build());
		builder.resultMaps(resultMaps);
		builder.resultSetType(null);
		builder.keyGenerator(new NoKeyGenerator());
		builder.keyProperty(generated.getField().getName());
		
		MappedStatement statement = builder.build();
		configuration.addMappedStatement(statement);

		MappedStatement keyStatement = configuration.getMappedStatement(id);
		KeyGenerator keyGen = new SelectKeyGenerator(keyStatement, getExecuteBefore());
		configuration.addKeyGenerator(id,keyGen);
		return keyGen;
	}

	protected KeyGenerator newJdbc3KeyGenerator(Builder builder, GeneratedField generated) {
		builder.keyProperty(generated.getField().getName());
		return new Jdbc3KeyGenerator();
	}

	public static class GeneratedField {
		private Field field;
		private String sql;
		public GeneratedField(Field field) {
			super();
			this.setField(field);
		}
		public GeneratedField(Field field, String sql) {
			super();
			this.setField(field);
			this.setSql(sql);
		}
		public Field getField() {
			return field;
		}
		public void setField(Field field) {
			this.field = field;
		}
		public String getSql() {
			return sql;
		}
		public void setSql(String sql) {
			this.sql = sql;
		}
	}
}
