package com.nuc.xnfz.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

/**
 * @author:3084软件科技
 * 实验室类
 * */
@Data
public class Lab {
    @TableId(value = "labid", type = IdType.AUTO)
    int labid;
    @TableField("lab_name")
    String labName;
    @TableField("lab_stunum")
    int labStunum;
    @TableField("lab_location")
    String labLocation;
    @TableField(exist = false)
    int courseSection;//记录课程在第几节

    @TableField(exist = false)
    List<Equipment> equipmentList;


}
