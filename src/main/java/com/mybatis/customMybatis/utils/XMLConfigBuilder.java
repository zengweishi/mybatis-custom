package com.mybatis.customMybatis.utils;

import com.mybatis.customMybatis.annotations.Select;
import com.mybatis.customMybatis.cfg.Configuration;
import com.mybatis.customMybatis.cfg.Mapper;
import com.mybatis.customMybatis.io.Resources;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: weishi.zeng
 * @Date: 2020/11/3 14:53
 * @Description:XML/注解解析工具类
 */
public class XMLConfigBuilder {
    public static Configuration loadConfiguration(InputStream stream) {
        //定义mybatis的配置对象
        Configuration configuration = new Configuration();
        //获取SAXReader对象
        SAXReader saxReader = new SAXReader();
        try {
            //根据输入流获取document对象
            Document document = saxReader.read(stream);
            //获取根节点
            Element rootElement = document.getRootElement();
            //获取所有的property节点(使用xpath中获取指定节点的方式)
            List<Element> list = rootElement.selectNodes("//property");
            for (Element element : list) {
                //获取property的name
                String name = element.attributeValue("name");
                //获取property的value
                String value = element.attributeValue("value");
                if ("driver".equals(name)) {
                    configuration.setDriver(value);
                } else if ("url".equals(name)) {
                    configuration.setUrl(value);
                } else if ("username".equals(name)) {
                    configuration.setUsername(value);
                } else if ("password".equals(name)) {
                    configuration.setPassward(value);
                }
            }
            //取出mapper标签
            List<Element> mapperList = rootElement.selectNodes("//mappers/mapper");
            for (Element element : mapperList) {
                Attribute attribute = element.attribute("resource");
                if (attribute != null) {
                    //使用的是xml
                    String resource = element.attributeValue("resource");
                    Map<String, Mapper> map = loadMapperXMLConfiguration(resource);
                    configuration.setMappers(map);
                } else {
                    //使用的是注解
                    String className = element.attributeValue("class");
                    Map<String, Mapper> map = loadMapperAnnotationConfiguration(className);
                    configuration.setMappers(map);
                }
            }
            return configuration;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析XML配置的mapper文件
     * @param resource
     * @return map中包含了获取的唯一标识（key是由dao的全限定类名和方法名组成）
     *      以及执行所需的必要信息（value是一个Mapper对象，里面存放的是执行的SQL语句和要封装的实体类全限定类名）
     */
    private static Map<String, Mapper> loadMapperXMLConfiguration(String resource) throws IOException {
        Map<String, Mapper> mappers = new HashMap<String, Mapper>();
        InputStream stream = Resources.getResourceAsStream(resource);
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(stream);
            Element rootElement = document.getRootElement();
            //获取<mapper>的namespace
            String namespace = rootElement.attributeValue("namespace");
            List<Element> selectMapperList = rootElement.selectNodes("//select");
            for (Element element : selectMapperList) {
                //获取select语句的id名称
                String id = element.attributeValue("id");
                //获取select语句结果的全限定类名
                String resultType = element.attributeValue("resultType");
                //获取执行的sql语句
                String queryString = element.getText();
                Mapper mapper = new Mapper();
                mapper.setResultType(resultType);
                mapper.setQueryString(queryString);
                String key = namespace + "." + id;
                mappers.put(key, mapper);
            }
            return mappers;
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            stream.close();
        }
    }

    /**
     * 解析注解配置
     * @param className
     * @return
     */
    private static Map<String, Mapper> loadMapperAnnotationConfiguration(String className) {
        Map<String, Mapper> mappers = new HashMap<String, Mapper>();
        try {
            Class<?> daoClass = Class.forName(className);
            //获取dao类中的方法名
            Method[] methods = daoClass.getMethods();
            for (Method method : methods) {
                //判断方法是否有@Select注解
                boolean annotationPresent = method.isAnnotationPresent(Select.class);
                if (annotationPresent) {
                    Mapper mapper = new Mapper();
                    Select annotation = method.getAnnotation(Select.class);
                    //获取注解的value值 sql语句
                    String value = annotation.value();
                    mapper.setQueryString(value);
                    Type returnType = method.getGenericReturnType();
                    //判断type是不是参数化的类型：List<String>是参数化类型，List不是参数化类型
                    if (returnType instanceof ParameterizedType) {
                        //强转
                        ParameterizedType type = (ParameterizedType)returnType;
                        //得到参数化类型中的实际类型参数，比如Map<String,Long> ,则Type[]中有String，Long
                        Type[] typeArguments = type.getActualTypeArguments();
                        //取出第一个
                        Class resultClass = (Class) typeArguments[0];
                        String name = resultClass.getName();
                        mapper.setResultType(name);
                    }
                    //获取方法名
                    String methodName = method.getName();
                    //获取类名
                    String daoClassName = daoClass.getDeclaringClass().getName();
                    String key = daoClassName + "." + methodName;
                    mappers.put(key, mapper);
                }
            }
            return mappers;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
