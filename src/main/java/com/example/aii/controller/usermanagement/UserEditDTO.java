package com.example.aii.controller.usermanagement;

import com.example.aii.entity.User;
import com.google.common.base.Converter;
import org.springframework.beans.BeanUtils;

import javax.annotation.Nonnull;

public class UserEditDTO {

    private String nickname;
    private User.STATUS status;
    private Long[] roleIds;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public User.STATUS getStatus() {
        return status;
    }

    public void setStatus(User.STATUS status) {
        this.status = status;
    }

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    public User getUser() {
        return new UserEditDTOConvert().doForward(this);
    }

    private static class UserEditDTOConvert extends Converter<UserEditDTO, User> {
        @Override
        protected User doForward(@Nonnull UserEditDTO userEditDTO) {
            User user = new User();
            BeanUtils.copyProperties(userEditDTO, user);
            return user;
        }

        @Override
        protected UserEditDTO doBackward(@Nonnull User user) {
            return null;
        }
    }



}
