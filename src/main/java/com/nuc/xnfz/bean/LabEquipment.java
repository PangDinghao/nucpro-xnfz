package com.nuc.xnfz.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class LabEquipment {
    @TableId(type = IdType.AUTO)
    int id;
    int labid;
    int equipmentid;






}
