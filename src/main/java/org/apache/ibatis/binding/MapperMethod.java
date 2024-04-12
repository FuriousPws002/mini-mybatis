package org.apache.ibatis.binding;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

/**
 * @author furious 2024/4/8
 */
public class MapperMethod {

    private final SqlCommand command;
    private final MethodSignature method;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration config) {
        this.command = new SqlCommand(config, mapperInterface, method);
        this.method = new MethodSignature(config, mapperInterface, method);
    }

    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result = null;
        switch (command.getType()) {
            case INSERT: {
                result = sqlSession.insert(command.getName(), method.convertArgsToSqlCommandParam(args));
                break;
            }
            case SELECT: {
                if (method.returnsMany) {
                    result = executeForMany(sqlSession, args);
                }
                break;
            }
            default:
                throw new BindingException(command.getType() + " not support");
        }
        return result;
    }

    private <T> Object executeForMany(SqlSession sqlSession, Object[] args) {
        return sqlSession.selectList(command.getName(), method.convertArgsToSqlCommandParam(args));
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

    public static class MethodSignature {

        private final ParamNameResolver paramNameResolver;
        private final boolean returnsMany;

        public MethodSignature(Configuration configuration, Class<?> mapperInterface, Method method) {
            this.paramNameResolver = new ParamNameResolver(method);
            returnsMany = Collection.class.isAssignableFrom(method.getReturnType());
        }

        public Object convertArgsToSqlCommandParam(Object[] args) {
            return paramNameResolver.getNamedParams(args);
        }
    }
}
