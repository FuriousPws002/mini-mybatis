package org.apache.ibatis.session;

import org.apache.ibatis.DataSourceBuilderTest;
import org.apache.ibatis.dao.UserMapper;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
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
}
