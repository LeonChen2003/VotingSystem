package com.vote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vote.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {}
