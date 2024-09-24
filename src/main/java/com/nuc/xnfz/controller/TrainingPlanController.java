package com.nuc.xnfz.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuc.xnfz.bean.Course;
import com.nuc.xnfz.bean.Trainingplan;
import com.nuc.xnfz.common.QueryPageParam;
import com.nuc.xnfz.common.R;
import com.nuc.xnfz.serivce.TrainingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/plan")
public class TrainingPlanController {
    @Autowired
    private TrainingPlanService trainingPlanService;

    /*
     * 1.新增
     * 2.删除
     * 3.修改
     * 4.条件分页查询
     * 5.查询所有计划
     * */

    //1.新增
    @RequestMapping("/save")
    public R save(@RequestBody Trainingplan trainingPlan){

        //实训计划名已存在的情况
        Trainingplan getOneByPlanname = trainingPlanService.getOne(
                new QueryWrapper<Trainingplan>()
                        .eq("plan_name", trainingPlan.getPlanName()));

        if(getOneByPlanname!=null){
            //注册失败
            return R.fail().message("计划名已存在");
        }else {
            boolean flag = trainingPlanService.saveTrainingPlan(trainingPlan);
            if (flag){
                //注册成功
                return R.ok();
            }else {
                //注册失败
                return R.fail().message("新增计划失败");
            }
        }
    }

    //2.删除
    @GetMapping("/remove")
    public R remove( Integer trainingplanid){
        boolean flag = trainingPlanService.removeAllByid(trainingplanid);

        if (flag){
            return R.ok().message("删除成功");
        }else {
            return R.fail().message("删除失败");
        }

    }

    //3.修改
    @RequestMapping("/update")
    public R update(@RequestBody Trainingplan trainingplan){

        boolean flag = trainingPlanService.updateTrainingPlanById(trainingplan);
        if (flag) {
            return R.ok().message("修改成功");
        } else {
            return R.fail().message("修改失败");
        }

    }

    //4.条件分页查询
    @PostMapping("/listPage")
    public R listPage(@RequestBody QueryPageParam query) throws ParseException {


        Trainingplan trainingPlan=new Trainingplan();

        Page<Course> page = new Page();
        int pageNum = query.getPageNum();
        //每页的条数
        int pageSize = query.getPageSize();
        int pageCurrent = (pageNum-1)*pageSize;

        HashMap param = query.getParam();
        String planname = (String) param.get("planname");
        Integer userid =0;
        String Datetime =  (String) param.get("planOpentime");
        String opentime = null;

        if ( param.get("userid")!=null&&param.get("userid")!=""){
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

        trainingPlan.setPlanName(planname);
        trainingPlan.setUserid(userid);

        //从pageCurrent到pageSize条数的数据
        List<Trainingplan> trainingPlanList = trainingPlanService.findAll(pageCurrent, pageSize, trainingPlan,opentime);
        //总条数
        long Total = trainingPlanService.findTotal( trainingPlan,opentime);

        return R.ok(trainingPlanList,Total);
    }

    //5.查询所有计划
    @PostMapping("/listAll")
    public R listPage() {
        List<Trainingplan> list = trainingPlanService.listAll();

        return R.ok(list);
    }
}
