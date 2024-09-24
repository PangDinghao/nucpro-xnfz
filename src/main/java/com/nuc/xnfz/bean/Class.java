package com.nuc.xnfz.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author:3084软件科技
 * 班级类
 * */
@Data
public class Class {
    @TableId( type = IdType.AUTO)
    private int classid;
    private int classno;
    private int userid;//班主任主键
    @TableField("training_planid")
    private int trainingPlanid;//参与的实训的主键

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")//时区加8
    @TableField("create_datetime")
    private Date createDatetime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("update_datetime")
    private Date updateDatetime;

    @TableField("update_user")
    private String updateUser;

}
