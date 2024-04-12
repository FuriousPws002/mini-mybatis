package org.apache.ibatis.session.defaults;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

/**
 * @author furious 2024/4/3
 */
public class DefaultSqlSession implements SqlSession {

    private final Configuration configuration;
    private final Executor executor;

    public DefaultSqlSession(Configuration configuration) {
        this(configuration, null);
    }

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        if (Objects.isNull(executor)) {
            executor = new SimpleExecutor(configuration);
        }
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    @Override
    public int insert(String statement, Object parameter) {
        return update(statement, parameter);
    }

    @Override
    public int update(String statement, Object parameter) {
        try {
            MappedStatement ms = configuration.getMappedStatement(statement);
            return executor.update(ms, parameter);
        } catch (Exception e) {
            ExceptionUtils.rethrow(e);
        }
        return -1;
    }

    @Override
    public <T> List<T> selectList(String statement, Object parameter) {
        try {
            MappedStatement ms = configuration.getMappedStatement(statement);
            return executor.query(ms, parameter);
        } catch (Exception e) {
            ExceptionUtils.rethrow(e);
        }
        return Collections.emptyList();
    }

    @Override
    public void close() throws IOException {

    }
}
