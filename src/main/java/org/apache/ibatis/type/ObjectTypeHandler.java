package org.apache.ibatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author furious 2024/4/9
 */
public class ObjectTypeHandler extends BaseTypeHandler<Object> {
    @Override
    protected void setNonNullParameter(PreparedStatement ps, int i, Object parameter) throws SQLException {
        ps.setObject(i, parameter);
    }
}
