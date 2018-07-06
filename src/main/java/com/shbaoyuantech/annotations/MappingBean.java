package com.shbaoyuantech.annotations;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface MappingBean {
    String table();
    String collection();
    boolean isCommon() default false;
}
