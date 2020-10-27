package com.example.aii.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.aii.entity.Project;
import com.example.aii.entity.User;
import com.example.aii.model.UserRelatedProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<Project> selectRelationProject(@Param("userId") Long userId);

    IPage<UserRelatedProject> findPageByNameLike(Page<User> page,
                                                 @Param("queryNameLike") String queryNameLike
    );
}
