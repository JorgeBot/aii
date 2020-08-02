package com.example.aii.controller.usermanagement;

import com.example.aii.entity.User;
import com.google.common.base.Converter;
import org.springframework.beans.BeanUtils;

public class UserDTO {

    private String username;
    private String password;
    private String nickname;
    private User.SEX sex;
    private User.STATUS status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public User.SEX getSex() {
        return sex;
    }

    public void setSex(User.SEX sex) {
        this.sex = sex;
    }

    public User.STATUS getStatus() {
        return status;
    }

    public void setStatus(User.STATUS status) {
        this.status = status;
    }

    public User toUser() {
        return new UserDTOConvert().doForward(this);
    }

    private static class UserDTOConvert extends Converter<UserDTO, User> {

        @Override
        protected User doForward(UserDTO userDTO) {
            User user = new User();
            BeanUtils.copyProperties(userDTO, user);
            return user;
        }

        @Override
        protected UserDTO doBackward(User user) {
            throw new AssertionError("不支持逆向转化方法!");
        }
    }
}
