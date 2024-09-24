package com.nuc.xnfz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuc.xnfz.bean.Class;
import com.nuc.xnfz.bean.Lab;
import com.nuc.xnfz.bean.LabCourse;
import com.nuc.xnfz.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LabMapper extends BaseMapper<Lab> {
    Lab findById(int labid);

    List<Lab> findAll(int beginIndex, int pageSize, Lab lab);//查询符合条件的信息

    int findTotal(int beginIndex,int pageSize,Lab lab);//查询符合条件的总条数



}
