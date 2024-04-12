package org.apache.ibatis.executor;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.mapping.MappedStatement;

/**
 * @author furious 2024/4/8
 */
public interface Executor {

    int update(MappedStatement ms, Object parameter) throws SQLException;

    <T> List<T> query(MappedStatement ms, Object parameter) throws SQLException;
}
