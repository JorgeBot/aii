package com.example.aii.controller.login;

import com.example.aii.entity.User;
import com.example.aii.service.impl.LoginService;
import com.example.aii.service.impl.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class LoginController {

    @Resource
    private LoginService loginService;

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public UserInformationDTO login(User user, boolean rememberMe) {
        Map<String, Object> userInformationMp = loginService.login(user, rememberMe);
        return new UserInformationDTO().fromMap(userInformationMp);
    }

    @PostMapping("/editPassword")
    public void editPassword(PasswordFormDTO dto) {
        userService.editPassword(dto);
    }
}
