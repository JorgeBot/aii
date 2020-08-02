package com.example.aii.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@TableName(autoResultMap = true)
public class User extends BaseEntity {

    public enum SEX {男, 女}

    public enum STATUS {正常, 禁用}

    private String username;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String salt;
    private String nickname;
    private SEX sex;
    private STATUS status;
    private LocalDateTime lastLoginDatetime;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Long[] roleIds;

    public User() {
    }

    public User(String username, String password, String salt, String nickname, SEX sex, STATUS status, LocalDateTime lastLoginDatetime) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.nickname = nickname;
        this.sex = sex;
        this.status = status;
        this.lastLoginDatetime = lastLoginDatetime;
    }

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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public SEX getSex() {
        return sex;
    }

    public void setSex(SEX sex) {
        this.sex = sex;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public LocalDateTime getLastLoginDatetime() {
        return lastLoginDatetime;
    }

    public void setLastLoginDatetime(LocalDateTime lastLoginDatetime) {
        this.lastLoginDatetime = lastLoginDatetime;
    }

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }
}
