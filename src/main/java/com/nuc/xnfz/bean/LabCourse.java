package com.nuc.xnfz.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author:3084软件科技
 * 实验室课程关联类
 * */

@Data
public class LabCourse {
    @TableId(type = IdType.AUTO)
    int id;
    int labid;
    int courseid;
    @TableField("course_section")
    int courseSection;
}
