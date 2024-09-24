package com.nuc.xnfz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuc.xnfz.bean.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    Role findById(Integer roleid);

}
