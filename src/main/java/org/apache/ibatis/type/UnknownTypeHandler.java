package org.apache.ibatis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Objects;

import org.apache.commons.lang3.ClassUtils;

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

    @Override
    protected Object getNullableResult(ResultSet rs, String columnLabel) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        int columnIndex = -1;
        for (int i = 1; i < columnCount; i++) {
            if (Objects.equals(metaData.getColumnLabel(i), columnLabel)) {
                columnIndex = i;
                break;
            }
        }

        TypeHandler<?> handler = null;
        if (columnIndex != -1) {
            handler = typeHandlerRegistry.getTypeHandler(safeGetClassForColumn(metaData, columnIndex));
        }
        if (handler == null || handler instanceof UnknownTypeHandler) {
            handler = OBJECT_TYPE_HANDLER;
        }
        return handler.getResult(rs, columnLabel);
    }

    private Class<?> safeGetClassForColumn(ResultSetMetaData metaData, Integer columnIndex) {
        try {
            return ClassUtils.getClass(metaData.getColumnClassName(columnIndex));
        } catch (Exception e) {
            return null;
        }
    }
}
