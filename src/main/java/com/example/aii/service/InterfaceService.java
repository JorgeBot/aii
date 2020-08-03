package com.example.aii.service;

import com.example.aii.entity.Interface;
import com.example.aii.mapper.InterfaceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class InterfaceService {

    @Resource
    private InterfaceMapper interfaceMapper;

    public void save(Interface in) {
        interfaceMapper.insert(in);
    }
}
