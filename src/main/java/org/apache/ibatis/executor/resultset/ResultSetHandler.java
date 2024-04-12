package org.apache.ibatis.executor.resultset;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author furious 2024/4/11
 */
public interface ResultSetHandler {

    <T> List<T> handleResultSets(Statement stmt) throws SQLException;
}
