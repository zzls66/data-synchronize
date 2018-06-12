package com.shbaoyuantech.models;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import org.bson.types.ObjectId;

@MappingBean(table = "by_position", collection = "dim_position")
public class PositionBean extends BaseBean{

    @MappingColumn(column = "name", field = "name")
    private String name;

    @MappingColumn(column = "duty_id", field = "duty_id", refCollection = "dim_duty")
    private ObjectId dutyId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectId getDutyId() {
        return dutyId;
    }

    public void setDutyId(ObjectId dutyId) {
        this.dutyId = dutyId;
    }
}
