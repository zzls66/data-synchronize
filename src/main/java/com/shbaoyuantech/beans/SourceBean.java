package com.shbaoyuantech.beans;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import org.bson.types.ObjectId;

@MappingBean(table = "by_source", collection = "dim_sources", isCommon = true)
public class SourceBean extends BaseBean {

    @MappingColumn(column = "name", field = "name")
    private String name;

    @MappingColumn(column = "level", field = "level")
    private Integer level;

    @MappingColumn(column = "fid_id", field = "parent_id", refBean = SourceBean.class)
    private ObjectId parentId;
}
