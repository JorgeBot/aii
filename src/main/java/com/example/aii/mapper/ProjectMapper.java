package com.example.aii.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.aii.entity.Project;
import com.example.aii.model.ProjectRelatedUser;
import org.apache.ibatis.annotations.Param;

public interface ProjectMapper extends BaseMapper<Project> {
    IPage<ProjectRelatedUser> findByProjectNameLike(Page<Project> page, @Param("projectNameLike") String projectNameLike);
}
