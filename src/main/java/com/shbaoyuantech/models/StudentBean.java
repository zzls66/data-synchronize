package com.shbaoyuantech.models;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;

@MappingBean(table = "by_student", collection = "dim_students", isCommonData = false)
public class StudentBean extends BaseBean{

    @MappingColumn(column = "name", field = "name")
    private String name;

    @MappingColumn(column = "mobile", field = "mobile")
    private String mobile;

    @MappingColumn(column = "gender", field = "gender")
    private Integer gender;

    @MappingColumn(column = "age", field = "age")
    private Integer age;

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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
