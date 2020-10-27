package com.example.aii.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.aii.entity.HttpInterface;
import com.example.aii.mapper.HttpInterfaceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HttpInterfaceService extends ServiceImpl<HttpInterfaceMapper, HttpInterface> implements IService<HttpInterface> {

    @Resource
    private HttpInterfaceMapper httpInterfaceMapper;

    public IPage<HttpInterface> findByNameLikeAndModuleId(String queryLike, Long moduleId, Page<HttpInterface> page) {
        return httpInterfaceMapper.selectByNameLikeAndModuleId(page, queryLike, moduleId);
    }
}
