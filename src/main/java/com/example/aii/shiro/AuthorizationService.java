package com.example.aii.shiro;

import com.example.aii.entity.User;
import com.example.aii.service.impl.RoleService;
import com.example.aii.service.impl.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.util.ByteSource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Set;

@Service
public class AuthorizationService {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Cacheable(value = "resources", key = "#user.id")
    public AuthorizationInfo getAuthorizationInfo(User user) {
        Set<String> resourceSet = roleService.getAllResourcesByUserId(user.getId());
        SimpleAuthorizationInfo au = new SimpleAuthorizationInfo();
        au.addStringPermissions(resourceSet);
        return au;
    }

    public AuthenticationInfo getAuthenticationInfo(String username, String realmName) {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new AuthenticationException("用户不存在");
        } else if (User.STATUS.禁用.equals(user.getStatus())) {
            throw new AuthenticationException("用户已禁用");
        } else {
            user.setLastLoginDatetime(LocalDateTime.now());
            return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), realmName);
        }
    }
}
