package org.apache.ibatis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

import org.apache.ibatis.executor.result.ResultMapException;

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

    @Override
    public T getResult(ResultSet rs, String columnLabel) throws SQLException {
        try {
            return getNullableResult(rs, columnLabel);
        } catch (Exception e) {
            throw new ResultMapException(e);
        }
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

    /**
     * 获取ResultSet中指定列的值
     *
     * @param rs          ResultSet
     * @param columnLabel as别名值，无as时就是列名
     * @return 获取值
     */
    protected abstract T getNullableResult(ResultSet rs, String columnLabel) throws SQLException;
}
