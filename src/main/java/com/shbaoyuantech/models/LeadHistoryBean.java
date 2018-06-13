package com.shbaoyuantech.models;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

@MappingBean(table = "by_lead_history", collection = "fact_lead_histories", isCommonData = false)
public class LeadHistoryBean extends BaseBean{

    @MappingColumn(column = "create_time", field = "createTime")
    private Date createTime;

    @MappingColumn(column = "status", field = "status")
    private Integer status;

    @MappingColumn(column = "operator_id", field = "operator_id", refCollection = "dim_staff")
    private ObjectId operatorId;

    @MappingColumn(column = "owner_id", field = "owner_id", refCollection = "dim_staff")
    private ObjectId ownerId;

    @MappingColumn(column = "", field = "roles", isRedundant = true)
    private List<Integer> roles;

    @MappingColumn(column = "store_id", field = "store_id", refCollection = "dim_stores")
    private ObjectId storeId;

    @MappingColumn(column = "lead_id", field = "lead_id", refCollection = "fact_leads")
    private ObjectId leadId;

    @MappingColumn(column = "related_contract_id", field = "relatedContract_id", refCollection = "fact_contracts")
    private ObjectId relatedContractId;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ObjectId getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(ObjectId operatorId) {
        this.operatorId = operatorId;
    }

    public ObjectId getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(ObjectId ownerId) {
        this.ownerId = ownerId;
    }

    public List<Integer> getRoles() {
        return roles;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }

    public ObjectId getStoreId() {
        return storeId;
    }

    public void setStoreId(ObjectId storeId) {
        this.storeId = storeId;
    }

    public ObjectId getLeadId() {
        return leadId;
    }

    public void setLeadId(ObjectId leadId) {
        this.leadId = leadId;
    }

    public ObjectId getRelatedContractId() {
        return relatedContractId;
    }

    public void setRelatedContractId(ObjectId relatedContractId) {
        this.relatedContractId = relatedContractId;
    }
}
