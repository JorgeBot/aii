package com.example.aii.controller.login;

import com.google.common.base.Converter;
import org.apache.commons.beanutils.BeanUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class UserInformationVO {

    private String username;
    private String nickname;
    private LocalDateTime lastLoginDatetime;
    private Set<String> menus;
    private Set<String> resources;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalDateTime getLastLoginDatetime() {
        return lastLoginDatetime;
    }

    public void setLastLoginDatetime(LocalDateTime lastLoginDatetime) {
        this.lastLoginDatetime = lastLoginDatetime;
    }

    public Set<String> getMenus() {
        return menus;
    }

    public void setMenus(Set<String> menus) {
        this.menus = menus;
    }

    public Set<String> getResources() {
        return resources;
    }

    public void setResources(Set<String> resources) {
        this.resources = resources;
    }

    public UserInformationVO fromMap(Map<String, Object> mp) {
        return new Convert().doBackward(mp);
    }

    private static class Convert extends Converter<UserInformationVO, Map<String, Object>> {
        @Override
        protected Map<String, Object> doForward(@Nonnull UserInformationVO userInformationVO) {
            return null;
        }

        @Override
        protected UserInformationVO doBackward(@Nonnull Map<String, Object> stringObjectMap) {
            UserInformationVO vo = new UserInformationVO();
            try {
                BeanUtils.populate(vo, stringObjectMap);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return vo;
        }
    }

}
