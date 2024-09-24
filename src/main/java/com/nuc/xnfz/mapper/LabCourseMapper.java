package com.nuc.xnfz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuc.xnfz.bean.Course;
import com.nuc.xnfz.bean.Lab;
import com.nuc.xnfz.bean.LabCourse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LabCourseMapper extends BaseMapper<LabCourse> {
    List<Lab> getLabByCourseId(Integer courseid);//根据courseid查询到所有Lab（课程的实验室安排）


    List<Integer> getIdByCourseid(int courseid);
}
