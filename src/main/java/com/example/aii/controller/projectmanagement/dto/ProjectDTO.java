package com.example.aii.controller.projectmanagement.dto;

import com.example.aii.entity.Project;
import com.google.common.base.Converter;
import org.springframework.beans.BeanUtils;

import javax.annotation.Nonnull;

public class ProjectDTO {
    private String projectName;
    private Project.STATUS status;
    private String description;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Project.STATUS getStatus() {
        return status;
    }

    public void setStatus(Project.STATUS status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Project toProject() {
        return new Convert().doForward(this);
    }

    private static class Convert extends Converter<ProjectDTO, Project> {
        @Override
        protected Project doForward(@Nonnull ProjectDTO projectDTO) {
            Project project = new Project();
            BeanUtils.copyProperties(projectDTO, project);
            return project;
        }

        @Override
        protected ProjectDTO doBackward(@Nonnull Project project) {
            return null;
        }
    }
}
