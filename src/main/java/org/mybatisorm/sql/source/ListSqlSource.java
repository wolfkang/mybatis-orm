package org.mybatisorm.sql.source;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.log4j.Logger;
import org.mybatisorm.Query;
import org.mybatisorm.annotation.AnnotationUtil;

public class ListSqlSource extends AbstractSelectSqlSource {

	private static Logger logger = Logger.getLogger(ListSqlSource.class);
	
	public ListSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser, clazz);
	}

	public BoundSql getBoundSql(final Object parameter) {
		String where = null;
		StringBuilder sb = new StringBuilder(staticSql);
		if (parameter instanceof Query) {
			Query query = (Query)parameter;
			where = query.hasCondition() ? query.getCondition() : query.getNotNullColumnEqualFieldAnd();
			if (where != null && where.length() > 0) {
				sb.append(" WHERE ").append(where);
			}
			if (query.getOrderBy() != null)
				sb.append(" ORDER BY ").append(query.getOrderBy());
		} else {
			where = AnnotationUtil.getNotNullColumnEqualFieldAnd(parameter);
			if (where.length() > 0) {
				sb.append(" WHERE ").append(where);
			}
		}
		return getBoundSql(sb.toString(),parameter);
	}
}
