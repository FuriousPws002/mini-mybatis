package org.apache.ibatis.binding;

import java.lang.reflect.Method;
import java.util.Objects;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

/**
 * @author furious 2024/4/8
 */
public class MapperMethod {

    private final SqlCommand command;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration config) {
        this.command = new SqlCommand(config, mapperInterface, method);
    }

    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result;
        switch (command.getType()) {
            case INSERT: {
                result = sqlSession.insert(command.getName(), args);
                break;
            }
            default:
                throw new BindingException(command.getType() + " not support");
        }
        return result;
    }

    public static class SqlCommand {

        private final String name;
        private final SqlCommandType type;

        public SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
            final String methodName = method.getName();
            String statementId = mapperInterface.getName() + "." + methodName;
            MappedStatement ms = configuration.getMappedStatement(statementId);
            if (Objects.isNull(ms)) {
                throw new BindingException(statementId + " statement not found");
            }
            this.name = ms.getId();
            this.type = ms.getSqlCommandType();
        }

        public String getName() {
            return name;
        }

        public SqlCommandType getType() {
            return type;
        }
    }

}
