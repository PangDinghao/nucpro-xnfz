package com.nuc.xnfz.serivce.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuc.xnfz.bean.*;
import com.nuc.xnfz.mapper.*;
import com.nuc.xnfz.serivce.CourseService;
import com.nuc.xnfz.serivce.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    TeacherCourseMapper teacherCourseMapper;
    @Autowired
    StudentCourseMapper studentCourseMapper;
    @Autowired
    LabCourseMapper labCourseMapper;

    public List<Course> findAll(int beginIndex, int pageSize, Course course, Integer userid, String opentime){
        List<Course> courseList = courseMapper.findAll(beginIndex, pageSize, course, userid, opentime);
        return courseList;
    }

    @Override
    public int findTotal(int beginIndex, int pageSize, Course course, Integer userid, String opentime) {
        int total = courseMapper.findTotal(beginIndex, pageSize, course, userid, opentime);
        return total;
    }

    @Override
    public boolean save(Course course) {
        int i = courseMapper.insert(course);
        //获取到user的主键
        int courseid = course.getCourseid();
        List<User> userList = course.getUserList();
        TeacherCourse teacherCourse = new TeacherCourse();
        // 获取当前日期和时间
        LocalDateTime now = LocalDateTime.now();
        // 格式化日期时间
        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(now);

        //如果用户类型不为空
        if (userList!=null) {
            for (User user : userList) {
                teacherCourse.setCourseid(courseid);
                teacherCourse.setUserid(user.getUserid());
                teacherCourse.setId(0);
                teacherCourse.setCreateDate(timestamp);

                int i1 = teacherCourseMapper.insert(teacherCourse);
                if (i1 == 0) {
                    return false;
                }
            }
        }
        if (i==0){
            return false;
        }

        List<Lab> labList = course.getLabList();
        if (labList != null && !labList.isEmpty()) {
            LabCourse labCourse = new LabCourse();
            for (Lab lab : labList) {
                labCourse.setLabid(lab.getLabid());
                labCourse.setCourseid(course.getCourseid());
                labCourse.setCourseSection(lab.getCourseSection());
                labCourse.setId(0);
                int insert = labCourseMapper.insert(labCourse);
                if (insert==0){
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean updateCourseById(Course course) {
        int i = courseMapper.updateById(course);
        List<User> userList = course.getUserList();

        TeacherCourse teacherCourse = new TeacherCourse();

        List<Integer> teacher_courseid = teacherCourseMapper.getIdByCourseid(course.getCourseid());

        // 获取当前日期和时间
        LocalDateTime now = LocalDateTime.now();
        // 格式化日期时间
        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(now);

        //修改课程安排(先删除相关关系安排再插入)
        List<Lab> labList = course.getLabList();

        List<Integer> idByCourseid = labCourseMapper.getIdByCourseid(course.getCourseid());

        for (int id : idByCourseid) {//删掉关系
            labCourseMapper.deleteById(id);
        }

        LabCourse labCourse = new LabCourse();
        for (Lab lab : labList) {//重新插入关系
            labCourse.setLabid(lab.getLabid());
            labCourse.setCourseid(course.getCourseid());
            labCourse.setCourseSection(lab.getCourseSection());
            labCourse.setId(0);
            int insert = labCourseMapper.insert(labCourse);
            if (insert==0){
                return false;
            }
        }


        //删去该用户的所有角色对应关系
        for (Integer id : teacher_courseid){
            teacherCourseMapper.deleteById(id);
        }

        if (userList!=null){
            for (User user : userList) {
                teacherCourse.setCourseid(course.getCourseid());
                teacherCourse.setUserid(user.getUserid());
                teacherCourse.setId(0);
                teacherCourse.setCreateDate(timestamp);
                int i1 = teacherCourseMapper.insert(teacherCourse);
                if (i1==0){
                    return false;
                }
            }
        }

        if (i!=0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean removeCourseById(int courseid) {
        Course course = courseMapper.findById(courseid);

        List<User> userList = course.getUserList();

        if (userList!=null){//如果用户类型不为空
            for (User user : userList) {
                int id = teacherCourseMapper.getIdByCourseUserid(user.getUserid(),courseid);

                int i1 = teacherCourseMapper.deleteById(id);
                if (i1==0){
                    return false;
                }
            }
        }
        int i = courseMapper.deleteById(courseid);
        if (i==0){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public List<Course> GetListCourseAll() {
        return courseMapper.GetListCourseAll();
    }

    @Override
    public boolean updateGrade(StudentCourse studentCourse) {
        int userid = studentCourse.getUserid();
        int courseid = studentCourse.getCourseid();
        int i = studentCourseMapper.getidByUserCourseid(userid, courseid);

        int i1 = studentCourseMapper.deleteById(i);

        int insert = studentCourseMapper.insert(studentCourse);
        if (insert==0){
            return false;
        }

        return true;
    }

    @Override
    public List<Course> getCourseByStu(int beginIndex, int pageSize, int studentid) {
        return studentCourseMapper.getCourseByStu(beginIndex,pageSize,studentid);
    }

    @Override
    public int getCourseTotalByStu(int studentid) {
        return studentCourseMapper.getCourseTotalByStu(studentid);
    }

    @Override
    public int getCourseTotalByTeacher(int teacherid) {
        return teacherCourseMapper.getCourseTotalByTeacher(teacherid);
    }

    @Override
    public List<Course> getCourseByTeacherId(int beginIndex, int pageSize, int userid) {
        return teacherCourseMapper.getCourseByTeacherId(userid,beginIndex,pageSize);
    }
}
