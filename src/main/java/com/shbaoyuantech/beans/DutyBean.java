package com.shbaoyuantech.beans;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;

@MappingBean(table = "by_duty", collection = "dim_duty")
public class DutyBean extends BaseBean {

    @MappingColumn(column = "name", field = "name")
    private String name;
}
