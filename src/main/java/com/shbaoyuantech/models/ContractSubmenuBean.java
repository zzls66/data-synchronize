package com.shbaoyuantech.models;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import org.bson.types.ObjectId;

@MappingBean(table = "by_contract_submenu", collection = "fact_contract_allocated", isCommonData = false)
public class ContractSubmenuBean extends BaseBean{

    @MappingColumn(column = "", field = "isStoreAllocated", isRedundant = true)
    private Boolean isStoreAllocated;

    @MappingColumn(column = "weight", field = "weight")
    private Double weight;

    @MappingColumn(column = "contract_id", field = "contract_id", refCollection = "fact_contracts")
    private ObjectId contractId;

    @MappingColumn(column = "store_id", field = "store_id", refCollection = "dim_stores")
    private ObjectId storeId;

    @MappingColumn(column = "staff_id", field = "staff_id", refCollection = "dim_staff")
    private ObjectId staffId;

    @MappingColumn(column = "model", field = "model")
    private Integer model;

    public Boolean getStoreAllocated() {
        return isStoreAllocated;
    }

    public void setStoreAllocated(Boolean storeAllocated) {
        isStoreAllocated = storeAllocated;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public ObjectId getContractId() {
        return contractId;
    }

    public void setContractId(ObjectId contractId) {
        this.contractId = contractId;
    }

    public ObjectId getStoreId() {
        return storeId;
    }

    public void setStoreId(ObjectId storeId) {
        this.storeId = storeId;
    }

    public ObjectId getStaffId() {
        return staffId;
    }

    public void setStaffId(ObjectId staffId) {
        this.staffId = staffId;
    }

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }
}
