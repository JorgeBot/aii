package com.example.aii.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.aii.entity.Module;
import com.example.aii.mapper.ModuleMapper;
import com.example.aii.pojo.Tree;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ModuleService extends ServiceImpl<ModuleMapper, Module> implements IService<Module> {

    @Resource
    private ModuleMapper moduleMapper;

    public List<Module> findByProjectId(Long projectId) {
        LambdaQueryWrapper<Module> q = Wrappers.lambdaQuery();
        q.eq(Module::getProjectId, projectId);
        return list(q);
    }

    public List<Tree<Object>> findAll2TreeByUserId(Long userId) {
        List<Module> moduleLs;
        if (userId != null) {
            moduleLs = findByUserId(userId);
        } else {
            moduleLs = list();
        }
        List<Tree<Object>> res = new ArrayList<>();
        moduleLs.forEach(e -> {
            Tree<Object> moduleTree = new Tree<>(e.getId(), e.getParentId(), e.getModuleName());
            res.add(moduleTree);
        });
        return Tree.nesting(res);
    }

    public List<Module> findByUserId(Long userId) {
        return moduleMapper.selectByUserId(userId);
    }
}
