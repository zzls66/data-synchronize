package com.shbaoyuantech.models;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import org.bson.types.ObjectId;

@MappingBean(table = "by_source", collection = "dim_sources")
public class SourceBean extends BaseBean{

    @MappingColumn(column = "name", field = "name")
    private String name;

    @MappingColumn(column = "level", field = "level")
    private Integer level;

    @MappingColumn(column = "fid_id", field = "parent_id", refCollection = "dim_sources")
    private ObjectId parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ObjectId getParentId() {
        return parentId;
    }

    public void setParentId(ObjectId parentId) {
        this.parentId = parentId;
    }
}
