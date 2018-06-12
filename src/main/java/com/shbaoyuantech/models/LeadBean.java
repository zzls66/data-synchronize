package com.shbaoyuantech.models;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import org.bson.types.ObjectId;

import java.util.Date;

@MappingBean(table = "by_lead", collection = "fact_leads", isCommonData = false)
public class LeadBean extends BaseBean{

    @MappingColumn(column = "name", field = "name")
    private String name;

    @MappingColumn(column = "mobile", field = "mobile")
    private String mobile;

    @MappingColumn(column = "create_time", field = "createTime")
    private Date createDateId;

    @MappingColumn(column = "last_update_time", field = "lastUpdateTime")
    private Date lastUpdateDateId;

    @MappingColumn(column = "status", field = "status")
    private Integer status;

    @MappingColumn(column = "creator_id", field = "creator_id", refCollection = "dim_staff")
    private ObjectId creatorId;

    @MappingColumn(column = "cc_id", field = "owner_id", refCollection = "dim_staff")
    private ObjectId ownerId;

    @MappingColumn(column = "store_id", field = "store_id", refCollection = "dim_stores")
    private ObjectId storeId;

    @MappingColumn(column = "source_id", field = "source_id", refCollection = "dim_sources")
    private ObjectId sourceId;

    @MappingColumn(column = "course_id", field = "course_id", refCollection = "dim_courses")
    private ObjectId courseId;

    @MappingColumn(column = "grade_id", field = "gradeId")
    private Integer gradeId;

    @MappingColumn(column = "subject_id", field = "subjectId")
    private Integer subjectId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getCreateDateId() {
        return createDateId;
    }

    public void setCreateDateId(Date createDateId) {
        this.createDateId = createDateId;
    }

    public Date getLastUpdateDateId() {
        return lastUpdateDateId;
    }

    public void setLastUpdateDateId(Date lastUpdateDateId) {
        this.lastUpdateDateId = lastUpdateDateId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ObjectId getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(ObjectId creatorId) {
        this.creatorId = creatorId;
    }

    public ObjectId getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(ObjectId ownerId) {
        this.ownerId = ownerId;
    }

    public ObjectId getStoreId() {
        return storeId;
    }

    public void setStoreId(ObjectId storeId) {
        this.storeId = storeId;
    }

    public ObjectId getSourceId() {
        return sourceId;
    }

    public void setSourceId(ObjectId sourceId) {
        this.sourceId = sourceId;
    }

    public ObjectId getCourseId() {
        return courseId;
    }

    public void setCourseId(ObjectId courseId) {
        this.courseId = courseId;
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }
}
