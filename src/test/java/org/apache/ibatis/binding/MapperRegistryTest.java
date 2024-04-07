package org.apache.ibatis.binding;

import org.apache.ibatis.dao.UserMapper;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author furious 2024/4/3
 */
public class MapperRegistryTest {

    @Test
    public void test() {
        Configuration configuration = new Configuration();
        configuration.addMapper(UserMapper.class);
        SqlSession sqlSession = new DefaultSqlSession(configuration);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        Assert.assertNotNull(userMapper);
    }

    @Test
    public void xmlParse() {
        Configuration configuration = new Configuration();
        configuration.addMapper(UserMapper.class);
        MappedStatement ms = configuration.getMappedStatement(UserMapper.class.getName() + "." + "insert");
        Assert.assertNotNull(ms);
    }
}
