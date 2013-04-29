/* Copyright 2012 The MyBatisORM Team
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
package org.mybatisorm;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.mybatisorm.annotation.handler.SqlCommandAnnotation;
import org.mybatisorm.exception.InvalidSqlSourceException;
import org.mybatisorm.exception.MyBatisOrmException;
import org.mybatisorm.sql.builder.SqlBuilder;
import org.mybatisorm.sql.source.ValueGenerator;
import org.mybatisorm.sql.source.ValueGeneratorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Donghyeok Kang(wolfkang@gmail.com)
 */
public class EntityManager extends SqlSessionDaoSupport implements InitializingBean {

	private static Logger logger = Logger.getLogger(EntityManager.class);

	private static final String SOURCE_COUNT = "Count";
	private static final String SOURCE_DELETE = "Delete";
	private static final String SOURCE_INCREMENT = "Increment";
	private static final String SOURCE_LIST = "List";
	private static final String SOURCE_UPDATE = "Update";
	private static final String SOURCE_LOAD = "Load";
	private static final String SOURCE_PAGE = "Page";
	private static final String SOURCE_INSERT = "Insert";
	private static final String SOURCE_ROWNUM = "Rownum";
	private static final String SOURCE_LIMIT = "Limit";
	private static final String SOURCE_GENERATOR = "Generator";

	private Configuration configuration;
	private SqlSession sqlSession;
	private SqlSourceBuilder sqlSourceBuilder;
	private String sourceType = "mysql";  // 기본 소스타입은 "mysql"
	private Map<String, Class<?>> sqlSourceClassMap;
	private ValueGenerator valueGenerator;

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public <T> T getDao(Class<T> clazz) {
		return getSqlSession().getMapper(clazz);
	}

	protected void initDao() throws Exception {
		sqlSession = getSqlSession();
		configuration = sqlSession.getConfiguration();
		sqlSourceBuilder = new SqlSourceBuilder(configuration);
		sqlSourceClassMap = new HashMap<String, Class<?>>();

		ResourceBundle bundle = new PropertyResourceBundle(this.getClass().getClassLoader().getResourceAsStream("sql-sources.properties"));
		// ClassLoader.getSystemResourceAsStream("SqlSources.properties"));
		for (String source : bundle.getString(sourceType + ".sqlsources").split(",")) {
			sqlSourceClassMap.put(source, Class.forName(bundle.getString(sourceType + "." + source)));
		}
		valueGenerator = (ValueGeneratorImpl) getSourceTypeClass(SOURCE_GENERATOR).newInstance();
		valueGenerator.setConfiguration(configuration);
	}

	private Class<?> getSourceTypeClass(String className) {
		return sqlSourceClassMap.get(className);
	}

	/**
	 * condition으로 where 조건을 생성하여 매핑 테이블에서 rownum을 구한다.
	 * 
	 * @param parameter
	 * @param condition
	 * @param orderBy
	 * @return rownum 값
	 */
	public int rownum(Object parameter, Condition condition, String orderBy) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(SOURCE_ROWNUM, clazz);
		Object result = sqlSession.selectOne(statementName, new Query(parameter, condition, orderBy));
		return (result == null) ? 0 : (Integer) result;
	}

	/**
	 * object의 null이 아닌 필드의 값을 증가시킨다. 값이 음수이면 감소된다.
	 * 
	 * @param parameter
	 */
	public void increment(Object parameter) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(SOURCE_INCREMENT, clazz);
		sqlSession.update(statementName, parameter);
	}

	/**
	 * object에 null 값을 제외하고 매핑 테이블에 insert 한다.
	 * 
	 * @param parameter
	 */
	public void insert(Object parameter) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(SOURCE_INSERT, clazz);
		sqlSession.insert(statementName, parameter);
	}

	/**
	 * object에 primaryKey가 true인 필드값으로 조건절을 생헝하여, null 값인 필드를 제외하고 매핑 테이블에 update 한다.
	 * 
	 * @param parameter
	 */
	public void update(Object parameter) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(SOURCE_UPDATE, clazz);
		sqlSession.update(statementName, parameter);
	}

	/**
	 * object의 null이 아닌 field에 대해 AND 조건으로 where 조건을 생성하여 매핑 테이블을 delete 한다.
	 * 
	 * @param parameter
	 */
	public void delete(Object parameter) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(SOURCE_DELETE, clazz);
		sqlSession.delete(statementName, parameter);
	}

	/**
	 * condition으로 where 조건을 생성하여 매핑 테이블을 delete 한다.
	 * 
	 * @param parameter
	 * @param condition
	 * @since 0.2.2
	 */
	public void delete(Object parameter, String condition) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(SOURCE_DELETE, clazz);
		sqlSession.delete(statementName, new Query(parameter, condition, null));
	}

	/**
	 * condition으로 where 조건을 생성하여 매핑 테이블을 delete 한다.
	 * 
	 * @param parameter
	 * @param condition
	 * @since 0.2.2
	 */
	public void delete(Object parameter, Condition condition) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(SOURCE_DELETE, clazz);
		sqlSession.delete(statementName, new Query(parameter, condition, null));
	}

	/**
	 * object의 null이 아닌 field에 대해 AND 조건으로 where 조건을 생성하여 매핑 테이블에서 1 row만 select한 다음,
	 * parameter 객체에 값을 setting 한다.
	 * 
	 * @param parameter
	 * @return select 결과가 존재하면 true, 없으면 false
	 */
	public boolean load(Object parameter) {
		Object result = get(parameter);
		if (result != null) {
			BeanUtils.copyProperties(result, parameter);
			return true;
		}
		return false;
	}

	/**
	 * object의 null이 아닌 field에 대해 AND 조건으로 where 조건을 생성하여 매핑 테이블에서 1 row만 select한다.
	 * 
	 * @param parameter
	 * @return select 결과 object
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(T parameter) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(SOURCE_LOAD, clazz);
		T result = (T) sqlSession.selectOne(statementName, parameter);
		return result;
	}

	/**
	 * object의 null이 아닌 field에 대해 AND 조건으로 where 조건을 생성하여 매핑 테이블에서 select count(*) 한다.
	 * 
	 * @param parameter
	 * @return select count(*) 결과값
	 */
	public int count(Object parameter) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(SOURCE_COUNT, clazz);
		return (Integer) sqlSession.selectOne(statementName, parameter);
	}

	/**
	 * condition으로 where 조건을 생성하여 매핑 테이블에서 select count(*) 한다.
	 * 
	 * @param parameter
	 * @param condition
	 * @return select count(*) 결과값
	 */
	public int count(Object parameter, String condition) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(SOURCE_COUNT, clazz);
		return (Integer) sqlSession.selectOne(statementName, new Query(parameter, condition, null));
	}

	/**
	 * object의 null이 아닌 field에 대해 AND 조건으로 where 조건을 생성하여 매핑 테이블에서 select count(*) 한다.
	 * 
	 * @param parameter
	 * @return select count(*) 결과값
	 */
	public int count(Object parameter, Condition condition) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(SOURCE_COUNT, clazz);
		return (Integer) sqlSession.selectOne(statementName, new Query(parameter, condition, null));
	}

	/**
	 * object의 null이 아닌 field에 대해 AND 조건으로 where 조건을 생성하여 매핑 테이블을 select하여,
	 * object와 동일한 타입의 객체를 java.util.List 에 담아 리턴한다.
	 * 예)
	 * Comment comment = new Comment();
	 * comment.setPlusCount(1);
	 * comment.setCommentKey(10);
	 * entityManager.list(comment,"register_date ASC");
	 * 실행되는 SQL
	 * SELECT * FROM plus_count = 1 AND comment_key = 10
	 * 
	 * @param parameter
	 * @return select 결과 리스트
	 */
	public <T> List<T> list(T parameter) {
		return list(new Query(parameter));
	}

	/**
	 * object의 null이 아닌 field에 대해 AND 조건으로 where 조건을 생성하여 매핑 테이블을 select하여,
	 * orderBy에 의해 정렬하고,
	 * object와 동일한 타입의 객체를 java.util.List 에 담아 리턴한다.
	 * 예)
	 * Comment comment = new Comment();
	 * comment.setPlusCount(1);
	 * comment.setCommentKey(10);
	 * entityManager.list(comment,"register_date ASC");
	 * 실행되는 SQL
	 * SELECT * FROM plus_count = 1 AND comment_key = 10 ORDER BY register_date ASC
	 * 
	 * @param parameter
	 * @param orderBy
	 * @return
	 */
	public <T> List<T> list(T parameter, String orderBy) {
		return list(new Query(parameter, orderBy));
	}

	public <T> List<T> list(T parameter, String orderBy, int rows) {
		return list(parameter, null, orderBy, 1, rows);
	}

	/**
	 * condition으로 where 조건을 생성하여 매핑 테이블을 select한 다음,
	 * object와 동일한 타입의 객체를 java.util.List 에 담아 리턴한다.
	 * 예)
	 * Comment comment = new Comment();
	 * comment.setPlusCount(1);
	 * entityManager.list(comment,"plus_count >= #{plusCount}",null);
	 * 실행되는 SQL
	 * SELECT * FROM plus_count >= 1
	 * 
	 * @param parameter
	 * @param condition
	 * @param orderBy
	 * @return select 결과 List 객체
	 */
	public <T> List<T> list(T parameter, String condition, String orderBy) {
		return list(new Query(parameter, condition, orderBy));
	}

	/**
	 * @param parameter
	 * @param condition
	 * @param orderBy
	 * @return
	 */
	public <T> List<T> list(T parameter, Condition condition, String orderBy) {
		return list(new Query(parameter, condition, orderBy));
	}

	/**
	 * @param parameter
	 * @param condition
	 * @return
	 */
	public <T> List<T> list(T parameter, Condition condition) {
		return list(new Query(parameter, condition, null));
	}

	/**
	 * @param parameter
	 * @param condition
	 * @param orderBy
	 * @return
	 */
	private <T> List<T> list(Query query) {
		String statementName = addStatement(SOURCE_LIST, query.getParameter().getClass());
		return sqlSession.selectList(statementName, query);
	}

	/**
	 * @param parameter
	 * @param condition
	 * @param orderBy
	 * @param rows
	 * @return
	 */
	public <T> List<T> list(T parameter, Condition condition, String orderBy, int rows) {
		return list(parameter, condition, orderBy, 1, rows);
	}

	/**
	 * @param parameter
	 * @param condition
	 * @param orderBy
	 * @param start
	 * @param rows
	 * @return
	 */
	public <T> List<T> list(T parameter, Condition condition, String orderBy, int start, int rows) {
		if (orderBy == null) throw new MyBatisOrmException("The orderBy cannot be null.");

		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(SOURCE_LIMIT, clazz);
		Query query = new Query(parameter, condition, orderBy);
		query.setStart(start);
		query.setRows(rows);
		List<T> list = sqlSession.selectList(statementName, query);
		return list;
	}

	/**
	 * object의 null이 아닌 field에 대해 AND 조건으로 where 조건을 생성하여 매핑 테이블에서 count() 한 다음,
	 * 데이터를 select 하여, java.util.List에 담고, 이를 Page 객체에 저장하여 리턴한다.
	 * 
	 * @param parameter 파라미터 객체
	 * @param pageNumber 페이지 번호
	 * @param rows 페이지 당 아이템 개수
	 * @return select 결과 Page 객체
	 */
	public <T> Page<T> page(T parameter, int pageNumber, int rows) {
		return page(parameter, null, pageNumber, rows);
	}

	/**
	 * object의 null이 아닌 field에 대해 AND 조건으로 where 조건을 생성하여 매핑 테이블에서 count() 한 다음,
	 * 데이터를 select 하여, java.util.List에 담고, 이를 Page 객체에 저장하여 리턴한다.
	 * 
	 * @param parameter 파라미터 객체
	 * @param orderBy 정렬 방식
	 * @param pageNumber 페이지 번호
	 * @param rows 페이지 당 아이템 개수
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Page<T> page(T parameter, String orderBy, int pageNumber, int rows) {
		int count = count(parameter);
		Page<T> page = new Page<T>(pageNumber, rows, count);
		if (count > (pageNumber - 1) * rows) {
			Class<?> clazz = parameter.getClass();
			String statementName = addStatement(SOURCE_PAGE, clazz);
			page.setList((List<T>) sqlSession.selectList(statementName, new Query(parameter, orderBy, pageNumber, rows)));
		}
		return page;
	}

	/**
	 * condition으로 where 조건을 생성하여 매핑 테이블에서 count() 한 다음,
	 * 데이터를 select 하여, java.util.List에 담고, 이를 Page 객체에 저장하여 리턴한다.
	 * object의 field 값들은 where 조건에 사용되지 않는다.
	 * 
	 * @param clazz 매핑 클래스
	 * @param condition 조건
	 * @param orderBy 정렬 방식
	 * @param pageNumber 페이지 번호
	 * @param rows 페이지 당 아이템 개수
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Page<T> page(T parameter, String condition, String orderBy, int pageNumber, int rows) {
		int count = count(parameter, condition);
		Page<T> page = new Page<T>(pageNumber, rows, count);
		if (count > (pageNumber - 1) * rows) {
			Class<?> clazz = parameter.getClass();
			String statementName = addStatement(SOURCE_PAGE, clazz);
			page.setList((List<T>) sqlSession.selectList(statementName, new Query(parameter, condition, orderBy, pageNumber, rows)));
		}
		return page;
	}

	/**
	 * condition으로 where 조건을 생성하여 매핑 테이블에서 count() 한 다음,
	 * 데이터를 select 하여, java.util.List에 담고, 이를 Page 객체에 저장하여 리턴한다.
	 * object의 field 값들은 where 조건에 사용되지 않는다.
	 * 
	 * @param clazz 매핑 클래스
	 * @param condition 조건
	 * @param orderBy 정렬 방식
	 * @param pageNumber 페이지 번호
	 * @param rows 페이지 당 아이템 개수
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Page<T> page(T parameter, Condition condition, String orderBy, int pageNumber, int rows) {
		int count = count(parameter, condition);
		Page<T> page = new Page<T>(pageNumber, rows, count);
		if (count > (pageNumber - 1) * rows) {
			Class<?> clazz = parameter.getClass();
			String statementName = addStatement(SOURCE_PAGE, clazz);
			page.setList((List<T>) sqlSession.selectList(statementName, new Query(parameter, condition, orderBy, pageNumber, rows)));
		}
		return page;
	}

	private synchronized String addStatement(String sourceName, Class<?> type) {
		Class<?> sqlSourceClass = getSourceTypeClass(sourceName);
		String id = "_" + sqlSourceClass.getSimpleName() + type.getSimpleName();
		if (!configuration.hasStatement(id)) {
			if (logger.isDebugEnabled()) logger.debug("add a mapped statement, " + id);
			Constructor<?> constructor = null;
			try {
				constructor = sqlSourceClass.getDeclaredConstructor(SqlSourceBuilder.class, Class.class);
			} catch (Exception e) {
				throw new InvalidSqlSourceException(e);
			}
			SqlBuilder sqlBuilder = (SqlBuilder) BeanUtils.instantiateClass(constructor, sqlSourceBuilder, type);
			SqlCommandType sqlType = SqlCommandAnnotation.getSqlCommand(sqlSourceClass).value();
			MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, id, sqlBuilder, sqlType);
			statementBuilder.timeout(configuration.getDefaultStatementTimeout());
			Class<?> resultType = sqlBuilder.getResultType();
			if (resultType != null) {
				List<ResultMap> resultMaps = new ArrayList<ResultMap>();
				ResultMap.Builder resultMapBuilder = new ResultMap.Builder(configuration, statementBuilder.id() + "-Inline", resultType, sqlBuilder.getResultMappingList());
				resultMaps.add(resultMapBuilder.build());
				// if (logger.isDebugEnabled()) logger.debug("add a resultMap [" + statementBuilder.id() + "-Inline] ==> [" + id + "]");
				statementBuilder.resultMaps(resultMaps);
			}
			if (SqlCommandType.INSERT.equals(sqlType)) {
				valueGenerator.generate(statementBuilder, id, type);
			}
			MappedStatement statement = statementBuilder.build();
			configuration.addMappedStatement(statement);
		}
		return id;
	}
}
