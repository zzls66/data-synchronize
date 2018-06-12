package com.shbaoyuantech.models;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import org.bson.types.ObjectId;

@MappingBean(table = "by_contract_course_allocation", collection = "fact_contract_students", isCommonData = false)
public class ContractCourseAllocationBean extends BaseBean{

    @MappingColumn(column = "student_id", field = "student_id", refCollection = "dim_students")
    private ObjectId studentId;

    @MappingColumn(column = "lesson_hour", field = "lessonHour")
    private Double lessonHour;

    @MappingColumn(column = "course_id", field = "course_id", refCollection = "dim_courses")
    private ObjectId courseId;

    @MappingColumn(column = "grade_id", field = "grade_id", refCollection = "dim_grades")
    private ObjectId gradeId;

    @MappingColumn(column = "subject_id", field = "subject_id", refCollection = "dim_subjects")
    private ObjectId subjectId;

    @MappingColumn(column = "contract_id", field = "contract_id", refCollection = "fact_contracts")
    private ObjectId contractId;

    public ObjectId getStudentId() {
        return studentId;
    }

    public void setStudentId(ObjectId studentId) {
        this.studentId = studentId;
    }

    public Double getLessonHour() {
        return lessonHour;
    }

    public void setLessonHour(Double lessonHour) {
        this.lessonHour = lessonHour;
    }

    public ObjectId getCourseId() {
        return courseId;
    }

    public void setCourseId(ObjectId courseId) {
        this.courseId = courseId;
    }

    public ObjectId getGradeId() {
        return gradeId;
    }

    public void setGradeId(ObjectId gradeId) {
        this.gradeId = gradeId;
    }

    public ObjectId getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(ObjectId subjectId) {
        this.subjectId = subjectId;
    }

    public ObjectId getContractId() {
        return contractId;
    }

    public void setContractId(ObjectId contractId) {
        this.contractId = contractId;
    }
}
