package org.apache.ibatis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author furious 2024/4/9
 */
public class StringTypeHandler extends BaseTypeHandler<String> {
    @Override
    protected void setNonNullParameter(PreparedStatement ps, int i, String parameter) throws SQLException {
        ps.setString(i, parameter);
    }

    @Override
    protected String getNullableResult(ResultSet rs, String columnLabel) throws SQLException {
        return rs.getString(columnLabel);
    }
}
