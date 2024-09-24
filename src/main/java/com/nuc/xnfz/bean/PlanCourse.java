package com.nuc.xnfz.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author:3084软件科技
 * 实训计划课程关系类
 * */
@Data
public class PlanCourse {
    @TableId(value = "id", type = IdType.AUTO)
    int id;
    int planid;
    int courseid;


}
