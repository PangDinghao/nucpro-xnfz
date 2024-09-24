package com.nuc.xnfz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuc.xnfz.bean.Course;
import com.nuc.xnfz.bean.Role;
import com.nuc.xnfz.bean.TeacherCourse;
import com.nuc.xnfz.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeacherCourseMapper extends BaseMapper<TeacherCourse> {
    List<User> getTeacherByCourseId(Integer courseid);
    List<Course> getCourseByTeacherId(Integer userid,int beginIndex,int pageSize);

    int getCourseTotalByTeacher(Integer teacherid);
    int getIdByCourseUserid(int userid,int courseid);//根据roleid和courseid查询主键

    List<Integer> getIdByCourseid(int courseid);//根据courseid查询所有符合的主键


}
