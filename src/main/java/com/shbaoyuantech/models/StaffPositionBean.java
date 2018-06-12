package com.shbaoyuantech.models;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import org.bson.types.ObjectId;

@MappingBean(table = "by_staff_position", collection = "dim_staff_positions")
public class StaffPositionBean extends BaseBean{

    @MappingColumn(column = "staff_id", field = "staff_id", refCollection = "dim_staff")
    private ObjectId staffId;

    @MappingColumn(column = "position_id", field = "position_id", refCollection = "dim_position")
    private ObjectId positionId;

    @MappingColumn(column = "duty_id", field = "duty_id", refCollection = "dim_duty")
    private ObjectId dutyId;

    //TODO
    private Integer roleType;

    @MappingColumn(column = "store_id", field = "store_id", refCollection = "dim_stores")
    private ObjectId storeId;

    @MappingColumn(column = "department_id", field = "department_id", refCollection = "dim_departments")
    private ObjectId departmentId;

    public ObjectId getStaffId() {
        return staffId;
    }

    public void setStaffId(ObjectId staffId) {
        this.staffId = staffId;
    }

    public ObjectId getPositionId() {
        return positionId;
    }

    public void setPositionId(ObjectId positionId) {
        this.positionId = positionId;
    }

    public ObjectId getDutyId() {
        return dutyId;
    }

    public void setDutyId(ObjectId dutyId) {
        this.dutyId = dutyId;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public ObjectId getStoreId() {
        return storeId;
    }

    public void setStoreId(ObjectId storeId) {
        this.storeId = storeId;
    }

    public ObjectId getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(ObjectId departmentId) {
        this.departmentId = departmentId;
    }
}
