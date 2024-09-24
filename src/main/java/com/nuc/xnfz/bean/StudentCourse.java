package com.nuc.xnfz.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class StudentCourse {
    @TableId( type = IdType.AUTO)
    private int id;
    private int userid;
    private int courseid;
    private double grade;//学的课程成绩
}
