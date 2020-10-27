package com.example.aii.controller.usermanagement;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.aii.controller.usermanagement.dto.RelatedUserRolesDTO;
import com.example.aii.controller.usermanagement.dto.UserDTO;
import com.example.aii.controller.usermanagement.dto.UserEditDTO;
import com.example.aii.entity.Project;
import com.example.aii.entity.User;
import com.example.aii.model.UserRelatedProject;
import com.example.aii.service.impl.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequiresPermissions("人员管理")
public class UserManagementController {

    @Resource
    private UserService userService;


    @GetMapping("/userPage")
    public IPage<UserRelatedProject> getUserPageByNameLike(@RequestParam(required = false) String queryNameLike,
                                                           Page<User> page) {
        return userService.findPageByNameLike(queryNameLike, page);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.list();
    }

    @PostMapping("/user")
    @RequiresPermissions("新建用户")
    public void createUser(UserDTO userDTO) {
        userService.createUser(userDTO.toUser());
    }

    @PatchMapping("/user/{id}")
    @RequiresPermissions("用户操作")
    public void updateUser(@PathVariable("id") Long id, UserEditDTO userEditDTO) {
        User user = userEditDTO.getUser();
        user.setId(id);
        userService.updateAndRelatedProjectById(user, userEditDTO.getRelatedProjectIdArray());
    }

    @PostMapping("/relatedUserRole")
    @RequiresPermissions("用户操作")
    public void postRelationUserRole(RelatedUserRolesDTO relatedUserRolesDTO) {
        List<User> userLs = relatedUserRolesDTO.toUserLs();
        userService.updateBatchById(userLs);
    }

    @GetMapping("/user/{userId}/relationProject")
    public List<Project> getRelationProject(@PathVariable Long userId) {
        return userService.findRelationProject(userId);
    }
}
