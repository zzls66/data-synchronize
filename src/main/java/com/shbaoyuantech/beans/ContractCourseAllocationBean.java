package com.shbaoyuantech.beans;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import org.bson.types.ObjectId;

@MappingBean(table = "by_contract_course_allocation", collection = "fact_contract_students")
public class ContractCourseAllocationBean extends BaseBean {

    @MappingColumn(column = "student_id", field = "student_id", refBean = StudentBean.class)
    private ObjectId studentId;

    @MappingColumn(column = "lesson_hour", field = "lessonHour")
    private Double lessonHour;

    @MappingColumn(column = "course_id", field = "course_id", refBean = CourseBean.class)
    private ObjectId courseId;

    @MappingColumn(column = "grade_id", field = "grade_id", refBean = GradeBean.class)
    private ObjectId gradeId;

    @MappingColumn(column = "subject_id", field = "subject_id", refBean = SubjectBean.class)
    private ObjectId subjectId;

    @MappingColumn(column = "contract_id", field = "contract_id", refBean = ContractBean.class)
    private ObjectId contractId;
}
