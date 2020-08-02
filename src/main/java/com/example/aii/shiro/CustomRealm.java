package com.example.aii.shiro;

import com.example.aii.entity.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;

public class CustomRealm extends AuthorizingRealm {

    @Lazy
    @Resource
    private ShrioService shrioService;

    @Override
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return shrioService.getAuthorizationInfo((User) principalCollection.getPrimaryPrincipal());
    }

    @Override
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return shrioService.getAuthenticationInfo(authenticationToken.getPrincipal().toString(), getName());
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        User user = (User) principals.getPrimaryPrincipal();
        return user.getUsername().equals("admin") || super.isPermitted(principals, permission);
    }

    @Override
    public boolean hasRole(PrincipalCollection principals, String roleIdentifier) {
        User user = (User) principals.getPrimaryPrincipal();
        return user.getUsername().equals("admin") || super.hasRole(principals, roleIdentifier);
    }
}
