package com.mybatis.customMybatis.cfg;

import lombok.Data;

/**
 * @Auther: weishi.zeng
 * @Date: 2020/11/3 14:54
 * @Description:用于封装执行的sql语句和结果类型的全限定类名
 */
@Data
public class Mapper {
    /**
     * 执行的sql语句
     */
    private String queryString;
    /**
     * 执行后返回的全限定类名
     */
    private String resultType;

}
