package com.example.aii.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.aii.entity.RelationalUserProject;
import com.example.aii.mapper.RelationalUserProjectMapper;
import com.example.aii.service.IService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RelationalUserProjectService extends ServiceImpl<RelationalUserProjectMapper, RelationalUserProject> implements IService<RelationalUserProject> {

    public void removeByProjectId(Long id) {
        remove(new LambdaQueryWrapper<RelationalUserProject>().eq(RelationalUserProject::getProjectId, id));
    }

    public List<RelationalUserProject> getByUserId(Long id) {
        LambdaQueryWrapper<RelationalUserProject> q = new LambdaQueryWrapper<>();
        q.eq(RelationalUserProject::getUserId, id);
        return list(q);
    }

    public void removeByUserId(Long userId) {
        LambdaQueryWrapper<RelationalUserProject> q = new LambdaQueryWrapper<>();
        q.eq(RelationalUserProject::getUserId, userId);
        remove(q);
    }

    @Transactional(rollbackFor = Exception.class)
    public void reRelatedUser2Project(Long userId, List<Long> projectIds) {
        removeByUserId(userId);
        List<RelationalUserProject> rLs = new ArrayList<>();
        projectIds.forEach(e -> {
            RelationalUserProject r = new RelationalUserProject();
            r.setUserId(userId);
            r.setProjectId(e);
            rLs.add(r);
        });
        saveBatch(rLs);
    }

    @Transactional(rollbackFor = Exception.class)
    public void reRelatedProject2User(Long projectId, List<Long> userIds) {
        removeByProjectId(projectId);
        List<RelationalUserProject> rLs = new ArrayList<>();
        userIds.forEach(e -> {
            RelationalUserProject r = new RelationalUserProject();
            r.setProjectId(projectId);
            r.setUserId(e);
            rLs.add(r);
        });
        saveBatch(rLs);
    }
}
