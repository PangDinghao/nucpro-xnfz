package com.nuc.xnfz.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
/**
 * @author:3084软件科技
 * 教师课程关系类
 * */
@Data
public class TeacherCourse {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int userid;
    private  int courseid;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")//时区加8
    @TableField("create_date")
    private Date createDate;

}
