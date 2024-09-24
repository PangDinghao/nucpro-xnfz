package com.nuc.xnfz.serivce.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuc.xnfz.bean.Course;
import com.nuc.xnfz.bean.PlanCourse;
import com.nuc.xnfz.bean.Trainingplan;
import com.nuc.xnfz.mapper.PlanCourseMapper;
import com.nuc.xnfz.mapper.TrainingPlanMapper;
import com.nuc.xnfz.serivce.ClassService;
import com.nuc.xnfz.serivce.TrainingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingPlanServiceImpl extends ServiceImpl<TrainingPlanMapper, Trainingplan> implements TrainingPlanService {
    @Autowired
    TrainingPlanMapper traningPlanMapper;
    @Autowired
    PlanCourseMapper planCourseMapper;

    @Autowired
    ClassService classService;

    @Override
    public boolean removeAllByid(Integer trainingplanid) {
        Trainingplan trainingplan = traningPlanMapper.findByid(trainingplanid);
        List<Course> courseList = trainingplan.getCourseList();
        int planid = trainingplan.getPlanid();

        List<Integer> plancourseid = planCourseMapper.getIdByPlanid(planid);

        for (int id:plancourseid) {
            int i = planCourseMapper.deleteById(id);
            if (i==0){
                return false;
            }
        }


        for (Course course: courseList) {
            classService.removeClassByplanid(trainingplanid, course.getCourseid());
        }

        int i = traningPlanMapper.deleteById(trainingplanid);
        if (i==0){
            return false;
        }
        return true;
    }

    @Override
    public List<Trainingplan> findAll(int beginIndex, int pageSize, Trainingplan traningPlan, String plan_opentime) {
        return traningPlanMapper.findAll(beginIndex,pageSize,traningPlan,plan_opentime);
    }

    @Override
    public int findTotal(Trainingplan traningPlan, String plan_opentime) {
        return traningPlanMapper.findTotal(traningPlan,plan_opentime);
    }

    @Override
    public boolean saveTrainingPlan(Trainingplan trainingPlan) {
        int i = traningPlanMapper.insert(trainingPlan);
        int planid = trainingPlan.getPlanid();

        List<Course> courseList = trainingPlan.getCourseList();

        PlanCourse planCourse = new PlanCourse();

        for(Course course :courseList){
            planCourse.setPlanid(planid);
            planCourse.setCourseid(course.getCourseid());
            planCourse.setId(0);
            int i2 = planCourseMapper.insert(planCourse);
            if (i2==0){
                return false;
            }
        }

        if (i==0){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public boolean updateTrainingPlanById(Trainingplan trainingplan) {
        List<Course> courseList = trainingplan.getCourseList();
        int planid = trainingplan.getPlanid();

        List<Integer> plancourseid = planCourseMapper.getIdByPlanid(planid);

        PlanCourse planCourse = new PlanCourse();

        for (int i :plancourseid){
            planCourseMapper.deleteById(i);
        }

        for (Course course : courseList) {
            planCourse.setCourseid(course.getCourseid());
            planCourse.setPlanid(planid);
            planCourse.setId(0);

            int insert = planCourseMapper.insert(planCourse);
            if (insert==0){
                return false;
            }
        }

        int update = traningPlanMapper.updateById(trainingplan);
        if (update==0){
            return false;
        }
        return true;

    }

    @Override
    public boolean removeTrainingPlanById(int trainingplanid) {
        return false;
    }

    @Override
    public List<Trainingplan> listAll() {
        return traningPlanMapper.listAll();
    }
}
