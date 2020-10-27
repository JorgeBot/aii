package com.example.aii.controller.common;

import com.example.aii.entity.User;
import com.example.aii.pojo.Tree;
import com.example.aii.service.impl.ModuleService;
import com.example.aii.shiro.LoginUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class CommonController {

    @Resource
    private ModuleService moduleService;

    @GetMapping("/moduleTree")
    public List<Tree<Object>> getModuleTree() {
        User loginUser = LoginUtils.getLoginUser();
        return moduleService.findAll2TreeByUserId(loginUser.getId());
    }
}
