package org.apache.ibatis.session;

import java.util.List;

import org.apache.ibatis.DataSourceBuilderTest;
import org.apache.ibatis.dao.UserMapper;
import org.apache.ibatis.entity.UserDO;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author furious 2024/4/8
 */
public class SqlSessionTest {

    @Test
    public void staticSqlExecution() {
        Configuration configuration = new Configuration();
        configuration.setDataSource(DataSourceBuilderTest.build());
        configuration.addMapper(UserMapper.class);
        SqlSession sqlSession = new DefaultSqlSession(configuration);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.insert();
    }

    /**
     * Param注解绑定参数
     */
    @Test
    public void insertWithParam() {
        Configuration configuration = new Configuration();
        configuration.setDataSource(DataSourceBuilderTest.build());
        configuration.addMapper(UserMapper.class);
        SqlSession sqlSession = new DefaultSqlSession(configuration);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.insertWithParam("Alice", 23);
    }

    /**
     * 支持传null
     */
    @Test
    public void insertWithParamNullable() {
        Configuration configuration = new Configuration();
        configuration.setDataSource(DataSourceBuilderTest.build());
        configuration.addMapper(UserMapper.class);
        SqlSession sqlSession = new DefaultSqlSession(configuration);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.insertWithParam("Jack", null);
    }

    /**
     * 无Param注解的单个参数
     * 单个参数没有设置Param注解时，无需参数名称和xml中变量名相同
     */
    @Test
    public void insertWithoutParam() {
        Configuration configuration = new Configuration();
        configuration.setDataSource(DataSourceBuilderTest.build());
        configuration.addMapper(UserMapper.class);
        SqlSession sqlSession = new DefaultSqlSession(configuration);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.insertWithoutParam("Tom");
    }

    /**
     * 简单对象
     */
    @Test
    public void insertWithPOJO() {
        Configuration configuration = new Configuration();
        configuration.setDataSource(DataSourceBuilderTest.build());
        configuration.addMapper(UserMapper.class);
        SqlSession sqlSession = new DefaultSqlSession(configuration);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        UserDO user = new UserDO();
        user.setName("Sam");
        user.setAge(20);
        userMapper.insertWithPOJO(user);
    }

    @Test
    public void queryPOJOHandleTheResult() {
        Configuration configuration = new Configuration();
        configuration.setDataSource(DataSourceBuilderTest.build());
        configuration.addMapper(UserMapper.class);
        SqlSession sqlSession = new DefaultSqlSession(configuration);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<UserDO> list = userMapper.select();
        Assert.assertNotNull(list);
        UserDO userDO = list.get(0);
        Assert.assertNotNull(userDO.getName());
        Assert.assertNotNull(userDO.getNamex());
    }

}
