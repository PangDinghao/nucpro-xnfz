package com.nuc.xnfz.serivce.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuc.xnfz.bean.*;
import com.nuc.xnfz.bean.Class;
import com.nuc.xnfz.mapper.ClassMapper;
import com.nuc.xnfz.mapper.PlanCourseMapper;
import com.nuc.xnfz.mapper.StudentCourseMapper;
import com.nuc.xnfz.mapper.UserMapper;
import com.nuc.xnfz.serivce.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {
    @Autowired
    ClassMapper classMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    PlanCourseMapper planCourseMapper;
    @Autowired
    StudentCourseMapper studentCourseMapper;

    @Override
    public boolean removeAllByid(Integer classid) {
        int i = userMapper.removeClassid(classid);
        int i1 = classMapper.deleteById(classid);
        return true;
    }

    @Override
    public int addClassByplanid(Integer planid, Integer classid1) {
        Class aClass = classMapper.selectById(classid1);
        int update = 0;//更新的行数
        if (aClass != null) {
            aClass.setTrainingPlanid(planid);
            update = classMapper.updateById(aClass);//class的planid更新
        }
        List<User> stuByClassid = userMapper.getStuByClassid(classid1);//获取该班级所有的学生

        List<Course> courseByPlanId = planCourseMapper.getCourseByPlanId(planid);

        StudentCourse studentCourse = new StudentCourse();

        for (User user:stuByClassid){
            for (Course course:courseByPlanId){
                studentCourse.setUserid(user.getUserid());
                studentCourse.setCourseid(course.getCourseid());
                studentCourse.setId(0);
                studentCourseMapper.insert(studentCourse);
            }
        }

        return update;
    }

    @Override
    public int removeClassByplanid(Integer planid, Integer classid1) {

        Class aClass = classMapper.selectById(classid1);
        aClass.setTrainingPlanid(0);
        int update = classMapper.updateById(aClass);//class的planid更新
        List<User> stuByClassid = userMapper.getStuByClassid(classid1);//获取该班级所有的学生

        List<Course> courseByPlanId = planCourseMapper.getCourseByPlanId(planid);

        StudentCourse studentCourse = new StudentCourse();

        for (User user:stuByClassid){

            List<Integer> integers = studentCourseMapper.getidByUserid(user.getUserid());//要删除的student_course主键

            for (Integer integer:integers){
                studentCourseMapper.deleteById(integer);
            }
        }


        return update;
    }
}

