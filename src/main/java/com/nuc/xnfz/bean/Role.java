package com.nuc.xnfz.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

/**
 * @author 3084软件科技
 *  用户类型（角色）类
 */
@Data
public class Role {
    @TableId(value = "roleid", type = IdType.AUTO)
    private int roleid;
    private String rolename;
    //角色对应的用户 信息
    private List<User> userList;
}
