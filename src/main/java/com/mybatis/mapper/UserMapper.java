package com.mybatis.mapper;

import com.mybatis.pojo.User;

import java.util.List;

public interface UserMapper {
    /**
     * 查询所有用户
     *
     * @return
     */
    List<User> listAllUsers();
}
