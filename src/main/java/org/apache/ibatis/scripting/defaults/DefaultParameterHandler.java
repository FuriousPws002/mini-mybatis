package org.apache.ibatis.scripting.defaults;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeException;

/**
 * @author furious 2024/4/10
 */
public class DefaultParameterHandler implements ParameterHandler {

    private final MappedStatement mappedStatement;
    private final Object parameterObject;
    private final BoundSql boundSql;
    private final Configuration configuration;

    public DefaultParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        this.mappedStatement = mappedStatement;
        this.configuration = mappedStatement.getConfiguration();
        this.parameterObject = parameterObject;
        this.boundSql = boundSql;
    }

    @Override
    public Object getParameterObject() {
        return parameterObject;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setParameters(PreparedStatement ps) throws SQLException {
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (Objects.isNull(parameterMappings)) {
            return;
        }
        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            Object value;
            String propertyName = parameterMapping.getProperty();
            if (Objects.isNull(parameterObject)) {
                value = null;
            } else if (configuration.getTypeHandlerRegistry().hasTypeHandler(parameterObject.getClass())) {
                value = parameterObject;
            } else if (parameterObject instanceof Map) {
                //parameterObject为map，org.apache.ibatis.reflection.ParamNameResolver.getNamedParams
                value = ((Map) parameterObject).get(propertyName);
            } else {
                //pojo对象，通过反射获取对象值
                try {
                    value = FieldUtils.readField(parameterObject, propertyName, true);
                } catch (Exception e) {
                    throw new TypeException(e);
                }
            }
            parameterMapping.getTypeHandler().setParameter(ps, i + 1, value);
        }
    }
}
