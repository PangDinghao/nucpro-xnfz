package com.nuc.xnfz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuc.xnfz.bean.Course;
import com.nuc.xnfz.bean.PlanCourse;
import com.nuc.xnfz.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface PlanCourseMapper extends BaseMapper<PlanCourse> {
    List<Course> getCourseByPlanId(Integer planid);//根据planid查询到所有course
    int getIdByPlanCourseid(int trainingplanid,int courseid);//根据trainingplanid和courseid查询主键

    List<Integer> getIdByPlanid(int planid);//根据planid查询所有符合的主键
}
