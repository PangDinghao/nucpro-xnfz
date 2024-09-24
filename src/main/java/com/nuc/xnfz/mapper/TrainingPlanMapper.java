package com.nuc.xnfz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuc.xnfz.bean.Trainingplan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TrainingPlanMapper extends BaseMapper<Trainingplan> {
    Trainingplan findByid(Integer planid);

    List<Trainingplan> findAll(int beginIndex, int pageSize, Trainingplan traningPlan, String plan_opentime);//分页条件查询符合条件的信息

    int findTotal(Trainingplan traningPlan, String plan_opentime);//查询符合条件的总条数

    List<Trainingplan> listAll();//返回所有
}
