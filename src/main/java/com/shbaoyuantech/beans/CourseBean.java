package com.shbaoyuantech.beans;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import org.bson.types.ObjectId;

@MappingBean(table = "by_course", collection = "dim_courses")
public class CourseBean extends BaseBean {

    @MappingColumn(column = "name", field = "name")
    private String name;

    @MappingColumn(column = "grade_id", field = "grade_id", refBean = GradeBean.class)
    private ObjectId gradeId;

    @MappingColumn(column = "subject_id", field = "subject_id", refBean = SubjectBean.class)
    private ObjectId subjectId;
}
