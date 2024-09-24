package com.nuc.xnfz.serivce;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuc.xnfz.bean.Class;
import com.nuc.xnfz.bean.Lab;

import java.util.List;


public interface LabService extends IService<Lab> {
    List<Lab> findAll(int beginIndex, int pageSize, Lab lab);//查询符合条件的信息

    int findTotal(int beginIndex,int pageSize,Lab lab);//查询符合条件的总条数

    boolean savelab(Lab lab);//新增lab及对应信息

    boolean updateAllById(Lab lab);//修改所有相关数据
}
