package com.tuplejump.inventorymanagement;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by shruti on 8/12/15.
 */
public class DBSupport {
    JdbcConnectionPool jdbcConnectionPool;

    public DBSupport(JdbcConnectionPool jdbcConnectionPool) {
        this.jdbcConnectionPool = jdbcConnectionPool;
    }

    public void update(String sql, Collection<Object> objects) {
        try {
            PreparedStatement ppstmt = null;
            int i = 1;
            Connection con = jdbcConnectionPool.getConnection();
            con.setAutoCommit(false);
            ppstmt = con.prepareStatement(sql);
            Iterator<Object> itr = objects.iterator();// getting Iterator from array
            // list to traverse elements
            Object object = null;
            while (itr.hasNext()) {
                object = itr.next();
                if (object instanceof String)
                    ppstmt.setString(i, object.toString());
                else if (object instanceof Integer)
                    ppstmt.setInt(i, ((Integer) object).intValue());
                i++;
            }
            ppstmt.executeUpdate();
            con.commit();
            ppstmt.close();
            con.setAutoCommit(true);
            con.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Item execute(String sql, ResultSetCallback resultSetCallback, Collection<Object> objects) {
        Item item;
        try {
            PreparedStatement ppstmt = null;
            int i = 1;
            Connection con = jdbcConnectionPool.getConnection();
            con.setAutoCommit(false);
            ppstmt = con.prepareStatement(sql);
            Iterator<Object> itr = objects.iterator();// getting Iterator from array
            // list to traverse elements
            Object object = null;
            while (itr.hasNext()) {
                object = itr.next();
                if (object instanceof String)
                    ppstmt.setString(i, object.toString());
                else if (object instanceof Integer)
                    ppstmt.setInt(i, ((Integer) object).intValue());
                i++;
            }
            ResultSet rs = ppstmt.executeQuery();
            con.commit();
            item = resultSetCallback.doWithResultSet(rs);
            rs.close();
            ppstmt.close();

            con.setAutoCommit(true);
            con.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return item;
    }

}
