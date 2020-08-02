package com.example.aii.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.aii.entity.Role;
import com.example.aii.entity.User;
import com.example.aii.mapper.RoleMapper;
import com.example.aii.mapper.UserMapper;
import com.example.aii.shiro.PermissionDirectory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserMapper userMapper;

    public List<Role> getAllRoles() {
        return roleMapper.selectList(new QueryWrapper<>());
    }

    public void insert(Role role) {
        roleMapper.insert(role);
    }

    public Set<String> getAllResourcesByUserId(Long id) {
        List<Role> roleLs = findByUserId(id);
        // 获取菜单
        Set<String> menus = roleLs.stream()
                .filter(f -> f.getMenus() != null)
                .flatMap(m -> m.getMenus().stream())
                .collect(Collectors.toSet());
        // 获取权限
        Set<String> resources = roleLs.stream()
                .filter(f -> f.getResources() != null)
                .flatMap(m -> m.getResources().stream())
                .collect(Collectors.toSet());
        resources.addAll(menus);
        return resources;
    }

    @CacheEvict(value = "resources", allEntries = true)
    public void updateById(Role role) {
        roleMapper.updateById(role);
    }

    public void deleteById(Long id) {
        roleMapper.deleteById(id);
    }

    public Set<String> findResourcesByMenuName(String menuName) {
        return PermissionDirectory.getPermission().get(menuName);
    }

    public List<Role> findByUserId(Long id) {
        User user = userMapper.selectById(id);
        QueryWrapper<Role> q = new QueryWrapper<>();
        q.in("id", Arrays.asList(user.getRoleIds()));
        return roleMapper.selectList(q);
    }
}
