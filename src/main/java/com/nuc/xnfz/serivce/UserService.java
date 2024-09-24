package com.nuc.xnfz.serivce;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuc.xnfz.bean.User;

import java.util.List;

public interface UserService extends IService<User> {
    User findById(Integer userid);
    boolean removeUserById(int userid);//删除用户及对应信息
    boolean save(User user);//保存注册/新增用户及对应信息
    List<User> findAll(int beginIndex,int pageSize,User user,Integer roleid,String createDatetime);//分页条件查询

    List<User> findUserByRoleid(Integer roleid);//查询角色对应的用户
    int getStuNumByClassid(Integer classid);//根据classid查询人数

    int updateClassidByUserid(Integer userid,Integer classid);//根据用户id更新班级

    boolean updateUserById(User user);//更新用户以及对应信息

    int findTotal(int beginIndex,int pageSize,User user,Integer roleid,String createDatetime);//查询数据总条数(用于分页)

    List<User> getStudentPagelistByCourseId(int beginIndex,int pageSize,int courseid);//根据courseid分页查询到学生信息

    int getStudentTotalByCourseId(Integer courseid);
}
