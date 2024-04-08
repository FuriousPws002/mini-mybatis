package org.apache.ibatis.executor.statement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author furious 2024/4/8
 */
public interface StatementHandler {

    Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException;

    int update(Statement statement) throws SQLException;
}
