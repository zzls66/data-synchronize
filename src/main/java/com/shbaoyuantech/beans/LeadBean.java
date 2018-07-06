package com.shbaoyuantech.beans;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import org.bson.types.ObjectId;

import java.util.Date;

@MappingBean(table = "by_lead", collection = "fact_leads")
public class LeadBean extends BaseBean {

    @MappingColumn(column = "name", field = "name")
    private String name;

    @MappingColumn(column = "mobile", field = "mobile")
    private String mobile;

    @MappingColumn(column = "create_time", field = "createTime")
    private Date createTime;

    @MappingColumn(field = "createDate_id", refDateField = "createTime", isSupplemental = true)
    private ObjectId createDateId;

    @MappingColumn(column = "last_update_time", field = "lastUpdateTime")
    private Date lastUpdateTime;

    @MappingColumn(field = "lastUpdateDate_id", refDateField = "lastUpdateTime", isSupplemental = true)
    private ObjectId lastUpdateDateId;

    @MappingColumn(column = "status", field = "status")
    private Integer status;

    @MappingColumn(column = "creator_id", field = "creator_id", refBean = StaffBean.class)
    private ObjectId creatorId;

    @MappingColumn(column = "cc_id", field = "owner_id", refBean = StaffBean.class)
    private ObjectId ownerId;

    @MappingColumn(column = "store_id", field = "store_id", refBean = StoreBean.class)
    private ObjectId storeId;

    @MappingColumn(column = "source_id", field = "source_id", refBean = SourceBean.class)
    private ObjectId sourceId;

    @MappingColumn(column = "course_id", field = "course_id", refBean = CourseBean.class)
    private ObjectId courseId;

    @MappingColumn(column = "grade_id", field = "gradeId", refBean = GradeBean.class)
    private ObjectId gradeId;

    @MappingColumn(column = "subject_id", field = "subjectId", refBean = SubjectBean.class)
    private ObjectId subjectId;
}
