package com.nuc.xnfz;

import com.nuc.xnfz.bean.Course;
import com.nuc.xnfz.bean.Role;
import com.nuc.xnfz.bean.User;
import com.nuc.xnfz.bean.UserRole;
import com.nuc.xnfz.mapper.RoleMapper;
import com.nuc.xnfz.mapper.UserMapper;
import com.nuc.xnfz.mapper.UserRoleMapper;
import com.nuc.xnfz.serivce.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class UserRoleTests {

    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    UserService userService;
//     根据一个用户ID,查看这个用户所对应的所有角色
    @Test
    void getRolesByStuId() {

        List<Role> rolesByStuId = userRoleMapper.getRolesByUserId(7);
        System.out.println(rolesByStuId);
    }
//    <!-- 根据一个角色ID,查看这个角色所对应的所有用户-->
    @Test
    void getUsersByRoleId() {
        List<User> usersByUserId = userRoleMapper.getUsersByRoleId(2);

        System.out.println(usersByUserId);


    }


    //    <!-- 根据用户id,查看这个用户的信息以及所对应的所有角色-->
    @Test
    void findUserById() {
        User user = userMapper.findById(5);
        System.out.println(user);
        List<Course> courseList = user.getCourseList();
        for (Course course:courseList){
            System.out.println(course);

        }

    }
    //    <!-- 根据角色id,查看这个角色的信息以及所对应的所有用户-->
    @Test
    void findRoleById() {
        Role role = roleMapper.findById(2);
        System.out.println(role);
    }

    //查询所有user
    @Test
    void findAll() {
        User user = new User();
////        List<User> userList = userMapper.findAll(0,4,user,1);
//
//        for (User users : userList) {
//            System.out.println("User: " + users);
//            // 输出其他用户信息...
//            System.out.println("-----------------------");
//        }

        List<Integer> idByUserid = userRoleMapper.getIdByUserid(41);
        System.out.println(idByUserid);


    }
}
