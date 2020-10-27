package com.example.aii.controller.usermanagement.dto;

import com.example.aii.entity.User;
import com.google.common.base.Converter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RelatedUserRolesDTO {
    private Long[] userIds;
    private Long[] roleIds;

    public Long[] getUserIds() {
        return userIds;
    }

    public void setUserIds(Long[] userIds) {
        this.userIds = userIds;
    }

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    public List<User> toUserLs() {
        return new RelationUserRoleConvert().doForward(this);
    }

    private static class RelationUserRoleConvert extends Converter<RelatedUserRolesDTO, List<User>> {
        @Override
        protected List<User> doForward(@Nonnull RelatedUserRolesDTO relatedUserRolesDTO) {
            List<User> userLs = new ArrayList<>();
            Arrays.stream(relatedUserRolesDTO.getUserIds()).forEach(user -> {
                User u = new User();
                u.setId(user);
                u.setRoleIds(relatedUserRolesDTO.getRoleIds());
                userLs.add(u);
            });
            return userLs;
        }

        @Override
        protected RelatedUserRolesDTO doBackward(@Nonnull List<User> userLs) {
            return null;
        }
    }
}
