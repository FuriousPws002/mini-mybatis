package org.apache.ibatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 参数类型处理接口
 *
 * @author furious 2024/4/9
 */
public interface TypeHandler<T> {

    void setParameter(PreparedStatement ps, int i, T parameter) throws SQLException;
}
