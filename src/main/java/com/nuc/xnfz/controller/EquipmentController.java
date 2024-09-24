package com.nuc.xnfz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuc.xnfz.bean.Equipment;
import com.nuc.xnfz.common.QueryPageParam;
import com.nuc.xnfz.common.R;
import com.nuc.xnfz.serivce.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    //新增
    @RequestMapping("/save")
    public R save(@RequestBody Equipment equipment) {
        // 判断设备名称和详情是否为空
        if (!StringUtils.hasLength(equipment.getEquipmentName()) ||
                !StringUtils.hasLength(equipment.getEquipmentDetial())) {
            return R.fail().message("设备名称和详情不能为空");
        }
        // 判断设备ID是否已存在
        Equipment getOneById = equipmentService.getOne(
                new QueryWrapper<Equipment>()
                        .eq("equipmentid", equipment.getEquipmentid()));
        if (getOneById!= null) {
            return R.fail().message("设备ID已存在");
        } else {
            boolean flag = equipmentService.save(equipment);
            if (flag) {
                return R.ok();
            } else {
                return R.fail().message("添加设备失败");
            }
        }
    }

    //2.删除
    @GetMapping("/remove")
    public R remove( Integer equipmentid){
        boolean flag = equipmentService.removeById(equipmentid);
        if (flag){
            return R.ok().message("删除成功");
        }else {
            return R.fail().message("删除失败");
        }
    }

    //3.修改
    @RequestMapping("/update")
    public R update(@RequestBody Equipment equipment){
        boolean flag = equipmentService.updateById(equipment);
        if (flag) {
            return R.ok().message("修改成功");
        } else {
            return R.fail().message("修改失败");
        }
    }

    //4.条件分页查询
    @PostMapping("/listPage")
    public R listPage(@RequestBody QueryPageParam query) throws ParseException {

        Page<Equipment> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        HashMap param = query.getParam();
        String equipmentName = (String) param.get("equipmentName");
        String equipmentCompany = (String) param.get("equipmentCompany");

        LambdaQueryWrapper<Equipment> lambdaQueryWrapper = new LambdaQueryWrapper();

        if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(equipmentName)&&!"".equals(equipmentName)){
            lambdaQueryWrapper.like(Equipment::getEquipmentName,equipmentName);
        }
        if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(equipmentCompany)&&!"".equals(equipmentCompany)){
            lambdaQueryWrapper.like(Equipment::getEquipmentCompany,equipmentCompany);
        }

        IPage result = equipmentService.page(page,lambdaQueryWrapper);

        return R.ok(result.getRecords(), result.getTotal());
    }

    //5.获取到所有的设备
    @GetMapping("ListAll")
    public R listAll(){
        List<Equipment> list = equipmentService.list();
        return R.ok().data(list);
    }

}