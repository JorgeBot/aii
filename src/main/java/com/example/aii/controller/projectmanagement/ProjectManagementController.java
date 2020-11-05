package com.example.aii.controller.projectmanagement;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.aii.controller.projectmanagement.dto.ModuleDTO;
import com.example.aii.controller.projectmanagement.dto.ProjectDTO;
import com.example.aii.controller.projectmanagement.dto.ProjectUserRelationDTO;
import com.example.aii.entity.Module;
import com.example.aii.entity.Project;
import com.example.aii.model.ProjectRelatedUser;
import com.example.aii.pojo.Tree;
import com.example.aii.service.impl.ModuleService;
import com.example.aii.service.impl.ProjectService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiresPermissions("项目管理")
public class ProjectManagementController {

    @Resource
    private ProjectService projectService;

    @Resource
    private ModuleService moduleService;

    @PostMapping("/project")
    @RequiresPermissions("新建项目")
    public void postProject(ProjectDTO projectDTO) {
        Project project = projectDTO.toProject();
        projectService.saveThisAndModule(project);
    }

    @GetMapping("/projectPage")
    public IPage<ProjectRelatedUser> getProjectPage(@RequestParam(required = false) String projectNameLike,
                                                    Page<Project> page) {
        return projectService.findByProjectNameLike(projectNameLike, page);
    }

    @GetMapping("/projects")
    public List<Project> getProjects() {
        return projectService.list();
    }

    @PatchMapping("/project/{id}")
    @RequiresPermissions("项目操作")
    public void patchProject(@PathVariable("id") Long id, ProjectDTO projectDTO) {
        Project project = projectDTO.toProject();
        project.setId(id);
        projectService.updateById(project);
    }

    @PatchMapping("/project/{projectId}/user/relation")
    @RequiresPermissions("关联人员")
    public void patchProjectUserRelation(@PathVariable("projectId") Long id, ProjectUserRelationDTO dto) {
        ProjectRelatedUser projectRelatedUser = dto.toProjectRelatedUser();
        projectRelatedUser.setId(id);
        projectService.updateProjectRelatedUser(projectRelatedUser);
    }

    @DeleteMapping("/project/{id}")
    @RequiresPermissions("项目操作")
    public void deleteProject(@PathVariable("id") Long id) {
        projectService.removeById(id);
    }

    @GetMapping("/project/{projectId}/modulesTree")
    public List<Tree<Object>> getModulesTreeByProjectId(@PathVariable Long projectId) {
        List<Module> moduleList = moduleService.findByProjectId(projectId);
        List<Tree<Object>> moduleTree = moduleList.stream().map(e -> new Tree<>(e.getId(), e.getParentId(), e.getModuleName()))
                .collect(Collectors.toList());
        return Tree.nesting(moduleTree);
    }

    @PostMapping("/project/module")
    @RequiresPermissions("模块操作")
    public void createModule(ModuleDTO moduleDTO) {
        moduleService.save(moduleDTO.toModule());
    }

    @PatchMapping("/project/module")
    @RequiresPermissions("模块操作")
    public void editModule(ModuleDTO moduleDTO) {
        moduleService.updateById(moduleDTO.toModule());
    }
}
