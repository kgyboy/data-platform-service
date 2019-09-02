package com.baiwang.dataplatform.common;

import org.apache.hive.jdbc.HiveStatement;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;

/**
 * @ClassName ConnectionFactory
 * @Description //TODO
 * @Author yangsibiao
 * @Date 2019/9/2
 **/
public class ConnectionFactory {

    private static String HIVE_DRIVER = "org.apache.hive.jdbc.HiveDriver";
    private static String HIVE_URL = "jdbc:hive2://192.168.6.64:10000/default";

    private static String DRIVER = "com.mysql.jdbc.Driver";
    private static String URL = "jdbc:mysql://192.168.6.141:3306/contract?useUnicode=true&allowMultiQuerie=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false";
    private static String USERNAME = "root";
    private static String PASSWORD = "localTest1717";


    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(DRIVER);
            if (conn == null)
//                conn = DriverManager.getConnection(HIVE_URL);
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }


    public static PreparedStatement prepare(Connection conn, String sql) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }

    public static void main(String[] args) {
        String sql = "SHOW TABLES";
//        sql = "SEELCT COUNT(1) FROM kylin_sales";
        sql = "DESC kylin_sales";
        List<String> list = HiveUtils.query(sql, null, new HiveUtils.ResultSetHandler<String>() {
            @Override
            public String processRs(ResultSet rs) throws SQLException {
                return rs.getString("field");
            }
        });

        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + "===" + list.get(i));
        }
    }

}
