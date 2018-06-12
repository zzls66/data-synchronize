package com.shbaoyuantech.models;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import org.bson.types.ObjectId;

import java.util.Date;

@MappingBean(table = "by_contract", collection = "fact_contracts", isCommonData = false)
public class ContractBean extends BaseBean{

    @MappingColumn(column = "contract_no", field = "contractNo")
    private String contractNo;

    @MappingColumn(column = "store_id", field = "store_id", refCollection = "dim_stores")
    private ObjectId storeId;

    @MappingColumn(column = "actual_amt", field = "totalPrice")
    private Double actualAmt;

    @MappingColumn(column = "lesson_cnt", field = "lessonCnt")
    private Double lessonCnt;

    @MappingColumn(column = "create_time", field = "createTime")
    private Date createTime;

    @MappingColumn(column = "contract_date", field = "contractDate")
    private Date contractDate;

    @MappingColumn(column = "contract_type", field = "contractType")
    private Integer contractType;

    @MappingColumn(column = "status", field = "status")
    private Integer status;

    @MappingColumn(column = "contract_category", field = "contractCategory")
    private Integer contractCategory;

    @MappingColumn(column = "class_hour_activity_id", field = "activity_id", refCollection = "dim_class_activity")
    private ObjectId activityId;

    @MappingColumn(column = "creator_id", field = "creator_id", refCollection = "dim_staff")
    private ObjectId creatorId;

    @MappingColumn(column = "owner_id", field = "owner_id", refCollection = "dim_staff")
    private ObjectId ownerId;

    @MappingColumn(column = "lead_id", field = "lead_id", refCollection = "fact_leads")
    private ObjectId leadId;

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public ObjectId getStoreId() {
        return storeId;
    }

    public void setStoreId(ObjectId storeId) {
        this.storeId = storeId;
    }

    public Double getActualAmt() {
        return actualAmt;
    }

    public void setActualAmt(Double actualAmt) {
        this.actualAmt = actualAmt;
    }

    public Double getLessonCnt() {
        return lessonCnt;
    }

    public void setLessonCnt(Double lessonCnt) {
        this.lessonCnt = lessonCnt;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public Integer getContractType() {
        return contractType;
    }

    public void setContractType(Integer contractType) {
        this.contractType = contractType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getContractCategory() {
        return contractCategory;
    }

    public void setContractCategory(Integer contractCategory) {
        this.contractCategory = contractCategory;
    }

    public ObjectId getActivityId() {
        return activityId;
    }

    public void setActivityId(ObjectId activityId) {
        this.activityId = activityId;
    }

    public ObjectId getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(ObjectId creatorId) {
        this.creatorId = creatorId;
    }

    public ObjectId getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(ObjectId ownerId) {
        this.ownerId = ownerId;
    }

    public ObjectId getLeadId() {
        return leadId;
    }

    public void setLeadId(ObjectId leadId) {
        this.leadId = leadId;
    }
}
