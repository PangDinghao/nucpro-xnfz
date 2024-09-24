package com.nuc.xnfz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuc.xnfz.bean.Equipment;
import com.nuc.xnfz.bean.Lab;
import com.nuc.xnfz.bean.LabCourse;
import com.nuc.xnfz.bean.LabEquipment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LabEquipmentMapper extends BaseMapper<LabEquipment> {
   List<Equipment> getEquipmentByLabId(Integer labid);//根据labid查询到所有equipment

    List<Integer> getIdBylabid(int labid);//根据labid获取到关系主键
}
