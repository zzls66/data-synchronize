package com.shbaoyuantech.models;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;

@MappingBean(table = "by_contract_hour_packge", collection = "dim_contract_hour_package", isCommonData = false)
public class ContractHourPackgeBean extends BaseBean{

    @MappingColumn(column = "valid_month", field = "validMonth")
    private Integer validMonth;

    public int getValidMonth() {
        return validMonth;
    }

    public void setValidMonth(int validMonth) {
        this.validMonth = validMonth;
    }
}
