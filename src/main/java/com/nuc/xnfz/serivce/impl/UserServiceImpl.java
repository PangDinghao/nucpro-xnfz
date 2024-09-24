package com.nuc.xnfz.serivce.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuc.xnfz.bean.Role;
import com.nuc.xnfz.bean.StudentCourse;
import com.nuc.xnfz.bean.User;
import com.nuc.xnfz.bean.UserRole;
import com.nuc.xnfz.mapper.StudentCourseMapper;
import com.nuc.xnfz.mapper.UserMapper;
import com.nuc.xnfz.mapper.UserRoleMapper;
import com.nuc.xnfz.serivce.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    StudentCourseMapper studentCourseMapper;

    @Override
    public User findById(Integer userid) {
        return userMapper.findById(userid);
    }

    @Override
    public boolean removeUserById(int userid) {//删除用户所有信息
        User user = userMapper.findById(userid);

        List<Role> roleList = user.getRoleList();

        if (roleList!=null){//如果用户类型不为空
            for (Role role : roleList) {
                int id = userRoleMapper.getIdByRoleUserid(userid,role.getRoleid());

                int i1 = userRoleMapper.deleteById(id);
                if (i1==0){
                    return false;
                }
            }
        }

        int i = userMapper.deleteById(userid);
        if (i==0){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public boolean save(User user) {

        int i = userMapper.insert(user);
        //获取到user的主键
        int userid = user.getUserid();
        List<Role> roleList = user.getRoleList();
        UserRole userRole = new UserRole();
        //如果用户类型不为空
        if (roleList!=null){
            for (Role role : roleList) {
                userRole.setRoleid(role.getRoleid());
                userRole.setUserid(userid);
                userRole.setId(0);
                int i1 = userRoleMapper.insert(userRole);
                if (i1==0){
                    return false;
                }
            }
        }
        if (i!=0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<User> findAll(int beginIndex,int pageSize,User user,Integer roleid,String createDatetime) {
        List<User> userList = userMapper.findAll(beginIndex,pageSize,user,roleid,createDatetime);
        return userList;
    }

    @Override
    public List<User> findUserByRoleid(Integer roleid) {
        List<User> userByRoleid = userMapper.findUserByRoleid(roleid);
        return userByRoleid;
    }

    @Override
    public int getStuNumByClassid(Integer classid) {
        int stuNumByClassid = userMapper.getStuNumByClassid(classid);
        return stuNumByClassid;
    }

    @Override
    public int updateClassidByUserid(Integer userid, Integer classid) {

        User user = userMapper.findById(userid);
        user.setClassid(classid);
        int i = userMapper.updateById(user);

        return i;
    }

    @Override
    public boolean updateUserById(User user) {
        int i = userMapper.updateById(user);
        List<Role> roleList = user.getRoleList();

        List<Integer> role_userid = userRoleMapper.getIdByUserid(user.getUserid());
        UserRole userRole = new UserRole();


        //删去该用户的所有角色对应关系
        for (Integer id : role_userid){
            userRoleMapper.deleteById(id);
        }

        if (roleList!=null){
            for (Role role : roleList) {
                userRole.setRoleid(role.getRoleid());
                userRole.setUserid(user.getUserid());
                userRole.setId(0);
                int i1 = userRoleMapper.insert(userRole);
                if (i1==0){
                    return false;
                }
            }
        }

        if (i!=0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public int findTotal(int beginIndex,int pageSize,User user,Integer roleid,String createDatetime) {
        int total = userMapper.findTotal(beginIndex,pageSize,user,roleid,createDatetime);
        return total;
    }

    @Override
    public List<User> getStudentPagelistByCourseId(int beginIndex, int pageSize, int courseid) {
        return studentCourseMapper.getStudentPagelistByCourseId(beginIndex,pageSize,courseid);
    }

    @Override
    public int getStudentTotalByCourseId(Integer courseid) {
        return studentCourseMapper.getStudentTotalByCourseId(courseid);
    }
}

