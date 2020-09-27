package com.bigdata.es.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @ description:
 * @ author: spencer
 * @ date: 2020/9/27 9:20
 */
public class OracleUtil {

    private static final String ORACLE_URL = "jdbc:oracle:thin:@wbbigdata00:1521:orcl";
    private static final String USER_NAME = "scott";
    private static final String PASSWORD = "bigdata";
    // 驱动名称
    private static final String JDBC_NAME = "oracle.jdbc.OracleDriver";

    public static Connection getConnection(){
        Connection connection = null;
        try {
            try {
                Class.forName(JDBC_NAME);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            connection = DriverManager.getConnection(ORACLE_URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public void close(Connection connection){

        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        System.out.println(getConnection());
    }
}
