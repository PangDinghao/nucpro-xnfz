package com.nuc.xnfz.serivce.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuc.xnfz.bean.Notice;
import com.nuc.xnfz.mapper.NoticeMapper;
import com.nuc.xnfz.serivce.NoticeService;
import org.springframework.stereotype.Service;


@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
}

