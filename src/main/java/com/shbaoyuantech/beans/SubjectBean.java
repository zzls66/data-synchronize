package com.shbaoyuantech.beans;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;

@MappingBean(table = "by_subject", collection = "dim_subjects")
public class SubjectBean extends BaseBean {

    @MappingColumn(column = "name", field = "name")
    private String name;
}
