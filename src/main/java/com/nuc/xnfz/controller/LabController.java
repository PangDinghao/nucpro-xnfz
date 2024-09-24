package com.nuc.xnfz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuc.xnfz.bean.Lab;
import com.nuc.xnfz.bean.Notice;
import com.nuc.xnfz.bean.User;
import com.nuc.xnfz.common.QueryPageParam;
import com.nuc.xnfz.common.R;
import com.nuc.xnfz.serivce.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/lab")
public class LabController {
    @Autowired
    private LabService labService;

    /*
    * 1.添加
    * 2.删除
    * 3.修改
    * 4.条件分页查询
    * 5.查询所有实验室
    * */

    //1.添加
    @RequestMapping("/save")
    public R save(@RequestBody Lab lab) {

        if (    lab.getLabLocation() == null ||
                lab.getLabStunum() <= 0 ||
                lab.getLabLocation().isEmpty()) {
            return R.fail().message("实验室学生数量和位置不能为空");
        }
        // 判断实验室ID是否已存在
        Lab getOneById = labService.getOne(
                new QueryWrapper<Lab>()
                        .eq("labid", lab.getLabid()));
        if (getOneById!= null) {
            return R.fail().message("实验室ID已存在");
        } else {
            boolean flag = labService.savelab(lab);
            if (flag) {
                return R.ok();
            } else {
                return R.fail().message("添加实验室失败");
            }
        }
    }

    //2.删除
    @GetMapping("/remove")
    public R remove( Integer labid){
        boolean flag = labService.removeById(labid);
        if (flag){
            return R.ok().message("删除成功");
        }else {
            return R.fail().message("删除失败");
        }
    }

    //3.修改
    @RequestMapping("/update")
    public R update(@RequestBody Lab lab){
        boolean flag = labService.updateAllById(lab);
        if (flag) {
            return R.ok().message("修改成功");
        } else {
            return R.fail().message("修改失败");
        }
    }

    //4.条件分页查询
    @PostMapping("/listPage")
    public R listPage(@RequestBody QueryPageParam query) {
        Lab lab=new Lab();

        Page<Lab> page = new Page();
        int pageNum = query.getPageNum();
        int pageSize = query.getPageSize();//每页的条数
        int pageCurrent = (pageNum-1)*pageSize;

        HashMap param = query.getParam();
        String labName = (String) param.get("labName");
        String labLocation = (String) param.get("labLocation");


        lab.setLabName(labName);
        lab.setLabLocation(labLocation);


        //从pageCurrent到pageSize条数的数据
        List<Lab> labList = labService.findAll(pageCurrent, pageSize, lab);
        //总条数
        long Total = labService.findTotal(pageCurrent, pageSize, lab);

        return R.ok(labList,Total);
    }

    //5.查询所有实验室
    @GetMapping("/listAll")
    public R getAllLabs() {
        return R.ok().data(labService.list());
    }
}