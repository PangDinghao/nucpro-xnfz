package com.nuc.xnfz.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuc.xnfz.bean.Course;
import com.nuc.xnfz.bean.Notice;
import com.nuc.xnfz.bean.StudentCourse;
import com.nuc.xnfz.bean.User;
import com.nuc.xnfz.common.QueryPageParam;
import com.nuc.xnfz.common.R;
import com.nuc.xnfz.serivce.CourseService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    CourseService courseService;


    /*
     * 1.新增
     * 2.删除
     * 3.修改
     * 4.条件分页查询课程及对应消息
     * 5.返回所有的课程
     * 6.修改学生的成绩
     * 7.根据学生id分页查看课程及成绩
     *
     * */

    //1.新增课程及其对应信息
    @RequestMapping("/save")
    public R save(@RequestBody Course course) {
        //新增或注册用户

        // 获取当前日期和时间
        LocalDateTime now = LocalDateTime.now();
        // 格式化日期时间
        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(now);

        course.setOpentime(timestamp);
        //考虑为课程名空的情况
        if (!StringUtils.hasLength(course.getCoursename())) {
            return R.fail().message("课程不能为空");
        }
        //考虑课程名已存在的情况
        Course getOneBycoursename = courseService.getOne(
                new QueryWrapper<Course>()
                        .eq("coursename", course.getCoursename()));

        if (getOneBycoursename != null) {
            //注册失败
            return R.fail().message("课程名已存在");
        } else {
            boolean flag = courseService.save(course);
            if (flag) {
                //注册成功
                return R.ok();
            } else {
                //注册失败
                return R.fail().message("课程新增失败");
            }
        }
    }

    //2.删除课程以及对应信息
    @GetMapping("/remove")
    public R remove(Integer courseid) {

        boolean flag = courseService.removeCourseById(courseid);

        if (flag) {
            return R.ok().message("删除成功");
        } else {
            return R.fail().message("删除失败");
        }

    }

    //3.修改课程及其对应信息
    @RequestMapping("/update")
    public R update(@RequestBody Course course) {

        boolean flag = courseService.updateCourseById(course);
        if (true) {
            //注册成功
            return R.ok();
        } else {
            //注册失败
            return R.fail().message("用户更新失败");
        }

    }

    //4.条件分页查询课程及对应消息
    @RequestMapping("/listPage")
    public R findListCoursePage(@RequestBody QueryPageParam query) {
        Course course = new Course();

        Page<Course> page = new Page();
        int pageNum = query.getPageNum();
        //每页的条数
        int pageSize = query.getPageSize();
        int pageCurrent = (pageNum - 1) * pageSize;

        HashMap param = query.getParam();
        String coursename = (String) param.get("coursename");
        String Datetime = (String) param.get("opentime");
        String opentime = null;
        Integer userid = null;

        if (param.get("userid") != "") {
            userid = (Integer) param.get("userid");
        }
        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(Datetime) && !"".equals(Datetime)) {
            // 去掉时区信息
            Datetime = Datetime.replaceAll("GMT[\\+\\-]\\d{4} \\((.*?)\\)", "");

            // 创建 SimpleDateFormat 对象，用于解析输入日期字符串
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss", Locale.ENGLISH);

            // 创建 SimpleDateFormat 对象，用于格式化日期
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                // 将字符串类型的日期解析为 Date 对象
                Date date = inputFormat.parse(Datetime);

                // 格式化 Date 对象为指定格式的日期字符串
                opentime = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        course.setCoursename(coursename);

        //从pageCurrent到pageSize条数的数据
        List<Course> courseList = courseService.findAll(pageCurrent, pageSize, course, userid, opentime);
        //总条数
        long Total = courseService.findTotal(pageCurrent, pageSize, course, userid, opentime);

        return R.ok(courseList, Total);
    }

    //5.返回所有的课程
    @RequestMapping("/listAll")
    public R GetListCourseAll() {

        //返回所有数据
        List<Course> courseList = courseService.GetListCourseAll();
        //总条数

        return R.ok(courseList);
    }

    //6.修改学生的成绩
    @PostMapping("/updateGrade")
    public R updateGrade(@RequestBody StudentCourse studentCourse) {

        boolean b = courseService.updateGrade(studentCourse);


        return R.ok();
    }

    //7.根据学生id分页查看课程及成绩
    @PostMapping("/getCourseByStu")
    public R getCourseByStu(@RequestBody QueryPageParam query) {

        int pageNum = query.getPageNum();
        //每页的条数
        int pageSize = query.getPageSize();
        int pageCurrent = (pageNum - 1) * pageSize;

        HashMap param = query.getParam();
        Integer userid = (Integer) param.get("userid");

        List<Course> courseByStu = courseService.getCourseByStu(pageCurrent, pageSize, userid);
        long courseTotalByStu = courseService.getCourseTotalByStu(userid);

        return R.ok(courseByStu,courseTotalByStu);
    }

    //
    @PostMapping("/getCourseByTeacher")
    public R getCourseByTeacherId(@RequestBody QueryPageParam query) {

        int pageNum = query.getPageNum();
        //每页的条数
        int pageSize = query.getPageSize();
        int pageCurrent = (pageNum - 1) * pageSize;

        HashMap param = query.getParam();
        Integer userid = (Integer) param.get("userid");

        List<Course> courseByTeacher = courseService.getCourseByTeacherId(pageCurrent, pageSize, userid);
        long courseTotalByTeacher = courseService.getCourseTotalByTeacher(userid);
        return R.ok(courseByTeacher,courseTotalByTeacher);
    }
}
