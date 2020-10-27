package com.example.aii.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.aii.entity.Module;
import com.example.aii.entity.Project;
import com.example.aii.entity.RelationalUserProject;
import com.example.aii.model.ProjectRelatedUser;
import com.example.aii.mapper.ProjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService extends ServiceImpl<ProjectMapper, Project> implements IService<Project> {

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ModuleService moduleService;

    @Resource
    private RelationalUserProjectService relationalUserProjectService;

    public IPage<ProjectRelatedUser> findByProjectNameLike(String projectNameLike, Page<Project> page) {
        return projectMapper.findByProjectNameLike(page, projectNameLike);
    }

    public void saveThisAndModule(Project project) {
        save(project);
        Module module = new Module();
        module.setModuleName(project.getProjectName());
        module.setProjectId(project.getId());
        moduleService.save(module);
    }

    public void updateProjectRelatedUser(ProjectRelatedUser projectRelatedUser) {
        List<Long> userIds = projectRelatedUser.getRelatedUserIdArray().stream()
                .map(Number::longValue).collect(Collectors.toList());
        relationalUserProjectService.reRelatedProject2User(projectRelatedUser.getId(), userIds);
    }
}
