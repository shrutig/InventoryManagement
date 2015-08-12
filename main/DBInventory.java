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
        String sql = "CREATE TABLE IF NOT EXISTS inventorytable " +
                "(code VARCHAR(255) not NULL, " +
                " quantity INTEGER, " +
                " type VARCHAR(255), " +
                " make VARCHAR(255), " +
                " PRIMARY KEY ( code ))";
        dbSupport.update(sql);
    }

    public void deleteDatabase() {
        String sql = "DROP TABLE inventorytable ";
        dbSupport.update(sql);
        jdbcConnectionPool.dispose();
    }

    public Item searchItem(final String code) {// return null if item not present
        Item item;
        String sql = "SELECT code,quantity,type,make FROM inventorytable WHERE code ='"+code+"'";
        ResultSetCallback resultSetCallback = new ResultSetCallback() {
            public Item doWithResultSet(ResultSet rs) throws SQLException {
                if (rs.next()) {
                        Item item = new Item();
                        item.setCode(code);
                        item.setQuantity(rs.getInt("quantity"));
                        item.setMake(rs.getString("make"));
                        item.setType(rs.getString("type"));
                        return item;
                }
                Item item = null;
                return item;
            }
        };
        item = dbSupport.execute(sql, resultSetCallback);
        return item;
    }

    public void addItem(Item item) {
        String sql = "INSERT INTO inventorytable VALUES ('" + item.getCode() + "', " + Integer.toString(item.getQuantity()) + " ,'" + item.getType() + "','" + item.getMake() + "')";
        if(searchItem(item.getCode())== null)
        dbSupport.update(sql);
    }

    public boolean placeOrder(String OrderCode, int quantity) {
        boolean canOrder = false;
        Item item = searchItem(OrderCode);
        if (item != null) {
            if (item.getQuantity() >= quantity) {
                changeQuantity(OrderCode,item.getQuantity() - quantity);
                canOrder = true;
            } else
                canOrder = false;
        }
        return canOrder;
    }

    public void changeQuantity(String code,int quantity){
        String sql = "UPDATE inventorytable SET quantity="+ Integer.toString(quantity) +" WHERE code = '" + code + "'";
        dbSupport.update(sql);
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
