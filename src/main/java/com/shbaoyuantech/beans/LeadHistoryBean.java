package com.shbaoyuantech.beans;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

@MappingBean(table = "by_lead_history", collection = "fact_lead_histories")
public class LeadHistoryBean extends BaseBean {

    @MappingColumn(column = "create_time", field = "createTime")
    private Date createTime;

    @MappingColumn(field = "createDate_id", refDateField = "createTime", isSupplemental = true)
    private ObjectId createDateId;

    @MappingColumn(column = "status", field = "status")
    private Integer status;

    @MappingColumn(column = "operator_id", field = "operator_id", refBean = StaffBean.class)
    private ObjectId operatorId;

    @MappingColumn(column = "owner_id", field = "owner_id", refBean = StaffBean.class)
    private ObjectId ownerId;

    @MappingColumn(field = "roles", isSupplemental = true)
    private List<Integer> roles;

    @MappingColumn(column = "store_id", field = "store_id", refBean = StoreBean.class)
    private ObjectId storeId;

    @MappingColumn(column = "lead_id", field = "lead_id", refBean = LeadBean.class)
    private ObjectId leadId;

    @MappingColumn(column = "related_contract_id", field = "relatedContract_id", refBean = ContractBean.class)
    private ObjectId relatedContractId;
}
