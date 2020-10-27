package com.example.aii.controller.usermanagement.dto;

import com.example.aii.entity.User;
import com.google.common.base.Converter;
import org.springframework.beans.BeanUtils;

import javax.annotation.Nonnull;
import java.util.List;

public class UserEditDTO {

    private String nickname;
    private User.STATUS status;
    private Long[] roleIds;
    private List<Number> relatedProjectIdArray;

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

    public List<Number> getRelatedProjectIdArray() {
        return relatedProjectIdArray;
    }

    public void setRelatedProjectIdArray(List<Number> relatedProjectIdArray) {
        this.relatedProjectIdArray = relatedProjectIdArray;
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
