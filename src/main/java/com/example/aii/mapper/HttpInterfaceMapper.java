package com.example.aii.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.aii.entity.HttpInterface;
import org.apache.ibatis.annotations.Param;

public interface HttpInterfaceMapper extends BaseMapper<HttpInterface> {

    IPage<HttpInterface> selectByNameLikeAndModuleId(Page<HttpInterface> page,
                                      @Param("queryLike")String queryLike,
                                      @Param("moduleId") Long moduleId);
}
