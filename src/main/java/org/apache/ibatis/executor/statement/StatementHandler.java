package org.apache.ibatis.executor.statement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author furious 2024/4/8
 */
public interface StatementHandler {

    Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException;

    int update(Statement statement) throws SQLException;

    void parameterize(Statement statement) throws SQLException;

    <T> List<T> query(Statement statement) throws SQLException;
}
