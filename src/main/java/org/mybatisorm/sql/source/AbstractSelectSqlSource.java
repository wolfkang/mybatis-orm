package org.mybatisorm.sql.source;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.SqlCommandType;
import org.mybatisorm.annotation.SqlCommand;
import org.mybatisorm.annotation.handler.ColumnHandler;
import org.mybatisorm.annotation.handler.TableHandler;
import org.mybatisorm.sql.builder.DynamicSqlBuilder;

@SqlCommand(value=SqlCommandType.SELECT, resultMapping=true)
public abstract class AbstractSelectSqlSource extends DynamicSqlBuilder {

	public AbstractSelectSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser);
		staticSql = "SELECT " + ColumnHandler.getColumnComma(clazz) +
				" FROM " + TableHandler.getName(clazz); 
	}
}
