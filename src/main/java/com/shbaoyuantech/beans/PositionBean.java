package com.shbaoyuantech.beans;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import org.bson.types.ObjectId;

@MappingBean(table = "by_position", collection = "dim_position")
public class PositionBean extends BaseBean {

    @MappingColumn(column = "name", field = "name")
    private String name;

    @MappingColumn(column = "duty_id", field = "duty_id", refBean = DutyBean.class)
    private ObjectId dutyId;
}
