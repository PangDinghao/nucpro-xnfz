package com.nuc.xnfz.serivce;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuc.xnfz.bean.Course;
import com.nuc.xnfz.bean.StudentCourse;
import com.nuc.xnfz.bean.User;

import java.util.List;

public interface CourseService extends IService<Course> {
    List<Course> findAll(int beginIndex, int pageSize, Course course, Integer userid, String opentime);//分页条件查询符合条件的信息
    int findTotal(int beginIndex, int pageSize, Course course, Integer userid, String opentime);//查询符合条件的总条数

    boolean save(Course course);//保存课程及相关信息

    boolean updateCourseById(Course course);//更新用户以及对应信息

    boolean removeCourseById(int userid);//删除用户及对应信息

    List<Course> GetListCourseAll();//获取到所有的Coruse数据

    boolean updateGrade(StudentCourse studentCourse);

    List<Course> getCourseByStu(int beginIndex,int pageSize,int studentid);//分页查询该学生的所有课程及成绩

    int getCourseTotalByStu(int studentid);//查询该学生的课程总数

    int getCourseTotalByTeacher(int teacherid);//查询该学生的课程总数

    List<Course> getCourseByTeacherId(int beginIndex,int pageSize,int userid);
}
