package com.example.aii.controller.rolemanagment;

import com.example.aii.entity.Role;
import com.example.aii.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@RestController
@RequiresPermissions("角色管理")
public class RoleManagementController {

    @Resource
    private RoleService roleService;

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PostMapping("/role")
    @RequiresPermissions("新建角色")
    public void saveRole(RoleDTO roleDTO) {
        roleService.insert(roleDTO.toRole());
    }


    @PatchMapping("/role/{id}")
    @RequiresPermissions("编辑角色")
    public void updateRole(@PathVariable("id") Long id, RoleDTO roleDTO) {
        roleDTO.setId(id);
        roleService.updateById(roleDTO.toRole());
    }

    @DeleteMapping("role/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        roleService.deleteById(id);
    }


    @RequiresPermissions("保存权限")
    @PatchMapping("/role/{roleId}/resources")
    public void updateRoleOfResources(@PathVariable("roleId") Long roleId, @RequestBody Set<String> resources) {
        Role role = new Role();
        role.setResources(resources);
        role.setId(roleId);
        roleService.updateById(role);
    }

    @GetMapping("/resources")
    public Set<String> getResourcesByMenuName(@RequestParam String menuName) {
        return roleService.findResourcesByMenuName(menuName);
    }
}
