package com.example.aii.shiro;

import com.example.aii.entity.User;
import org.apache.shiro.SecurityUtils;

public class LoginUtils {

    public static User getLoginUser() {
        User login = (User) SecurityUtils.getSubject().getPrincipal();
        if (login != null) {
            return login;
        }
        return new User("guest", null, null, "访客", User.SEX.男, User.STATUS.正常, null);
    }
}
