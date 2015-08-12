package com.tuplejump.inventorymanagement;

/**
 * Created by shruti on 8/10/15.
 */


import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.jdbcx.JdbcConnectionPool;

public class DBInventory implements Inventory, OrderManager {

    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/test";

    JdbcConnectionPool jdbcConnectionPool;
    DBSupport dbSupport;

    public DBInventory() {

        try {
            Class.forName(JDBC_DRIVER);
            jdbcConnectionPool = JdbcConnectionPool.create(DB_URL, "username", "password");
            dbSupport = new DBSupport(jdbcConnectionPool);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String sql = "CREATE TABLE IF NOT EXISTS table1 " +
                "(code VARCHAR(255) not NULL, " +
                " quantity INTEGER, " +
                " type VARCHAR(255), " +
                " make VARCHAR(255), " +
                " PRIMARY KEY ( code ))";
        dbSupport.update(sql);
    }

    public void deleteDatabase() {
        String sql = "DROP TABLE table1 ";
        dbSupport.update(sql);
        jdbcConnectionPool.dispose();
    }

    public Item searchItem(final String code) {// return null if item not present
        Item item;
        String sql = "SELECT code,quantity,type,make FROM table1";
        ResultSetCallback resultSetCallback = new ResultSetCallback() {
            public Item doWithResultSet(ResultSet rs) throws SQLException {
                while (rs.next()) {
                    if ((rs.getString("code")).equals(code)) {
                        Item item = new Item();
                        item.setCode(code);
                        item.setQuantity(rs.getInt("quantity"));
                        item.setMake(rs.getString("make"));
                        item.setType(rs.getString("type"));
                        return item;
                    }
                }
                Item item = null;
                return item;
            }
        };
        item = dbSupport.execute(sql, resultSetCallback);
        return item;
    }

    public void addItem(Item item) {
        String sql = "INSERT INTO table1 VALUES ('" + item.getCode() + "', " + Integer.toString(item.getQuantity()) + " ,'" + item.getType() + "','" + item.getMake() + "')";
        dbSupport.update(sql);
    }

    public boolean placeOrder(String OrderCode, int quantity) {
        boolean canOrder = false;
        Item item = searchItem(OrderCode);
        if (item != null) {
            if (item.getQuantity() >= quantity) {
                item.setQuantity(item.getQuantity() - quantity);
                String sql = "DELETE FROM table1 WHERE code = '" + OrderCode + "'";
                dbSupport.update(sql);
                sql = "INSERT INTO table1 VALUES ('" + item.getCode() + "', " + Integer.toString(item.getQuantity()) + " ,'" + item.getType() + "','" + item.getMake() + "')";
                dbSupport.update(sql);
                canOrder = true;
            } else
                canOrder = false;
        }
        return canOrder;
    }


    public boolean canPlaceOrder(String ItemCode, int quantity) {
        boolean canOrder = false;
        Item item = searchItem(ItemCode);
        if (item != null) {
            if (item.getQuantity() >= quantity) {
                canOrder = true;
            } else
                canOrder = false;
        }
        return canOrder;
    }
}
