package com.example.aii.controller.projectmanagement;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.aii.entity.Project;
import com.example.aii.service.ProjectService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequiresPermissions("项目管理")
public class ProjectManagementController {

    @Resource
    private ProjectService projectService;

    @PostMapping("/project")
    @RequiresPermissions("新建项目")
    public void postProject(ProjectDTO projectDTO) {
        Project project = projectDTO.toProject(projectDTO);
        projectService.save(project);
    }

    @GetMapping("/projectPage")
    public IPage<Project> getProjectPage(@RequestParam(required = false) String projectNameLike,
                                         Page<Project> page) {
        return projectService.findByProjectNameLike(projectNameLike, page);
    }

    @PatchMapping("/project/{id}")
    @RequiresPermissions("编辑项目")
    public void patchProject(@PathVariable("id") Long id, ProjectDTO projectDTO) {
        Project project = projectDTO.toProject(projectDTO);
        project.setId(id);
        projectService.update(project);
    }

    @DeleteMapping("/project/{id}")
    @RequiresPermissions("删除项目")
    public void deleteProject(@PathVariable("id") Long id) {
        projectService.delete(id);
    }

//    @GetMapping("/project/{projectId}/relationProjectUser")
//    public Map<String, List<String>> getRelationProjectUserByProjectId(@PathVariable("projectId") Long projectId) {
//
//    }
}
