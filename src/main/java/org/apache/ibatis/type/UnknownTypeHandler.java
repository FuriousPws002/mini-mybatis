package org.apache.ibatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author furious 2024/4/9
 */
public class UnknownTypeHandler extends BaseTypeHandler<Object> {

    private static final ObjectTypeHandler OBJECT_TYPE_HANDLER = new ObjectTypeHandler();

    private final TypeHandlerRegistry typeHandlerRegistry;

    public UnknownTypeHandler(TypeHandlerRegistry typeHandlerRegistry) {
        this.typeHandlerRegistry = typeHandlerRegistry;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void setNonNullParameter(PreparedStatement ps, int i, Object parameter) throws SQLException {
        TypeHandler handler = typeHandlerRegistry.getTypeHandler(parameter.getClass());
        if (Objects.isNull(handler) || handler instanceof UnknownTypeHandler) {
            handler = OBJECT_TYPE_HANDLER;
        }
        handler.setParameter(ps, i, parameter);
    }
}
