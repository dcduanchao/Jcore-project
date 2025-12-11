package com.dc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dc.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
