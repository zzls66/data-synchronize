package com.shbaoyuantech.beans;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import org.bson.types.ObjectId;

@MappingBean(table = "by_staff_position", collection = "dim_staff_positions", isCommon = true)
public class StaffPositionBean extends BaseBean {

    @MappingColumn(column = "staff_id", field = "staff_id", refBean = StaffBean.class)
    private ObjectId staffId;

    @MappingColumn(column = "position_id", field = "position_id", refBean = PositionBean.class)
    private ObjectId positionId;

    @MappingColumn(column = "duty_id", field = "duty_id", refBean = DutyBean.class)
    private ObjectId dutyId;

    @MappingColumn(column = "store_id", field = "store_id", refBean = StoreBean.class)
    private ObjectId storeId;

    @MappingColumn(column = "department_id", field = "department_id", refBean = DepartmentBean.class)
    private ObjectId departmentId;
}
