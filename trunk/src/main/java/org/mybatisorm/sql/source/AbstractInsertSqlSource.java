package org.mybatisorm.sql.source;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.log4j.Logger;
import org.mybatisorm.annotation.SqlCommand;
import org.mybatisorm.annotation.handler.ColumnHandler;
import org.mybatisorm.annotation.handler.FieldList;
import org.mybatisorm.annotation.handler.TableHandler;
import org.mybatisorm.sql.builder.DynamicSqlBuilder;

@SqlCommand(SqlCommandType.INSERT)
public abstract class AbstractInsertSqlSource extends DynamicSqlBuilder {

	private static Logger logger = Logger.getLogger(AbstractInsertSqlSource.class);
	
	public AbstractInsertSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser);
		staticSql = "INSERT INTO " + TableHandler.getName(clazz) + "(";
	}

	public BoundSql getBoundSql(final Object parameter, FieldList fieldList) {
		StringBuilder sb = new StringBuilder(staticSql);
		sb.append(ColumnHandler.join(fieldList.getColumnNames(), ",")).append(") VALUES (")
				.append(ColumnHandler.join(fieldList.getFieldNames(), "#{%1$s}", ",")).append(")");
		//logger.debug(sql);
		
		return getBoundSql(sb.toString(),parameter);
	}
}
