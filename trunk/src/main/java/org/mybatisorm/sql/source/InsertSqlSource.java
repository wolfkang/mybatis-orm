package org.mybatisorm.sql.source;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.log4j.Logger;
import org.mybatisorm.annotation.AnnotationUtil;
import org.mybatisorm.annotation.FieldList;
import org.mybatisorm.annotation.SqlCommand;
import org.mybatisorm.sql.builder.DynamicSqlBuilder;

@SqlCommand(SqlCommandType.INSERT)
public class InsertSqlSource extends DynamicSqlBuilder {

	private static Logger logger = Logger.getLogger(InsertSqlSource.class);
	
	public InsertSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser);
		staticSql = "INSERT INTO " + AnnotationUtil.getTableName(clazz) + "(";
	}

	public BoundSql getBoundSql(final Object parameter) {
		FieldList fieldList = AnnotationUtil.getNotNullFieldList(parameter);
		StringBuilder sb = new StringBuilder(staticSql);
		sb.append(AnnotationUtil.join(fieldList.getColumnNames(), ",")).append(") VALUES (")
				.append(AnnotationUtil.join(fieldList.getFieldNames(), "#{%1$s}", ",")).append(")");
		//logger.debug(sql);
		
		return getBoundSql(sb.toString(),parameter);
	}
}
