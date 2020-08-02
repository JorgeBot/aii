package com.example.aii.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.aii.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
