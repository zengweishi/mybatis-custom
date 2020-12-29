package com.mybatis.customMybatis.sqlSession;

import com.mybatis.customMybatis.cfg.Configuration;
import com.mybatis.customMybatis.sqlSession.impl.DefaultSqlSessionFactory;
import com.mybatis.customMybatis.utils.XMLConfigBuilder;

import java.io.InputStream;

/**
 * @Auther: weishi.zeng
 * @Date: 2020/10/29 18:16
 * @Description:用于创建sqlSessionFactory工厂
 */
public class SqlSessionFactoryBuilder {

    /**
     * 通过字节流创建sqlSessionFactory工厂
     * @param resource
     * @return
     */
    public SqlSessionFactory build(InputStream resource) {
        //加载mybatis相关配置
        Configuration configuration = XMLConfigBuilder.loadConfiguration(resource);
        return new DefaultSqlSessionFactory(configuration);
    }
}
