package org.apache.ibatis.executor.resultset;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

/**
 * @author furious 2024/4/11
 */
public class ResultSetWrapper {

    private final ResultSet resultSet;
    private final TypeHandlerRegistry typeHandlerRegistry;
    private final List<String> columnLabels = new ArrayList<>();
    private final List<String> classNames = new ArrayList<>();
    private final List<Integer> jdbcTypes = new ArrayList<>();

    public ResultSetWrapper(ResultSet rs, Configuration configuration) throws SQLException {
        this.resultSet = rs;
        this.typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            columnLabels.add(metaData.getColumnLabel(i));
            classNames.add(metaData.getColumnClassName(i));
            jdbcTypes.add(metaData.getColumnType(i));
        }
    }

}
