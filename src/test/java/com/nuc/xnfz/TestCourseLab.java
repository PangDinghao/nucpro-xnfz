package com.nuc.xnfz;

import com.nuc.xnfz.bean.Course;
import com.nuc.xnfz.bean.Lab;
import com.nuc.xnfz.mapper.LabCourseMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestCourseLab {
    @Autowired
    LabCourseMapper labCourseMapper;
    @Test
    void test1(){
        List<Lab> labByCourseId = labCourseMapper.getLabByCourseId(1);
        System.out.println(labByCourseId);
    }
}
