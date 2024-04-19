package org.apache.ibatis.plugin;

import java.sql.Connection;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;

/**
 * @author furious 2024/4/19
 */
public class PageInterceptor implements Interceptor {
    @Override
    public String pointcut() {
        return PointcutRegistry.StatementHandler_prepare_Connection_Integer;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = invocation.getTarget();
        BoundSql original = statementHandler.getBoundSql();
        Object[] args = invocation.getArgs();
        Connection connection = (Connection) args[0];
        //不判读是否为select语句，limit仅为示范参数
        String sql = original.getSql();
        sql += " LIMIT 1,1";
        return connection.prepareStatement(sql);
    }
}
