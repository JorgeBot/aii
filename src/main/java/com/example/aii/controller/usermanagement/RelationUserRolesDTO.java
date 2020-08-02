package com.example.aii.controller.usermanagement;

import com.example.aii.entity.User;
import com.google.common.base.Converter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RelationUserRolesDTO {
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

    public User[] toUsers() {
        return new RelationUserRoleConvert().doForward(this);
    }

    private static class RelationUserRoleConvert extends Converter<RelationUserRolesDTO, User[]> {
        @Override
        protected User[] doForward(@Nonnull RelationUserRolesDTO relationUserRolesDTO) {
            List<User> userLs = new ArrayList<>();
            Arrays.stream(relationUserRolesDTO.getUserIds()).forEach(user -> {
                User u = new User();
                u.setId(user);
                u.setRoleIds(relationUserRolesDTO.getRoleIds());
                userLs.add(u);
            });
            return userLs.toArray(new User[0]);
        }

        @Override
        protected RelationUserRolesDTO doBackward(@Nonnull User[] users) {
            return null;
        }
    }
}
