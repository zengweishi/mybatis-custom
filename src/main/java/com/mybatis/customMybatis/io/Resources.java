package com.mybatis.customMybatis.io;

import java.io.InputStream;

/**
 * @Auther: weishi.zeng
 * @Date: 2020/10/29 18:11
 * @Description:使用类加载器读取配置文件
 */
public class Resources {

    /**
     * 根据参数获取字节流
     * @param filePath
     * @return
     */
    public static InputStream getResourceAsStream(String filePath) {
        return Resources.class.getClassLoader().getResourceAsStream(filePath);
    }
}
