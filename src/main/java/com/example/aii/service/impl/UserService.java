package com.example.aii.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.aii.controller.login.dto.PasswordFormDTO;
import com.example.aii.entity.Project;
import com.example.aii.entity.RelationalUserProject;
import com.example.aii.entity.User;
import com.example.aii.exception.AiiException;
import com.example.aii.exception.ValidationFailedException;
import com.example.aii.mapper.UserMapper;
import com.example.aii.model.UserRelatedProject;
import com.example.aii.util.EncryptUtils;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IService<User> {

    @Resource
    private UserMapper userMapper;

    @Resource
    private Realm realm;

    @Resource
    private HashedCredentialsMatcher matcher;

    @Resource
    private RelationalUserProjectService relationalUserProjectService;

    public User findByUsername(String username) {
        QueryWrapper<User> q = new QueryWrapper<>();
        q.eq("username", username);
        return getOne(q);
    }

    public void createUser(User user) {
        if (checkDuplicateName(user.getUsername())) {
            throw new AiiException("用户名已存在");
        } else {
            String saleValue = EncryptUtils.createSalt();
            save(new User(user.getUsername(), EncryptUtils.salt(user.getPassword(), ByteSource.Util.bytes(saleValue)), saleValue, user.getNickname(), user.getSex(), user.getStatus(), null));
        }
    }

    private boolean checkDuplicateName(String username) {
        QueryWrapper<User> q = new QueryWrapper<>();
        q.eq("username", username);
        return userMapper.selectCount(q) > 0;
    }

    public IPage<UserRelatedProject> findPageByNameLike(String queryNameLike, Page<User> page) {
        return userMapper.findPageByNameLike(page, queryNameLike);
    }

    public List<Project> findRelationProject(Long userId) {
        return userMapper.selectRelationProject(userId);
    }

    public void updateAndRelatedProjectById(User user, List<Number> relatedProjectIdArray) {
        Long userId = user.getId();
        // 判断是否存在更新
        User sourceUser = this.getById(userId);
        if (!user.getNickname().equals(sourceUser.getNickname()) ||
                !user.getStatus().equals(sourceUser.getStatus()) ||
                !Arrays.equals(user.getRoleIds(), sourceUser.getRoleIds())) {
            updateById(user);
        }
        // 判断是否关联变更
        if (relatedProjectIdArray != null) {
            List<RelationalUserProject> rupLs = relationalUserProjectService.getByUserId(userId);
            List<Long> rpaConvert = relatedProjectIdArray.stream()
                    .map(Number::longValue)
                    .collect(Collectors.toList());
            List<Long> rpa = rupLs.stream()
                    .map(RelationalUserProject::getProjectId)
                    .collect(Collectors.toList());
            if (!rpaConvert.equals(rpa)) {
                relationalUserProjectService.reRelatedUser2Project(userId, rpaConvert);
            }
        }
    }

    public void editPassword(PasswordFormDTO dto) {
        User user = findByUsername(dto.getUsername());
        UsernamePasswordToken token = new UsernamePasswordToken(dto.getUsername(), dto.getOldPassword());
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), realm.getName());
        if (!matcher.doCredentialsMatch(token, info)) {
            throw new ValidationFailedException("原密码错误");
        }
        String saleValue = EncryptUtils.createSalt();
        User u = new User();
        SimpleHash hash = new SimpleHash("SHA-256", dto.getNewPassword(), ByteSource.Util.bytes(saleValue), 2);
        u.setPassword(hash.toHex());
        u.setSalt(saleValue);
        u.setId(user.getId());
        updateById(u);
    }
}
