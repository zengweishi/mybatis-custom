package com.mybatis.customMybatis.cfg;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: weishi.zeng
 * @Date: 2020/11/3 14:53
 * @Description:自定义Mybatis的配置类
 */
public class Configuration {
    private String driver;
    private String url;
    private String username;
    private String passward;
    //下面用putAll的方式
    private Map<String,Mapper> mappers = new HashMap<String, Mapper>();

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassward() {
        return passward;
    }

    public Map<String, Mapper> getMappers() {
        return mappers;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassward(String passward) {
        this.passward = passward;
    }

    public void setMappers(Map<String, Mapper> mappers) {
        // 这里应该使用put的方式，直接赋值的话会覆盖原本Map集合中的内容
        this.mappers.putAll(mappers);
    }
}
