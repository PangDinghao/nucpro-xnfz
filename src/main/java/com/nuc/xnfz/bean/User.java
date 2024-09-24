package com.nuc.xnfz.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 3084软件科技
 *  用户类
 */
@Data
public class User {
    @TableId(value = "userid", type = IdType.AUTO)
    private int userid;
    private String username;
    private String account;
    private String password;
    private String email;
    private String phone;
    private String sex;
    private Integer classid;//班级主键

    //用户对应的角色信息
    @TableField(exist = false)
    private List<Role> roleList;

    @TableField(exist = false)
    private List<Course> courseList;
    @TableField(exist = false)
    private double grade;//根据课程查看学生的成绩
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")//时区加8
    @TableField("create_datetime")
    private Date createDatetime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("update_datetime")
    private Date updateDatetime;

    @TableField("update_user")
    private String updateUser;

}
