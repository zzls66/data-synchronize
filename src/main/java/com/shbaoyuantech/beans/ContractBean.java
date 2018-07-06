package com.shbaoyuantech.beans;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import org.bson.types.ObjectId;

import java.util.Date;

@MappingBean(table = "by_contract", collection = "fact_contracts")
public class ContractBean extends BaseBean {

    @MappingColumn(column = "contract_no", field = "contractNo")
    private String contractNo;

    @MappingColumn(column = "store_id", field = "store_id", refBean = StoreBean.class)
    private ObjectId storeId;

    @MappingColumn(column = "actual_amt", field = "totalPrice")
    private Double actualAmt;

    @MappingColumn(column = "lesson_cnt", field = "lessonCnt")
    private Double lessonCnt;

    @MappingColumn(column = "create_time", field = "createTime")
    private Date createTime;

    @MappingColumn(field = "createDate_id", refDateField = "createTime", isSupplemental = true)
    private ObjectId createDateId;

    @MappingColumn(column = "contract_date", field = "contractDate")
    private Date contractDate;

    @MappingColumn(field = "contractDate_id", refDateField = "contractDate", isSupplemental = true)
    private ObjectId contractDateId;

    @MappingColumn(column = "contract_type", field = "contractType")
    private Integer contractType;

    @MappingColumn(column = "status", field = "status")
    private Integer status;

    @MappingColumn(column = "contract_category", field = "contractCategory")
    private Integer contractCategory;

    @MappingColumn(column = "class_hour_activity_id", field = "activity_id", refBean = ClassHourActivityBean.class)
    private ObjectId activityId;

    @MappingColumn(column = "creator_id", field = "creator_id", refBean = StaffBean.class)
    private ObjectId creatorId;

    @MappingColumn(column = "owner_id", field = "owner_id", refBean = StaffBean.class)
    private ObjectId ownerId;

    @MappingColumn(column = "lead_id", field = "lead_id", refBean = LeadBean.class)
    private ObjectId leadId;
}
