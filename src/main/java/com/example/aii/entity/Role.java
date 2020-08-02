package com.example.aii.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import java.util.Set;

@TableName(autoResultMap = true)
public class Role extends BaseEntity {

    private String roleName;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<String> menus;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<String> resources;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<String> getResources() {
        return resources;
    }

    public void setResources(Set<String> resources) {
        this.resources = resources;
    }

    public Set<String> getMenus() {
        return menus;
    }

    public void setMenus(Set<String> menus) {
        this.menus = menus;
    }
}
