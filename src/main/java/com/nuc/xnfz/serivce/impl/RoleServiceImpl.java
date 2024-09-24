package com.nuc.xnfz.serivce.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuc.xnfz.bean.Notice;
import com.nuc.xnfz.bean.Role;
import com.nuc.xnfz.mapper.NoticeMapper;
import com.nuc.xnfz.mapper.RoleMapper;
import com.nuc.xnfz.serivce.NoticeService;
import com.nuc.xnfz.serivce.RoleService;
import org.springframework.stereotype.Service;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}

