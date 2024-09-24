package com.nuc.xnfz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuc.xnfz.bean.Notice;
import com.nuc.xnfz.common.AddToClassRequest;
import com.nuc.xnfz.common.QueryPageParam;
import com.nuc.xnfz.common.R;
import com.nuc.xnfz.bean.User;
import com.nuc.xnfz.serivce.UserService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * @author 53595
 */ //数据接口
@RestController
@RequestMapping("/user")
public class UserController {

    /*
    * 1.登录
    * 2.注册
    * 3.删除用户以及对应信息
    * 4.修改用户以及对应信息
    * 5.分页条件查询所有用户
    * 6.根据roleid查询用户
    * 7.根据班级主键查询班级人数
    * 8.添加用户至班级
    * 9.根据用户id更新班级
    * 10.根据课程courseid获取到学生信息（成绩相关）
    * */

    @Autowired
    UserService userService;

    //1.登录
    @RequestMapping("/login")
    public  R login(@RequestBody User user, HttpSession httpSession){
        if( !StringUtils.hasLength(user.getAccount())||
                !StringUtils.hasLength(user.getPassword())){
            return R.fail().message("用户名和密码不能为空");
        }

        User getOneByAccountAndPassword = userService.getOne(
                new QueryWrapper<User>()
                        .eq("account", user.getAccount())
                        .eq("password",user.getPassword())
        );

        if(getOneByAccountAndPassword!=null){

            //登陆成功
            httpSession.setAttribute("user",userService.findById(getOneByAccountAndPassword.getUserid()));
            User user1 = userService.findById(getOneByAccountAndPassword.getUserid());
            return R.ok().data(user1);
        }else {

            return R.fail().message("用户或密码名错误");
        }
    }

    //2.注册
    @RequestMapping("/save")
    public R save(@RequestBody User user){
        //新增或注册用户

        // 获取当前日期和时间
        LocalDateTime now = LocalDateTime.now();
        // 格式化日期时间
        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(now);

        user.setCreateDatetime(timestamp);
        user.setUpdateDatetime(timestamp);
        //考虑用户名为空的情况
        if (
                !StringUtils.hasLength(user.getAccount())||
                !StringUtils.hasLength(user.getPassword())){
            return R.fail().message("用户名和密码不能为空");
        }

        //考虑用户名已存在的情况
        User getOneByAccount = userService.getOne(
                new QueryWrapper<User>()
                        .eq("account", user.getAccount()));

        if(getOneByAccount!=null){
            //注册失败
            return R.fail().message("用户名已存在");
        }else {
            boolean flag = userService.save(user);
            if (flag){
                //注册成功
                return R.ok();
            }else {
                //注册失败
                return R.fail().message("用户注册失败");
            }
        }
    }
    //3.删除用户以及对应信息
    @GetMapping("/remove")
    public R remove( Integer userid){
        boolean flag = userService.removeUserById(userid);

        if (flag){
            return R.ok().message("删除成功");
        }else {
            return R.fail().message("删除失败");
        }

    }
    //4.修改
    @RequestMapping("/update")
    public R update(@RequestBody User user){

        // 获取当前日期和时间
        LocalDateTime now = LocalDateTime.now();
        // 格式化日期时间
        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(now);

        user.setUpdateDatetime(timestamp);

        boolean flag = userService.updateUserById(user);
        if (flag){
            //注册成功
            return R.ok();
        }else {
            //注册失败
            return R.fail().message("用户更新失败");
        }

    }

    //5.条件分页查询
    @RequestMapping("/listPage")
    public R findListUserPage(@RequestBody QueryPageParam query){
        User user=new User();

        int pageNum = query.getPageNum();
        int pageSize = query.getPageSize();//每页的条数
        int pageCurrent = (pageNum-1)*pageSize;

        HashMap param = query.getParam();
        String username = (String) param.get("username");
        String phone = (String) param.get("phone");
        Integer classid = (Integer) param.get("classid");
        String Datetime =  (String) param.get("createDatetime");
        String createDatetime = null;
        Integer roleid = null;

        if (param.get("roleid") != "") {
            roleid = (Integer) param.get("roleid");
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
                createDatetime = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        user.setUsername(username);
        user.setPhone(phone);
        user.setClassid(classid);//班级id


        //从pageCurrent到pageSize条数的数据
        List<User> userList = userService.findAll(pageCurrent, pageSize, user,roleid,createDatetime);
        //总条数
        long Total = userService.findTotal(pageCurrent, pageSize, user,roleid,createDatetime);

        return R.ok(userList,Total);
    }

    //6.根据roleid查询用户
    @GetMapping("/listByRoleid")
    public R findListByRoleid(Integer roleid){
        User user=new User();
        //从pageCurrent到pageSize条数的数据
        List<User> userList = userService.findUserByRoleid(roleid);

        return R.ok(userList);
    }

    //7.根据班级主键查询班级人数
    @GetMapping("/getstunum")
    public R getStuNumByClassid(Integer classid){
        int stuNum = userService.getStuNumByClassid(classid);
        return R.ok(stuNum);
    }


    //8.添加用户至班级
    @RequestMapping("/addToClass")
    public R addClassStu(@RequestBody AddToClassRequest AddStudentId){

        Integer[] studentId = AddStudentId.getAddStudentId();
        Integer classid = AddStudentId.getClassid();

        if (studentId.length==0){
            return R.fail().message("未选择用户错误");
        }

        for (Integer stuid:studentId){

            int i = userService.updateClassidByUserid(stuid, classid);
            if (i==0){
                return R.fail().message("更新classid失败");
            }
        }
        return R.ok();
    }

    //9.根据用户id更新班级
    @RequestMapping("/updateToClass")
    public R RemoveClassStu(@RequestBody AddToClassRequest AddStudentId){

        Integer[] studentId = AddStudentId.getAddStudentId();
        Integer classid = AddStudentId.getClassid();

        if (studentId.length==0){
            return R.fail().message("未选择用户错误");
        }

        for (Integer stuid:studentId){

            int i = userService.updateClassidByUserid(stuid, classid);
            if (i==0){
                return R.fail().message("更新classid失败");
            }
        }
        return R.ok();
    }

    //10.根据课程courseid获取到学生信息（成绩相关）
    @RequestMapping("/getStuBycourseid")
    public R getStuByCourseid(@RequestBody QueryPageParam query) {

        int pageNum = query.getPageNum();
        int pageSize = query.getPageSize();//每页的条数
        int pageCurrent = (pageNum-1)*pageSize;

        HashMap param = query.getParam();

        Integer courseid = (Integer) param.get("courseid");

        List<User> studentPagelistByCourseId = userService.getStudentPagelistByCourseId(pageCurrent,pageSize,courseid);

        long Total = userService.getStudentTotalByCourseId(courseid);

        return R.ok(studentPagelistByCourseId,Total);

    }




}
