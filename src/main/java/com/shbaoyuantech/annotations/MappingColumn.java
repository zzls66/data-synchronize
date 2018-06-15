package com.shbaoyuantech.annotations;

import com.shbaoyuantech.beans.BaseBean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MappingColumn {
    String column() default "";
    String field();
    Class<? extends BaseBean> refBean() default BaseBean.class;
    String refDateField() default "";
    boolean isSupplemental() default false;
}
