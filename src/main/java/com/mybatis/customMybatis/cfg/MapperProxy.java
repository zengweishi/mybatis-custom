package com.mybatis.customMybatis.cfg;

import com.mybatis.customMybatis.utils.Executor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: weishi.zeng
 * @Date: 2020/11/5 17:09
 * @Description:
 */
public class MapperProxy implements InvocationHandler {
    private Connection connection;
    private Map<String, Mapper> mappers = new HashMap<String, Mapper>();

    public MapperProxy(Connection connection, Map<String, Mapper> mappers) {
        this.connection = connection;
        this.mappers = mappers;
    }

    /**
     * 执行sql
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //获取方法名
        String methodName = method.getName();
        //获取类名
        String className = method.getDeclaringClass().getName();
        //组装mapper的key
        String key = className + "." + methodName;
        Mapper mapper = mappers.get(key);
        if (mapper == null) {
            throw new IllegalArgumentException("传入的参数有误");
        }
        return new Executor().selectList(mapper, connection);
    }
}
