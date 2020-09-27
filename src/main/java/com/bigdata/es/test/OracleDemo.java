package com.bigdata.es.test;

import com.bigdata.es.utils.OracleUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @ description:
 * @ author: spencer
 * @ date: 2020/9/27 9:32
 */
public class OracleDemo {

    private static Connection connection = null;
    private static PreparedStatement pstmt = null;
    private static ResultSet rs = null;
    public static void main(String[] args) {
        select();
    }

    /**
     * 查询oracle数据
     */
    private static void select() {
        connection = OracleUtil.getConnection();
        String sql = "select * from dept";
        try {
            pstmt = connection.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()){
                int deptno = rs.getInt(1);
                String dname = rs.getString(2);
                String loc = rs.getString(3);
                System.out.println("DEPTNO: " + deptno + ", DNAME: " + dname + ", LOC: " + loc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
