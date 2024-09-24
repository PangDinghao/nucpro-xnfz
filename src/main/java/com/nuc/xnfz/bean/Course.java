package com.nuc.xnfz.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author:3084软件科技
 * 课程类
 * */
@Data
public class Course {
    @TableId(value = "courseid", type = IdType.AUTO)
    private int courseid;
    private String coursename;
    private String coursedetail;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")//时区+8
    private Date opentime;
    @TableField(exist = false)//不与数据库关联
    private List<User> userList;//课程教师列表
    @TableField(exist = false)//不与数据库关联
    private List<User> stuList;//课程学生列表@TableField(exist = false)//不与数据库关联
    @TableField(exist = false)//不与数据库关联
    private List<Lab> labList;//课程学生列表@TableField(exist = false)//不与数据库关联
    @TableField(exist = false)//不与数据库关联
    private double grade;//课程学生列表


}
