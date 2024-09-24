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
 * 实训计划类
 * */

@Data
public class Trainingplan {
    @TableId(value = "planid", type = IdType.AUTO)
    private int planid;
    @TableField("plan_name")
    private String planName;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField("plan_opentime")
    private Date planOpentime;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField("plan_endtime")
    private Date planEndtime;
    @TableField("plan_goal")
    private String planGoal;
    @TableField(exist = false)
    private List<Course> courseList;
    //实训负责老师的主键id
    private int userid;


}
