package org.mybatisorm.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
	String value() default "";
	String name() default "";
	boolean primaryKey() default false;
	boolean autoIncrement() default false;
	String sequence() default "";
}
