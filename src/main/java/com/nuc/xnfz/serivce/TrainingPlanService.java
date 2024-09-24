package com.nuc.xnfz.serivce;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuc.xnfz.bean.Course;
import com.nuc.xnfz.bean.Trainingplan;

import java.util.List;

public interface TrainingPlanService extends IService<Trainingplan> {
    boolean removeAllByid(Integer trainingplanid);

    List<Trainingplan> findAll(int beginIndex, int pageSize, Trainingplan traningPlan, String plan_opentime);//分页条件查询符合条件的信息

    int findTotal(Trainingplan traningPlan, String plan_opentime);//查询符合条件的总条数

    boolean saveTrainingPlan(Trainingplan trainingPlan);//保存实训计划以及相关信息

    boolean updateTrainingPlanById(Trainingplan trainingplan);//更新用户以及对应信息

    boolean removeTrainingPlanById(int trainingplanid);//删除用户及对应信息

    List<Trainingplan> listAll();//返回所有
}
