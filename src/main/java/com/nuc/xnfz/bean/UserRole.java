package com.nuc.xnfz.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author 3084软件科技
 *  用户与角色关联类
 */
@Data
public class UserRole {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private Integer userid;
    private Integer roleid;
}
