package com.nuc.xnfz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuc.xnfz.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 53595
 * 表user的数据库操作
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    User findById(Integer userid);
    List<User> findUserByRoleid(Integer roleid);//查询角色对应的用户
    int removeClassid(Integer classid);//将classid对应的用户的classid都置0
    int getStuNumByClassid(Integer classid);//根据classid查询人数
    List<User> getStuByClassid(Integer classid);//根据classid查询班级成员

    List<User> findAll(int beginIndex,int pageSize,User user,Integer roleid,String createDatetime);//查询符合条件的信息

    int findTotal(int beginIndex,int pageSize,User user,Integer roleid,String createDatetime);//查询符合条件的总条数
}
