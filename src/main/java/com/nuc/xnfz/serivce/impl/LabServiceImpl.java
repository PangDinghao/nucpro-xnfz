package com.nuc.xnfz.serivce.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuc.xnfz.bean.*;
import com.nuc.xnfz.mapper.LabEquipmentMapper;
import com.nuc.xnfz.mapper.LabMapper;
import com.nuc.xnfz.serivce.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LabServiceImpl extends ServiceImpl<LabMapper, Lab> implements LabService {
    @Autowired
    LabMapper labMapper;
    @Autowired
    LabEquipmentMapper labEquipmentMapper;

    @Override
    public List<Lab> findAll(int beginIndex, int pageSize, Lab lab) {
        return labMapper.findAll(beginIndex,pageSize,lab);
    }

    @Override
    public int findTotal(int beginIndex, int pageSize, Lab lab) {
        return labMapper.findTotal(beginIndex,pageSize,lab);
    }

    @Override
    public boolean savelab(Lab lab) {
        List<Equipment> equipmentList = lab.getEquipmentList();
        LabEquipment le = new LabEquipment();

        int insert = labMapper.insert(lab);

        for (Equipment equipment : equipmentList) {
            le.setLabid(lab.getLabid());
            le.setEquipmentid(equipment.getEquipmentid());
            le.setId(0);
            int insert1 = labEquipmentMapper.insert(le);
            if (insert1==0){
                return false;
            }
        }


        if (insert==0){
            return false;
        }
        return true;
    }

    @Override
    public boolean updateAllById(Lab lab) {

        List<Equipment> equipmentList = lab.getEquipmentList();
        int labid = lab.getLabid();

        List<Integer> labequipmentid = labEquipmentMapper.getIdBylabid(labid);

        LabEquipment labEquipment = new LabEquipment();

        for (int i :labequipmentid){
            labEquipmentMapper.deleteById(i);
        }

        for (Equipment equipment : equipmentList) {
            labEquipment.setId(0);
            labEquipment.setEquipmentid(equipment.getEquipmentid());
            labEquipment.setLabid(labid);

            int insert = labEquipmentMapper.insert(labEquipment);
            if (insert==0){
                return false;
            }
        }

        int update = labMapper.updateById(lab);
        if (update==0){
            return false;
        }
        return true;

    }
}

