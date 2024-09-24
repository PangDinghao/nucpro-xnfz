package com.nuc.xnfz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuc.xnfz.bean.Role;
import com.nuc.xnfz.bean.User;
import com.nuc.xnfz.bean.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    int getIdByRoleUserid(int userid,int roleid);//根据roleid和userid查询主键

    List<Integer> getIdByUserid(int userid);//根据userid查询所有符合的主键
    List<User> getUsersByRoleId(Integer roleid);

    List<Role> getRolesByUserId(Integer userid);
}
