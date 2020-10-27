package com.example.aii.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.aii.entity.Module;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module> {

    List<Module> selectByUserId(Long userId);
}
