package com.shbaoyuantech.models;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import org.bson.types.ObjectId;

@MappingBean(table = "by_course", collection = "dim_courses", isCommonData = false)
public class CourseBean extends BaseBean{

    @MappingColumn(column = "name", field = "name")
    private String name;

    @MappingColumn(column = "grade_id", field = "grade_id", refCollection = "dim_grades")
    private ObjectId gradeId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectId getGradeId() {
        return gradeId;
    }

    public void setGradeId(ObjectId gradeId) {
        this.gradeId = gradeId;
    }
}
