package com.baiwang.dataplatform.config;

import java.sql.*;

/**
 * @ClassName HiveConfig
 * @Description //TODO
 * @Author yangsibiao
 * @Date 2019/8/30
 **/
public class HiveConfig {

    private static String driverName = "org.apache.hive.jdbc.HiveDriver";
//    private static String url = "jdbc:hive2://192.168.6.64:10000/default";
    private static String url = "jdbc:hive2://192.168.6.64:10000/default";
    //    private static String user = "";
    //    private static String password = "";
    private static String sql = "";
    private static ResultSet res;
    private static String DRIVER = "com.mysql.jdbc.Driver";
    private static String URL = "jdbc:mysql://192.168.6.141:3306/contract?useUnicode=true&allowMultiQuerie=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false";
//    private static String URL = "jdbc:mysql://192.168.6.141:3306/contract?useUnicode=true&allowMultiQuerie=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false";
    private static String USERNAME = "root";
    private static String PASSWORD = "localTest1717";


    public static void main(String[] args) {
        Connection conn = null;
//        Statement stmt = null;
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url);
//            stmt = conn.createStatement();
            String sql = "SELECT count(1) FROM kylin_sales";
//            sql = "SHOW TABLES";

            PreparedStatement statement = conn.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
//            try {
//                if (conn != null) {
//                    conn.close();
//                    conn = null;
//                }
//                if (stmt != null) {
//                    stmt.close();
//                    stmt = null;
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
        }
    }

    private static void countData(Statement stmt, String tableName)
            throws SQLException {
        sql = "select count(1) from " + tableName;
        System.out.println("Running:" + sql);
        res = stmt.executeQuery(sql);
        System.out.println("执行“regular hive query”运行结果:");
        while (res.next()) {
            System.out.println("count ------>" + res.getString(1));
        }
    }

    private static void selectData(Statement stmt, String tableName)
            throws SQLException {
        sql = "select * from " + tableName + " limit 10";
        System.out.println("Running:" + sql);
        res = stmt.executeQuery(sql);
        System.out.println("执行 select * query 运行结果:");
        while (res.next()) {
            System.out.println(res.getString(1) + "\t" + res.getString(2));
        }
    }

}

