package com.nuc.xnfz.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
/**
 * @author:3084软件科技
 * 新闻公告类
* */

@Data
public class Notice {
    @TableId(value = "no", type = IdType.AUTO)
    private Integer no;
    private String title;
    private String author;
    private Integer hits;
    private String details;
    @TableField("cover_img")
    private String coverImg;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")//时区+8
    @TableField("create_datetime")
    private Date createDatetime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("update_datetime")
    private Date updateDatetime;
    @TableField("update_user")
    private String updateUser;
    @TableField("notice_type")
    private Integer noticeType;
}
