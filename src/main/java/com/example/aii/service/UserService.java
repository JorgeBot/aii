package com.example.aii.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.aii.entity.User;
import com.example.aii.exception.AiiException;
import com.example.aii.mapper.UserMapper;
import com.example.aii.util.EncryptUtils;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public User findByUsername(String username) {
        QueryWrapper<User> q = new QueryWrapper<>();
        q.eq("username", username);
        return userMapper.selectOne(q);
    }

    public void createUser(User user) {
        if (checkDuplicateName(user.getUsername())) {
            throw new AiiException("用户名已存在");
        } else {
            String saleValue = EncryptUtils.createSalt();
            userMapper.insert(new User(user.getUsername(), EncryptUtils.salt(user.getPassword(), ByteSource.Util.bytes(saleValue)), saleValue, user.getNickname(), user.getSex(), user.getStatus(), null));
        }
    }

    private boolean checkDuplicateName(String username) {
        QueryWrapper<User> q = new QueryWrapper<>();
        q.eq("username", username);
        return userMapper.selectCount(q) > 0;
    }

    public IPage<User> findPageByNameLike(String queryNameLike, Page<User> page) {
        QueryWrapper<User> q = new QueryWrapper<>();
        if (queryNameLike != null) {
            q.like("nickname", queryNameLike);
        }
        return userMapper.selectPage(page, q);
    }

    public void updateUser(User user) {
        userMapper.updateById(user);
    }

    public void updateBatch(User[] users) {
        Arrays.stream(users).forEach(e -> userMapper.updateById(e));
    }
}
