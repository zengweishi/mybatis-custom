package com.mybatis.customMybatis.sqlSession.impl;

import com.mybatis.customMybatis.cfg.Configuration;
import com.mybatis.customMybatis.sqlSession.SqlSession;
import com.mybatis.customMybatis.sqlSession.SqlSessionFactory;

import java.sql.Connection;

/**
 * @Auther: weishi.zeng
 * @Date: 2020/10/29 18:19
 * @Description:
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
