package com.shbaoyuantech.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MappingColumn {
    String column();
    String field();
    String refCollection() default "";
    boolean isRedundant() default false;
}
