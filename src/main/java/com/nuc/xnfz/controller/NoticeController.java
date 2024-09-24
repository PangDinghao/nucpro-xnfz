package com.nuc.xnfz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuc.xnfz.bean.Notice;
import com.nuc.xnfz.bean.User;
import com.nuc.xnfz.common.QueryPageParam;
import com.nuc.xnfz.common.R;
import com.nuc.xnfz.serivce.NoticeService;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    /*
    * 1.新增
    * 2.删除
    * 3.修改
    * 4.条件分页查询
    * 5.点击量加1
    * 6.推荐查询
    * */

    //1.新增
    @RequestMapping("/save")
    public R save(@RequestBody Notice notice){
        // 获取当前日期和时间
        LocalDateTime now = LocalDateTime.now();
        // 格式化日期时间
        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(now);

        notice.setCreateDatetime(timestamp);
        notice.setUpdateDatetime(timestamp);

        //标题和内容为空的情况
        if (
                !StringUtils.hasLength(notice.getTitle())||
                        !StringUtils.hasLength(notice.getDetails())){
            return R.fail().message("标题和内容不能为空");
        }

        //标题已存在的情况
        Notice getOneByTitle = noticeService.getOne(
                new QueryWrapper<Notice>()
                        .eq("title", notice.getTitle()));

        //没有上传图片的情况
        if(notice.getCoverImg().isEmpty()|| notice.getCoverImg().equals("")){
            notice.setCoverImg("empty.png");
        }

        if(getOneByTitle!=null){
            //注册失败
            return R.fail().message("标题已存在");
        }else {
            boolean flag = noticeService.save(notice);
            if (flag){
                //注册成功
                return R.ok();
            }else {
                //注册失败
                return R.fail().message("新增公告失败");
            }
        }
    }

    //2.删除
    @GetMapping("/remove")
    public R remove( Integer no){
        boolean flag = noticeService.removeById(no);

        if (flag){
            return R.ok().message("删除成功");
        }else {
            return R.fail().message("删除失败");
        }

    }

    //3.修改
    @RequestMapping("/update")
    public R update(@RequestBody Notice notice){

        // 获取当前日期和时间
        LocalDateTime now = LocalDateTime.now();
        // 格式化日期时间
        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(now);

        notice.setUpdateDatetime(timestamp);
        boolean flag = noticeService.updateById(notice);

        if (flag) {
            return R.ok().message("修改成功");
        } else {
            return R.fail().message("修改失败");
        }

    }

    //4.条件分页查询
    @PostMapping("/listPage")
    // public List<User> listPage(@RequestBody HashMap map){
    public R listPage(@RequestBody QueryPageParam query) throws ParseException {

        Page<Notice> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());


        HashMap param = query.getParam();
        String title = (String) param.get("title");
        String author = (String) param.get("author");
        String Datetime =  (String) param.get("createDatetime");
        String createDatetime = null;

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


        LambdaQueryWrapper<Notice> lambdaQueryWrapper = new LambdaQueryWrapper();

        if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(title)&&!"".equals(title)){
            lambdaQueryWrapper.like(Notice::getTitle,title);
        }
        if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(author)&&!"".equals(author)){
            lambdaQueryWrapper.like(Notice::getAuthor,author);
        }
        if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(createDatetime)&&!"".equals(createDatetime)){
            lambdaQueryWrapper.like(Notice::getCreateDatetime,createDatetime);
        }




        IPage result = noticeService.page(page,lambdaQueryWrapper);

        return R.ok(result.getRecords(), result.getTotal());
    }

    //5.点击量加一
    @PostMapping("hit")
    public R hit(@RequestBody Notice notice){
        notice.setHits(notice.getHits()+1);
        boolean flag = noticeService.updateById(notice);

        if (flag){
            return R.ok().message("点击数新增成功");
        }else {
            return R.fail().message("点击数新增失败");
        }
    }

    //6.查询置顶公告
    @GetMapping("/toplist")
    // public List<User> listPage(@RequestBody HashMap map){
    public R Toplist(){

        LambdaQueryWrapper<Notice> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(Notice::getNoticeType,1);

        return R.ok(noticeService.list(lambdaQueryWrapper));

    }
}
