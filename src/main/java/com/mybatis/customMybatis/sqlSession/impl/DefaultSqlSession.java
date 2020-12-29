package com.mybatis.customMybatis.sqlSession.impl;

import com.mybatis.customMybatis.cfg.Configuration;
import com.mybatis.customMybatis.cfg.MapperProxy;
import com.mybatis.customMybatis.sqlSession.SqlSession;
import com.mybatis.customMybatis.utils.DataSourceUtil;
import com.mybatis.mapper.UserMapper;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Auther: weishi.zeng
 * @Date: 2020/10/29 18:21
 * @Description:
 */
public class DefaultSqlSession implements SqlSession {
    private Configuration configuration;
    private Connection connection;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        this.connection = DataSourceUtil.getConnection(configuration);

    }

    /**
     * 获取执行代理对象，T是被代理对象
     * @param mapperClass
     * @param <T> 被代理对象
     * @return 代理对象
     */
    public <T> T getMapper(Class<T> mapperClass) {
        return (T) Proxy.newProxyInstance(mapperClass.getClassLoader(),
                new Class[]{mapperClass},new MapperProxy(connection,configuration.getMappers()));
    }

    /**
     * 释放资源
     */
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
