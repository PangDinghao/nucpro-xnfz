package com.nuc.xnfz;

import com.nuc.xnfz.bean.Course;
import com.nuc.xnfz.bean.Lab;
import com.nuc.xnfz.bean.User;
import com.nuc.xnfz.mapper.CourseMapper;
import com.nuc.xnfz.mapper.StudentCourseMapper;
import com.nuc.xnfz.mapper.TeacherCourseMapper;
import com.nuc.xnfz.serivce.CourseService;
import com.nuc.xnfz.serivce.UserService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TeacherCourseTest {
    @Autowired
    TeacherCourseMapper teacherCourseMapper;
    @Autowired
    StudentCourseMapper studentCourseMapper;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CourseService courseService;

    @Autowired
    UserService userService;

    @Test
    void Test1(){
        List<User> teacherByCourseId = teacherCourseMapper.getTeacherByCourseId(2);
        for (User user:teacherByCourseId){
            System.out.println(user);
            System.out.println("--------------------------");
        }
    }
    @Test
    void Test2(){
        int idByCourseUserid = teacherCourseMapper.getIdByCourseUserid(2, 4);
        System.out.println(idByCourseUserid);
    }

    @Test
    void Test3(){
        Course course1 = courseMapper.findById(1);
        System.out.println(course1);
        List<User> userList = course1.getUserList();
        List<User> stuList = course1.getStuList();
        List<Lab> labList = course1.getLabList();
        for (User user:userList){
            System.out.println(user);
            System.out.println("--------------------------");
        }
        System.out.println("学生");
        for (User user:stuList){
            System.out.println(user);
            System.out.println("--------------------------");
        }
        System.out.println(labList);
    }@Test
    void Test4(){
        Course course = new Course();
        course.setCoursename("高等数学");
        List<Course> all = courseService.findAll(0, 4, course,2, null);
        for (Course course1:all){
            System.out.println(course1);
            System.out.println("--------------------------");
        }
    }
    @Test
    void Test5(){

        List<Course> courses = courseService.GetListCourseAll();
        for (Course course1:courses){
            System.out.println(course1);
            System.out.println("--------------------------");
        }
    }
    @Test
    void Test6(){

        List<Course> course1 = studentCourseMapper.getCourseByStudentId(5);
            System.out.println(course1);
            System.out.println("--------------------------");
        }

    @Test
    void Test7(){

        List<User> userList = studentCourseMapper.getStudentByCourseId(1);
        for (User user : userList) {
            System.out.println(user);
            System.out.println("--------------------------");
        }

    }

    @Test
    void Test8(){

        List<User> userList = userService.getStudentPagelistByCourseId(0,3,1);
        for (User user : userList) {
            System.out.println(user);
            System.out.println("--------------------------");
        }

    }

}
