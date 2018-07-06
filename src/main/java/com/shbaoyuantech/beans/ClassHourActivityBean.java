package com.shbaoyuantech.beans;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;

@MappingBean(table = "by_class_hour_activity", collection = "dim_class_activity")
public class ClassHourActivityBean extends BaseBean {

    @MappingColumn(column = "name", field = "name")
    private String name;
}
