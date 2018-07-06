package com.shbaoyuantech.beans;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import org.bson.types.ObjectId;

@MappingBean(table = "by_contract_submenu", collection = "fact_contract_allocated")
public class ContractSubmenuBean extends BaseBean {

    @MappingColumn(column = "weight", field = "weight")
    private Double weight;

    @MappingColumn(column = "contract_id", field = "contract_id", refBean = ContractBean.class)
    private ObjectId contractId;

    @MappingColumn(column = "store_id", field = "store_id", refBean = StoreBean.class)
    private ObjectId storeId;

    @MappingColumn(column = "staff_id", field = "staff_id", refBean = StaffBean.class)
    private ObjectId staffId;

    @MappingColumn(column = "model", field = "model")
    private Integer model;
}
