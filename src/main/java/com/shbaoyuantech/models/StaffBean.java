package com.shbaoyuantech.models;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;

@MappingBean(table = "by_staff", collection = "dim_staff")
public class StaffBean extends BaseBean{

    @MappingColumn(column = "name", field = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
