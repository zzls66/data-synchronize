package com.shbaoyuantech.beans;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;

@MappingBean(table = "by_contract_hour_packge", collection = "dim_contract_hour_package")
public class ContractHourPackgeBean extends BaseBean {

    @MappingColumn(column = "valid_month", field = "validMonth")
    private Integer validMonth;
}
