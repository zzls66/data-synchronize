package com.shbaoyuantech.beans;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;

@MappingBean(table = "by_store", collection = "dim_stores")
public class StoreBean extends BaseBean {

    @MappingColumn(column = "company_id", field = "companyId")
    private Integer companyId;

    @MappingColumn(column = "name", field = "name")
    private String name;

    @MappingColumn(column = "city_code", field = "cityCode")
    private String cityCode;

    @MappingColumn(column = "serial_no", field = "region")
    private Integer region;
}
