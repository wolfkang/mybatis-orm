package org.mybatisorm.sql.source.mysql;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.log4j.Logger;
import org.mybatisorm.Query;
import org.mybatisorm.sql.source.AbstractSelectSqlSource;

public class PageSqlSource extends AbstractSelectSqlSource {

	private static Logger logger = Logger.getLogger(PageSqlSource.class);
	
	public PageSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser, clazz);
	}

	public BoundSql getBoundSql(final Object queryParam) {
		Query query = (Query)queryParam;
		String where = null;

		StringBuilder sb = new StringBuilder(staticSql);
		where = (query.getCondition() != null) ? query.getCondition() : query.getNotNullColumnEqualFieldAnd();
		if (where.length() > 0) {
			sb.append(" WHERE ").append(where);
		}
		if (query.getOrderBy() != null)
			sb.append(" ORDER BY ").append(query.getOrderBy());

		query.setStart((query.getPageNumber()-1)*query.getRows());
		sb.append(" LIMIT #{start}, #{rows}");

		return getBoundSql(sb.toString(),queryParam);
	}
}
