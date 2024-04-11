package org.apache.ibatis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author furious 2024/4/9
 */
public class IntegerTypeHandler extends BaseTypeHandler<Integer> {
    @Override
    protected void setNonNullParameter(PreparedStatement ps, int i, Integer parameter) throws SQLException {
        ps.setInt(i, parameter);
    }

    @Override
    protected Integer getNullableResult(ResultSet rs, String columnLabel) throws SQLException {
        return rs.getInt(columnLabel);
    }
}
