package com.example.aii.service;

import com.example.aii.entity.Role;
import com.example.aii.entity.User;
import com.example.aii.shiro.LoginUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class LoginService {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    public Map<String, Object> login(User user, boolean rememberMe) {
        Map<String, Object> resMp = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword(), rememberMe);
        subject.login(token);
        User loginUser = LoginUtils.getLoginUser();
        // 更新最后更新时间
        loginUser.setLastLoginDatetime(LocalDateTime.now());
        userService.updateUser(loginUser);
        // 获取用户角色信息
        List<Role> roleSet = roleService.findByUserId(loginUser.getId());
        Set<String> menuSet = new HashSet<>();
        Set<String> resourceSet = new HashSet<>();
        roleSet.forEach(e -> {
            if (e.getMenus() != null) menuSet.addAll(e.getMenus());
            if (e.getResources() != null) resourceSet.addAll(e.getResources());
        });
        resMp.put("id", loginUser.getId());
        resMp.put("username", loginUser.getUsername());
        resMp.put("nickname", loginUser.getNickname());
        resMp.put("sex", loginUser.getSex());
        resMp.put("lastLoginDateTime", loginUser.getLastLoginDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        resMp.put("menus", menuSet);
        resMp.put("resources", resourceSet);
        return resMp;
    }
}
