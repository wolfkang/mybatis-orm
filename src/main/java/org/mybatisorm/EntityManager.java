package org.mybatisorm;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.mybatisorm.sql.source.DeleteSqlSource;
import org.mybatisorm.sql.source.InsertSqlSource;
import org.mybatisorm.sql.source.SelectCountSqlSource;
import org.mybatisorm.sql.source.SelectListSqlSource;
import org.mybatisorm.sql.source.SelectOneSqlSource;
import org.mybatisorm.sql.source.UpdateSqlSource;
import org.mybatisorm.util.PageNavigation;
import org.mybatisorm.util.Search;
import org.springframework.beans.BeanUtils;

public class EntityManager extends SqlSessionDaoSupport {

	private static final String PREFIX_INSERT = "dmInsert";
	private static final String PREFIX_DELETE = "dmDelete";
	private static final String PREFIX_UPDATE = "dmUpdate";
	private static final String PREFIX_LOAD = "dmLoad";
	private static final String PREFIX_LIST = "dmList";
	private static final String PREFIX_COUNT = "dmCount";
	
	private static Logger logger = Logger.getLogger(EntityManager.class);
	
	private Configuration configuration;
	private SqlSession sqlSession;
	private SqlSourceBuilder sqlSourceParser;
	
	public <T>T getDao(Class<T> clazz) {
		return getSqlSession().getMapper(clazz);
	}
	
	protected void initDao() throws Exception {
		this.sqlSession = getSqlSession();
		this.configuration = sqlSession.getConfiguration();
		//this.configuration.buildAllStatements();
		this.sqlSourceParser = new SqlSourceBuilder(configuration);
	}

	public void insert(Object object) {
		Class<?> clazz = object.getClass();
		String statementName = PREFIX_INSERT + clazz.getSimpleName();
		if (!configuration.hasStatement(statementName)) {
		    addMappedStatement(statementName,new InsertSqlSource(sqlSourceParser,clazz),SqlCommandType.INSERT,null);
		}
		getSqlSession().insert(statementName, object);
	}
	
	public void update(Object object) {
		Class<?> clazz = object.getClass();
		String statementName = PREFIX_UPDATE + clazz.getSimpleName();
		if (!configuration.hasStatement(statementName)) {
			addMappedStatement(statementName,new UpdateSqlSource(sqlSourceParser,clazz),SqlCommandType.UPDATE,null);
		}
		getSqlSession().update(statementName, object);
	}
	
	public void delete(Object object) {
		Class<?> clazz = object.getClass();
		String statementName = PREFIX_DELETE + clazz.getSimpleName();
		if (!configuration.hasStatement(statementName)) {
			addMappedStatement(statementName,new DeleteSqlSource(sqlSourceParser,clazz),SqlCommandType.DELETE,null);
		}
		getSqlSession().delete(statementName, object);
	}
	
	public Object load(Object object) {
		Class<?> clazz = object.getClass();
		String statementName = PREFIX_LOAD + clazz.getSimpleName();
		if (!configuration.hasStatement(statementName)) {
			addMappedStatement(statementName,new SelectOneSqlSource(sqlSourceParser,clazz),SqlCommandType.SELECT,clazz);
		}
		Object result = getSqlSession().selectOne(statementName, object);
		if (result != null)
			BeanUtils.copyProperties(result, object);
		return result;
	}
	
	public int count(Object object) {
		Class<?> clazz = object.getClass();
		String statementName = PREFIX_COUNT + clazz.getSimpleName();
		if (!configuration.hasStatement(statementName)) {
			addMappedStatement(statementName,new SelectCountSqlSource(sqlSourceParser,clazz),SqlCommandType.SELECT,Integer.class);
		}
		Object result = getSqlSession().selectOne(statementName, object);
		return (Integer)result;
	}
	
	public List<?> list(Object object) {
		return list(object,null,null);
	}
	
	public List<?> list(Object object, String orderQuery) {
		return list(object,orderQuery,null);
	}
	
	public Object list(Object object, String orderQuery, int articlePerPage) {
		return list(object,orderQuery,new PageNavigation(articlePerPage));
	}
	
	public List<?> list(Object object, String orderQuery, PageNavigation page) {
		Class<?> clazz = object.getClass();
		String statementName = PREFIX_LIST + clazz.getSimpleName();
		if (!configuration.hasStatement(statementName)) {
			addMappedStatement(statementName,new SelectListSqlSource(sqlSourceParser,clazz),SqlCommandType.SELECT,clazz);
		}
		Search search = new Search();
		search.setParameter(object);
		search.setOrderQuery(orderQuery);
		search.setPage(page);
		List list = getSqlSession().selectList(statementName, search);
		if (page != null) {
			page.makelink(list.size());
			list = list.subList(page.getRowstart(), page.getRowend());
		}
			
		return list;
	}
	
	private synchronized void addMappedStatement(String statementName, SqlSource sqlSource, SqlCommandType sqlCommandType, Class<?> resultType) {
		if (!configuration.hasStatement(statementName)) {
			logger.debug("add a mapped statement, "+statementName);
			MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, statementName, sqlSource, sqlCommandType);
			statementBuilder.timeout(configuration.getDefaultStatementTimeout());
			if (resultType != null) {
				List<ResultMap> resultMaps = new ArrayList<ResultMap>();
				ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(
						configuration,
						statementBuilder.id() + "-Inline",
						resultType,
						new ArrayList<ResultMapping>());
				resultMaps.add(inlineResultMapBuilder.build());
				statementBuilder.resultMaps(resultMaps);
			}
	
			MappedStatement statement = statementBuilder.build();
			configuration.addMappedStatement(statement);
		}
	}

}
