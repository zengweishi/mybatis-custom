package com.mybatis.customMybatis.sqlSession;

import com.mybatis.mapper.UserMapper;

public interface SqlSession {
    <T> T getMapper(Class<T> userMapperClass);

    void close();
}
