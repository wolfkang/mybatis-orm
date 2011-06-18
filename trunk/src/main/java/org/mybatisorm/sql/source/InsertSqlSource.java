package org.mybatisorm.sql.source;

import java.util.List;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.log4j.Logger;
import org.mybatisorm.sql.builder.StaticSqlBuilder;
import org.mybatisorm.util.AnnotationUtil;
import org.mybatisorm.util.StringUtil;

public class InsertSqlSource extends StaticSqlBuilder {

	private static Logger logger = Logger.getLogger(InsertSqlSource.class);
	
	public InsertSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super();
		List<String> columnNames = AnnotationUtil.getNonAutoIncrementColumnNames(clazz);
		String sql = String.format("INSERT INTO %1$s ( %2$s ) VALUES ( %3$s ) ",
				AnnotationUtil.getTableName(clazz),
				StringUtil.join(columnNames, ","),
				StringUtil.join(columnNames, "#{%1$s}",","));
		logger.debug(sql);
		
		parse(sqlSourceParser, sql, clazz);
	}

}
