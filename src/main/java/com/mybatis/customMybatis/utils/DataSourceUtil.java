package com.mybatis.customMybatis.utils;

import com.mybatis.customMybatis.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @Auther: weishi.zeng
 * @Date: 2020/11/5 16:44
 * @Description:用于创建数据库数据源的工具类
 */
public class DataSourceUtil {
    public static Connection getConnection(Configuration configuration) {
        try {
            Class.forName(configuration.getDriver());
            return DriverManager.getConnection(configuration.getUrl(),configuration.getUsername(),configuration.getPassward());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
