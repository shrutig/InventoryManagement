package com.tuplejump.inventorymanagement;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by shruti on 8/12/15.
 */
public class DBSupport {
    JdbcConnectionPool jdbcConnectionPool;
    public DBSupport(JdbcConnectionPool jdbcConnectionPool){
        this.jdbcConnectionPool =jdbcConnectionPool;
    }
    public void update(String sql){
        try {
            Connection con = jdbcConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            con.close();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public Item execute(String sql,ResultSetCallback resultSetCallback){
        Item item;
        try {
            Connection con = jdbcConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            item= resultSetCallback.doWithResultSet(rs);
            rs.close();
            stmt.close();
            con.close();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        return item;
    }

}
