package com.example.aii.controller.usermanagement;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.aii.entity.User;
import com.example.aii.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequiresPermissions("人员管理")
public class UserManagementController {

    @Resource
    private UserService userService;


    @GetMapping("/userPage")
    public IPage<User> getUserPageByNameLike(@RequestParam(required = false) String queryNameLike,
                                             Page<User> page) {
        return userService.findPageByNameLike(queryNameLike, page);
    }

    @PostMapping("/user")
    @RequiresPermissions("新建用户")
    public void createUser(UserDTO userDTO) {
        userService.createUser(userDTO.toUser());
    }

    @PatchMapping("/user/{id}")
    @RequiresPermissions("编辑用户")
    public void updateUser(@PathVariable("id") Long id, UserEditDTO userEditDTO) {
        User user = userEditDTO.getUser();
        user.setId(id);
        userService.updateUser(user);
    }

    @PostMapping("/relationUserRole")
    @RequiresPermissions("配置角色")
    public void postRelationUserRole(RelationUserRolesDTO relationUserRolesDTO) {
        User[] users = relationUserRolesDTO.toUsers();
        userService.updateBatch(users);
    }
}
