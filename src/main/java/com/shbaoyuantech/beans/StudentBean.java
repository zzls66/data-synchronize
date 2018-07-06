package com.shbaoyuantech.beans;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;

@MappingBean(table = "by_student", collection = "dim_students")
public class StudentBean extends BaseBean {

    @MappingColumn(column = "name", field = "name")
    private String name;

    @MappingColumn(column = "mobile", field = "mobile")
    private String mobile;

    @MappingColumn(column = "gender", field = "gender")
    private Integer gender;

    @MappingColumn(column = "age", field = "age")
    private Integer age;
}
