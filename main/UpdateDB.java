package com.tuplejump.inventorymanagement;

/**
 * Created by shruti on 8/11/15.
 */
import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.Statement;
public class UpdateDB {

    public void update(JdbcConnectionPool jdbcConnectionPool,String sql){
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
}
