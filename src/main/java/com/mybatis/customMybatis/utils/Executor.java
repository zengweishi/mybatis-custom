package com.mybatis.customMybatis.utils;

import com.mybatis.customMybatis.cfg.Mapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: weishi.zeng
 * @Date: 2020/11/5 17:24
 * @Description:执行sql语句，返回结果集
 */
public class Executor {
    public <E> List<E> selectList(Mapper mapper, Connection connection) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String queryString = mapper.getQueryString();
        String resultType = mapper.getResultType();
        try {
            Class<?> resultClass = Class.forName(resultType);
            //获取PrepareStatement对象
            preparedStatement = connection.prepareStatement(queryString);
            //执行sql语句获取结果集
            resultSet = preparedStatement.executeQuery();
            //封装结果集
            ArrayList<E> list = new ArrayList<E>();
            while (resultSet.next()) {
                //实例化要封装的实体对象
                E obj = (E) resultClass.newInstance();
                //结果集的元信息
                ResultSetMetaData metaData = resultSet.getMetaData();
                //总列数
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount ;i++) {
                    //获取每列名称
                    String columnName = metaData.getColumnName(i);
                    //根据列名获取每列的值
                    Object columnValue = resultSet.getObject(columnName);
                    //obj赋值：使用Java内省机制（借助PropertyDescriptor实现属性的封装）
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultClass);
                    //获取它的写入方法
                    Method writeMethod = propertyDescriptor.getWriteMethod();
                    //把获取的列值给对象赋值
                    writeMethod.invoke(obj,columnValue);
                }
                //赋好值加入list
                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            release(preparedStatement, resultSet);
        }
    }

    private void release(PreparedStatement preparedStatement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
