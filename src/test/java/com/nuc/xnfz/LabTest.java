package com.nuc.xnfz;

import com.nuc.xnfz.bean.Equipment;
import com.nuc.xnfz.bean.Lab;
import com.nuc.xnfz.bean.LabEquipment;
import com.nuc.xnfz.mapper.LabEquipmentMapper;
import com.nuc.xnfz.mapper.LabMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class LabTest {
    @Autowired
    LabEquipmentMapper labEquipmentMapper;

    @Autowired
    LabMapper labMapper;

    @Test
    void test1(){
        List<Equipment> equipmentByLabId = labEquipmentMapper.getEquipmentByLabId(1);
        System.out.println(equipmentByLabId);
    }

    @Test
    void test2(){
        Lab byId = labMapper.findById(1);
        System.out.println(byId);

    }
}
