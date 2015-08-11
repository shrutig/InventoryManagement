package com.tuplejump.inventorymanagement;

/**
 * Created by shruti on 8/10/15.
 */


import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.Statement;

import org.h2.jdbcx.JdbcConnectionPool;

public class DBInventory implements Inventory, OrderManager {

    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/test";

    JdbcConnectionPool jdbcConnectionPool;
    UpdateDB updateDB;

    public DBInventory() {
        updateDB = new UpdateDB();
        try {
            Class.forName(JDBC_DRIVER);
            jdbcConnectionPool = JdbcConnectionPool.create(DB_URL, "username", "password");
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        String sql = "CREATE TABLE IF NOT EXISTS table1 " +
                "(code VARCHAR(255) not NULL, " +
                " quantity INTEGER, " +
                " type VARCHAR(255), " +
                " make VARCHAR(255), " +
                " PRIMARY KEY ( code ))";
        updateDB.update(jdbcConnectionPool, sql);
    }

    public void deleteDatabase() {
        String sql = "DROP TABLE table1 ";
        updateDB.update(jdbcConnectionPool, sql);
        jdbcConnectionPool.dispose();
    }

    public Item searchItem(String code) {// return null if item not present
        Item item = new Item();
        Statement stmt = null;
        Connection con = null;
        try {
            con = jdbcConnectionPool.getConnection();
            stmt = con.createStatement();
            ResultSet itr = stmt.executeQuery("SELECT code,quantity,type,make FROM table1");
            while (itr.next()) {
                String code1 = itr.getString("code");
                item.setCode(code1);
                item.setQuantity(itr.getInt("quantity"));
                item.setMake(itr.getString("make"));
                item.setType(itr.getString("type"));
                if ((item.getCode()).equals(code)) {
                    stmt.close();
                    con.close();
                    return item;
                }
            }
            itr.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        item = null;
        return item;
    }

    public void addItem(Item item) {
        String sql = "INSERT INTO table1 VALUES ('" + item.getCode() + "', " + Integer.toString(item.getQuantity()) + " ,'" + item.getType() + "','" + item.getMake() + "')";
        updateDB.update(jdbcConnectionPool, sql);
    }

    public boolean placeOrder(String OrderCode, int quantity) {
        boolean canOrder = false;
        Item item = searchItem(OrderCode);
        if (item != null) {
            if (item.getQuantity() >= quantity) {
                item.setQuantity(item.getQuantity() - quantity);
                String sql = "DELETE FROM table1 WHERE code = '" + OrderCode + "'";
                updateDB.update(jdbcConnectionPool, sql);
                sql = "INSERT INTO table1 VALUES ('" + item.getCode() + "', " + Integer.toString(item.getQuantity()) + " ,'" + item.getType() + "','" + item.getMake() + "')";
                updateDB.update(jdbcConnectionPool, sql);
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
