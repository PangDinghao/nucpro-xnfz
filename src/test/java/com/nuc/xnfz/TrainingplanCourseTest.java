package com.nuc.xnfz;

import com.nuc.xnfz.bean.Course;
import com.nuc.xnfz.bean.Trainingplan;
import com.nuc.xnfz.mapper.PlanCourseMapper;
import com.nuc.xnfz.mapper.TrainingPlanMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
@SpringBootTest
public class TrainingplanCourseTest {
    @Autowired
    TrainingPlanMapper traningPlanMapper;
    @Autowired
    PlanCourseMapper planCourseMapper;

    @Test
    public void test1(){
        List<Course> courseByPlanId = planCourseMapper.getCourseByPlanId(1);
        System.out.println(courseByPlanId);
    }
    @Test
    public void test2(){
        Trainingplan byid = traningPlanMapper.findByid(1);
        System.out.println(byid);
    }
    @Test
    public void test3(){
        Trainingplan traningPlan = new Trainingplan();
        traningPlan.setPlanid(1);
        traningPlan.setPlanid(1);
        List<Trainingplan> all = traningPlanMapper.findAll(0,4,traningPlan,null);
        for (Trainingplan traningPlan1:all){
            System.out.println(traningPlan1);
        }
    }

}
