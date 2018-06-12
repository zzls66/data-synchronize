package com.shbaoyuantech.models;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;

@MappingBean(table = "by_store", collection = "dim_stores")
public class StoreBean extends BaseBean{

    @MappingColumn(column = "company_id", field = "companyId")
    private Integer companyId;

    @MappingColumn(column = "name", field = "name")
    private String name;

    @MappingColumn(column = "city_code", field = "cityCode")
    private String cityCode;

    @MappingColumn(column = "serial_no", field = "region")
    private Integer region;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public Integer getRegion() {
        return region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }
}
