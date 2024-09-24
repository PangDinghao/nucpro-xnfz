package com.nuc.xnfz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuc.xnfz.bean.Class;
import com.nuc.xnfz.bean.Notice;
import com.nuc.xnfz.common.AddToClassRequest;
import com.nuc.xnfz.common.AddToPlanRequest;
import com.nuc.xnfz.common.QueryPageParam;
import com.nuc.xnfz.common.R;
import com.nuc.xnfz.serivce.ClassService;
import com.nuc.xnfz.serivce.NoticeService;
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
@RequestMapping("/class")
public class ClassController {
    @Autowired
    private ClassService classService;

    /*
    * 1.新增
    * 2.删除
    * 3.修改
    * 4.条件分页查询
    * 5.返回所有班级
    * */

    //1.新增
    @RequestMapping("/save")
    public R save(@RequestBody Class class1){

        // 获取当前日期和时间
        LocalDateTime now = LocalDateTime.now();
        // 格式化日期时间
        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(now);

        class1.setCreateDatetime(timestamp);
        class1.setUpdateDatetime(timestamp);

        //班好已存在的情况
        Class getOneByClassno = classService.getOne(
                new QueryWrapper<Class>()
                        .eq("classno", class1.getClassno()));

        if(getOneByClassno!=null){
            //注册失败
            return R.fail().message("班号已存在");
        }else {
            boolean flag = classService.save(class1);
            if (flag){
                //注册成功
                return R.ok();
            }else {
                //注册失败
                return R.fail().message("新增班级失败");
            }
        }
    }

    //2.删除
    @GetMapping("/remove")
    public R remove(Integer classid){
        boolean flag = classService.removeAllByid(classid);
        if (flag){
            return R.ok().message("删除成功");
        }else {
            return R.fail().message("删除失败");
        }

    }

    //3.修改
    @RequestMapping("/update")
    public R update(@RequestBody Class class1){

        // 获取当前日期和时间
        LocalDateTime now = LocalDateTime.now();
        // 格式化日期时间
        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(now);

        class1.setUpdateDatetime(timestamp);

        boolean flag = classService.updateById(class1);

        if (flag) {
            return R.ok().message("修改成功");
        } else {
            return R.fail().message("修改失败");
        }

    }

    //4.条件分页查询
    @PostMapping("/listPage")
    public R listPage(@RequestBody QueryPageParam query) throws ParseException {


        Page<Class> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());


        HashMap param = query.getParam();
        String classno = (String) param.get("classno");
        String training_planid = null;
        String userid=null;

        if (param.get("trainingPlanid")!=null){
            //实训计划id
            training_planid = param.get("trainingPlanid").toString();
        }

        if (param.get("userid")!=null){
            //班主任用户id
            userid = param.get("userid").toString();
        }




        String Datetime =  (String) param.get("createDatetime");
        String createDatetime = null;

        LambdaQueryWrapper<Class> lambdaQueryWrapper = new LambdaQueryWrapper();


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


        if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(classno)&&!"".equals(classno)){
            lambdaQueryWrapper.like(Class::getClassno,classno);
        }
        if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(userid)&&!"".equals(userid)){
            lambdaQueryWrapper.like(Class::getUserid,userid);
        }
        if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(training_planid)&&!"".equals(training_planid)){
            lambdaQueryWrapper.like(Class::getTrainingPlanid,training_planid);
        }
        if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(createDatetime)&&!"".equals(createDatetime)){
            lambdaQueryWrapper.like(Class::getCreateDatetime,createDatetime);
        }


        IPage result = classService.page(page,lambdaQueryWrapper);

        return R.ok(result.getRecords(), result.getTotal());
    }

    //5.返回所有班级信息
    @PostMapping("/list")
    public R list(){
        List<Class> list = classService.list();
        return R.ok(list);

    }

    //6.添加班级至实训计划（包括班级所有的学生）
    @RequestMapping("/addToPlan")
    public R addClassStu(@RequestBody AddToPlanRequest addToPlanRequest){

        Integer[] classid = addToPlanRequest.getAddClassId();
        Integer planid = addToPlanRequest.getPlanid();

        if (classid.length==0){
            return R.fail().message("未选择班级错误");
        }

        for (Integer classid1:classid){

            int i = classService.addClassByplanid(planid, classid1);
            if (i==0){
                return R.fail().message("更新classid失败");
            }
        }
        return R.ok();
    }

    //6.将班级移除至实训计划（包括班级所有的学生）
    @RequestMapping("/removeToPlan")
    public R removeClassStu(@RequestBody AddToPlanRequest addToPlanRequest){

        Integer[] classid = addToPlanRequest.getAddClassId();
        Integer planid = addToPlanRequest.getPlanid();

        if (classid.length==0){
            return R.fail().message("未选择班级错误");
        }

        for (Integer classid1:classid){

            int i = classService.removeClassByplanid(planid, classid1);
            if (i==0){
                return R.fail().message("更新classid失败");
            }
        }
        return R.ok();
    }

}
