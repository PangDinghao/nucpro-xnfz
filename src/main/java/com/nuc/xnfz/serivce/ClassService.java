package com.nuc.xnfz.serivce;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuc.xnfz.bean.Class;
import com.nuc.xnfz.bean.Role;

public interface ClassService extends IService<Class> {
    boolean removeAllByid(Integer classid);//根据classid删除班级信息以及将班级成员的classid值置0

    int addClassByplanid(Integer planid, Integer classid1);//将班级添加到实训计划中，并且将班级所有成员和计划包含的课程都关联（存入stu_course表中）

    int removeClassByplanid(Integer planid, Integer classid1);
}
