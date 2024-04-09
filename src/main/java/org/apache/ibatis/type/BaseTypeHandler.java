package org.apache.ibatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

/**
 * @author furious 2024/4/9
 */
public abstract class BaseTypeHandler<T> implements TypeHandler<T> {

    @Override
    public void setParameter(PreparedStatement ps, int i, T parameter) throws SQLException {
        if (Objects.isNull(parameter)) {
            ps.setNull(i, Types.NULL);
            return;
        }
        setNonNullParameter(ps, i, parameter);
    }

    /**
     * 设置非空值
     *
     * @param ps        PreparedStatement
     * @param i         1开始
     * @param parameter 参数值
     * @throws SQLException SQLException
     */
    protected abstract void setNonNullParameter(PreparedStatement ps, int i, T parameter) throws SQLException;
}
