package com.nuc.xnfz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuc.xnfz.bean.Course;
import com.nuc.xnfz.bean.StudentCourse;
import com.nuc.xnfz.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

@Mapper
public interface StudentCourseMapper extends BaseMapper<StudentCourse> {
    List<User> getStudentByCourseId(Integer courseid);
    List<Course> getCourseByStudentId(Integer userid);

    List<User> getStudentPagelistByCourseId(int beginIndex,int pageSize,Integer courseid);
    int getStudentTotalByCourseId(Integer courseid);

    List<Integer> getidByUserid(int userid);

    int getidByUserCourseid(int userid, int courseid);

    List<Course> getCourseByStu(int beginIndex,int pageSize,int studentid);//分页查询该学生的所有课程及成绩

    int getCourseTotalByStu(int studentid);//查询该学生的课程总数
}
