package org.mybatisorm.sql.source.oracle;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.log4j.Logger;
import org.mybatisorm.Query;
import org.mybatisorm.annotation.AnnotationUtil;
import org.mybatisorm.annotation.SqlCommand;
import org.mybatisorm.sql.builder.DynamicSqlBuilder;

@SqlCommand(SqlCommandType.SELECT)
public class PageSqlSource extends DynamicSqlBuilder {

	private static Logger logger = Logger.getLogger(PageSqlSource.class);
	
	public PageSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser);
		staticSql = "SELECT " + AnnotationUtil.getColumnAsFieldComma(clazz) + " FROM " +
				"(SELECT " + AnnotationUtil.getColumnComma(clazz) + ", ROWNUM rnum FROM " +
				AnnotationUtil.getTableName(clazz) + " WHERE ";
	}

	public BoundSql getBoundSql(Object queryParam) {
		Query query = (Query)queryParam;
		String where = null;

		StringBuilder sb = new StringBuilder(staticSql);
		where = query.hasCondition() ? query.getCondition() : query.getNotNullColumnEqualFieldAnd();
		if (where.length() > 0) {
			sb.append("(").append(where).append(") AND ");
		}
		sb.append("ROWNUM <= #{end} ");
		
		if (query.getOrderBy() != null)
			sb.append(" ORDER BY ").append(query.getOrderBy());

		sb.append(") WHERE rnum >= #{start}");
		
		query.setStart((query.getPageNumber()-1)*query.getRows()+1);
		query.setEnd(query.getPageNumber()*query.getRows());

		return getBoundSql(sb.toString(),queryParam);
	}
}
