package org.apache.ibatis.session;

import java.io.Closeable;
import java.util.List;

/**
 * @author furious 2024/3/29
 */
public interface SqlSession extends Closeable {

    Configuration getConfiguration();

    <T> T getMapper(Class<T> type);

    int insert(String statement, Object parameter);

    int update(String statement, Object parameter);

    <T> List<T> selectList(String statement, Object parameter);
}
