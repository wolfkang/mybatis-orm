package org.mybatisorm.sql.source;

import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.session.Configuration;

public interface ValueGenerator {
	public void generate(Builder builder, String parentId, Class<?> clazz);
	public void setConfiguration(Configuration configuration);
}
