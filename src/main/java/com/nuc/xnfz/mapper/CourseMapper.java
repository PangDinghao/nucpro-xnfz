package com.nuc.xnfz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuc.xnfz.bean.Course;
import com.nuc.xnfz.bean.Role;
import com.nuc.xnfz.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {
    Course findById(Integer courseid);

    List<Course> findAll(int beginIndex, int pageSize, Course course, Integer userid, String opentime);//分页条件查询符合条件的信息

    int findTotal(int beginIndex, int pageSize, Course course, Integer userid, String opentime);//查询符合条件的总条数

    List<Course> GetListCourseAll();//获取到所有的Coruse数据

}
