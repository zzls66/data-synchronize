package com.shbaoyuantech.beans;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;

@MappingBean(table = "by_department", collection = "dim_departments")
public class DepartmentBean extends BaseBean {

    @MappingColumn(column = "name", field = "name")
    private String name;

    @MappingColumn(column = "city_code", field = "cityCode")
    private String cityCode;
}
