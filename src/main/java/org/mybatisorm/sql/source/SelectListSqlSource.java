package org.mybatisorm.sql.source;

import java.util.List;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.mybatisorm.util.AnnotationUtil;
import org.mybatisorm.util.Page;
import org.mybatisorm.util.Search;
import org.mybatisorm.util.StringUtil;

public class SelectListSqlSource extends SelectSqlSource {

	public SelectListSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser, clazz);
	}

	public BoundSql getBoundSql(Object parameterObject) {
		Object parameter = parameterObject;
		String orderQuery = null;
		Page page = null;
		String prefix = "";
		Search search = null;
		if (parameterObject instanceof Search) {
			search = (Search)parameterObject;
			parameter = search.getParameter();
			orderQuery = search.getOrderQuery();
			page = search.getPage();
			prefix = "parameter.";
		}
		StringBuilder sb = new StringBuilder(staticSql);
		List names = AnnotationUtil.getNotNullColumnNames(parameter);
		if (names != null && !names.isEmpty())
			sb.append(" WHERE ").append(StringUtil.join(AnnotationUtil.getNotNullColumnNames(parameter),"%1$s = #{"+prefix+"%1$s}"," AND "));
		if (search != null && search.getSearchfield() != null && search.getKeyword() != null)
			sb.append(sb.indexOf("WHERE") > 0 ? " AND " : " WHERE ").append(search.getSearchfield()).append(" LIKE CONCAT('%',#{keyword},'%') ");
		if (orderQuery != null)
			sb.append(" ORDER BY ").append(orderQuery);
		if (page != null)
			sb.append(" LIMIT ").append(page.getLimitstart()).append(", ").append(page.getLimitend());

		String sql = sb.toString(); 
		return getBoundSql(sql,parameterObject);
	}
}
