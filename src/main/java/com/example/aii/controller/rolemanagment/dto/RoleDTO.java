package com.example.aii.controller.rolemanagment.dto;

import com.example.aii.entity.Role;
import com.example.aii.shiro.PermissionDirectory;
import com.google.common.base.Converter;
import org.springframework.beans.BeanUtils;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RoleDTO {
    private Long id;
    private String roleName;
    private String[] menus;
    private String[] resources;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String[] getMenus() {
        return menus;
    }

    public void setMenus(String[] menus) {
        this.menus = menus;
    }

    public String[] getResources() {
        return resources;
    }

    public void setResources(String[] resources) {
        this.resources = resources;
    }

    public Role toRole() {
        return new RoleConvert().doForward(this);
    }

    private static class RoleConvert extends Converter<RoleDTO, Role> {

        @Override
        protected Role doForward(@Nonnull RoleDTO roleDTO) {
            Role role = new Role();
            BeanUtils.copyProperties(roleDTO, role);
            if (roleDTO.getResources() != null) {
                role.setResources(new HashSet<>(Arrays.asList(roleDTO.getResources())));
            }
            if (roleDTO.getMenus() != null) {
                Set<String> menuSet = new HashSet<>(Arrays.asList(roleDTO.getMenus()));
                menuSet.retainAll(PermissionDirectory.getPermission().keySet());
                role.setMenus(menuSet);
            }
            return role;
        }

        @Override
        protected RoleDTO doBackward(@Nonnull Role role) {
            return null;
        }
    }
}
