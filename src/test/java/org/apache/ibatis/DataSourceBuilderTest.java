package org.apache.ibatis;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * DataSource工具类
 *
 * @author furious 2024/4/8
 */
public abstract class DataSourceBuilderTest {

    public static DataSource build() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        config.setUsername("root");
        config.setPassword("your mysql password");
        return new HikariDataSource(config);
    }
}
