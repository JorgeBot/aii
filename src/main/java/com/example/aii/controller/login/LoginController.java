package com.example.aii.controller.login;

import com.example.aii.entity.User;
import com.example.aii.service.LoginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@RestController
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("/login")
    public UserInformationVO login(User user, boolean rememberMe) {
        Map<String, Object> userInformationMp = loginService.login(user, rememberMe);
        return new UserInformationVO().fromMap(userInformationMp);
    }

    public Object m1() throws Exception {
        Object res = null;
        FutureTask<Object> fk = new FutureTask<>(() -> {
            // 逻辑
            return new Object();
        });
        try {
            res = fk.get(3000, TimeUnit.SECONDS);
        } catch (Exception e) {
            res = "默认值";
        }
        return res;
    }
}
