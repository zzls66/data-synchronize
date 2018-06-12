package com.shbaoyuantech.models;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;

@MappingBean(table = "by_subject", collection = "dim_subjects")
public class SubjectBean extends BaseBean {

    @MappingColumn(column = "name", field = "name")
    private String name;

    public String getName() {
        Class aClass = this.getClass();
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
