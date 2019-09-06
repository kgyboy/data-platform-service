package com.baiwang.dataplatform.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @ClassName ConnectionFactory
 * @Description //TODO
 * @Author yangsibiao
 * @Date 2019/9/2
 **/
@Component
public class ConnectionFactory {
    @Value("${hive.driver}")
    private String HIVE_DRIVER;
    @Value("${hive.url}")
    private String HIVE_URL;
    @Value("${hive.username}")
    private String HIVE_USERNAME;
    @Value("${hive.password}")
    private String HIVE_PASSWORD;


    @Bean
    public Connection conn() {
        Connection conn = null;
        try {
            Class.forName(HIVE_DRIVER);
            if (conn == null)
//                conn = DriverManager.getConnection(HIVE_URL);
                conn = DriverManager.getConnection(HIVE_URL, HIVE_USERNAME, HIVE_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

}
