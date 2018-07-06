package com.shbaoyuantech.beans;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;

@MappingBean(table = "by_grade", collection = "dim_grades")
public class GradeBean extends BaseBean {

    @MappingColumn(column = "name", field = "name")
    private String name;
}
