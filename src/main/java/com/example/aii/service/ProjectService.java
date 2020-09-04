package com.example.aii.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.aii.entity.Project;
import com.example.aii.mapper.ProjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class ProjectService {

    @Resource
    private ProjectMapper projectMapper;

    public void save(Project project) {
        projectMapper.insert(project);
    }

    public IPage<Project> findByProjectNameLike(String projectNameLike, Page<Project> page) {
        QueryWrapper<Project> q = new QueryWrapper<>();
        if (!StringUtils.isEmpty(projectNameLike)) {
            q.like("project_name", projectNameLike);
        }
        return projectMapper.selectPage(page, q);
    }

    public void update(Project project) {
        projectMapper.updateById(project);
    }

    public void delete(Long id) {
        projectMapper.deleteById(id);
    }
}
