package org.apache.ibatis.executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.executor.statement.StatementUtil;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;

/**
 * @author furious 2024/4/8
 */
public class SimpleExecutor implements Executor {

    private final Configuration configuration;

    public SimpleExecutor(Configuration configuration) {
        if (Objects.isNull(configuration.getDataSource())) {
            throw new ExecutorException("dataSource in Configuration cannot be empty");
        }
        this.configuration = configuration;
    }

    @Override
    public int update(MappedStatement ms, Object parameter) throws SQLException {
        Statement stmt = null;
        try {
            StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, null);
            stmt = prepareStatement(handler);
            return handler.update(stmt);
        } finally {
            StatementUtil.closeStatement(stmt);
        }
    }

    private Statement prepareStatement(StatementHandler handler) throws SQLException {
        Statement stmt;
        Connection connection = configuration.getDataSource().getConnection();
        connection.setAutoCommit(true);
        stmt = handler.prepare(connection, null);
        return stmt;
    }

}
