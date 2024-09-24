package com.nuc.xnfz.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
/**
 * @author:3084软件科技
 * 实验室设备类
 * */
@Data
public class Equipment {
    @TableId(type = IdType.AUTO)
    private Integer equipmentid;
    @TableField("equipment_name")
    private String equipmentName;
    @TableField("equipment_company")
    private String equipmentCompany;
    @TableField("equipment_detial")
    private String equipmentDetial;
    @TableField("equipment_price")
    private double equipmentPrice;
    @TableField("equipment_num")
    private Integer equipmentNum;

    @TableField(exist = false)
    int usednum;
}
