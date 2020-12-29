import com.mybatis.customMybatis.io.Resources;
import com.mybatis.customMybatis.sqlSession.SqlSession;
import com.mybatis.customMybatis.sqlSession.SqlSessionFactory;
import com.mybatis.customMybatis.sqlSession.SqlSessionFactoryBuilder;
import com.mybatis.mapper.UserMapper;
import com.mybatis.pojo.User;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * @Auther: weishi.zeng
 * @Date: 2020/9/29 01:21
 * @Description:测试自定义mybatis
 */
public class MybatisTest {
    @Test
    public void testInit() {
        try {
            //1.读取配置文件
            InputStream resource = Resources.getResourceAsStream("mybatis-config.xml");
            //2.创建sqlSessionFactory工厂
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            SqlSessionFactory factory = builder.build(resource);
            //3.获取sqlsession对象
            SqlSession sqlSession = factory.openSession();
            //4.使用sqlsession创建Mapper的代理对象
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            //5.使用代理对象执行查询
            List<User> users = mapper.listAllUsers();
            System.out.println(users.toString());
            //6.释放资源
            sqlSession.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
