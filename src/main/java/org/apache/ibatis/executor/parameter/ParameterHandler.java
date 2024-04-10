package org.apache.ibatis.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author furious 2024/4/10
 */
public interface ParameterHandler {

    Object getParameterObject();

    void setParameters(PreparedStatement ps) throws SQLException;
}
