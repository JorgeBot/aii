package com.example.aii.controller.projectmanagement.dto;

import com.example.aii.entity.Module;
import com.google.common.base.Converter;
import org.springframework.beans.BeanUtils;

import javax.annotation.Nonnull;

public class ModuleDTO {
    private Long id;
    private String moduleName;
    private Long parentId;
    private Long projectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Module toModule() {
        return new Convert().doForward(this);
    }

    private static class Convert extends Converter<ModuleDTO, Module> {
        @Override
        protected Module doForward(@Nonnull ModuleDTO moduleDTO) {
            Module res = new Module();
            BeanUtils.copyProperties(moduleDTO, res);
            return res;
        }

        @Override
        protected ModuleDTO doBackward(@Nonnull Module module) {
            return null;
        }
    }
}
