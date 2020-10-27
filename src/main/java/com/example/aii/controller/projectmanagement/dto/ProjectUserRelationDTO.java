package com.example.aii.controller.projectmanagement.dto;

import com.example.aii.model.ProjectRelatedUser;
import com.google.common.base.Converter;
import org.springframework.beans.BeanUtils;

import javax.annotation.Nonnull;
import java.util.List;

public class ProjectUserRelationDTO {
    private List<Long> relatedUserIdArray;

    public List<Long> getRelatedUserIdArray() {
        return relatedUserIdArray;
    }

    public void setRelatedUserIdArray(List<Long> relatedUserIdArray) {
        this.relatedUserIdArray = relatedUserIdArray;
    }

    public ProjectRelatedUser toProjectRelatedUser() {
        return new Convert().doForward(this);
    }

    private static class Convert extends Converter<ProjectUserRelationDTO, ProjectRelatedUser> {
        @Override
        protected ProjectRelatedUser doForward(@Nonnull ProjectUserRelationDTO projectUserRelationDTO) {
            ProjectRelatedUser projectRelatedUser = new ProjectRelatedUser();
            BeanUtils.copyProperties(projectUserRelationDTO, projectRelatedUser);
            return projectRelatedUser;
        }

        @Override
        protected ProjectUserRelationDTO doBackward(@Nonnull ProjectRelatedUser projectRelatedUser) {
            return null;
        }
    }
}
