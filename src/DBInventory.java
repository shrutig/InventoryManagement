package com.tuplejump.inventorymanagement;

/**
 * Created by shruti on 8/10/15.
 */


import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

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
        Collection<Object> objects = new ArrayList<Object>();
        dbSupport.update(sql, objects);
    }

    public void deleteDatabase() {
        String sql = "DROP TABLE inventorytable ";
        Collection<Object> objects = new ArrayList<Object>();
        dbSupport.update(sql, objects);
        jdbcConnectionPool.dispose();
    }

    public Item searchItem(final String code) {// return null if item not present
        Item item;
        Collection<Object> objects = new ArrayList<Object>();
        objects.add(code);
        String sql = "SELECT code,quantity,type,make FROM inventorytable WHERE code = ?";
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
        item = dbSupport.execute(sql, resultSetCallback, objects);
        return item;
    }

    public void addItem(Item item) {
        PreparedStatement updateItem = null;
        Collection<Object> objects = new ArrayList<Object>();
        String sql = "INSERT INTO inventorytable VALUES (?,?,?,?)";
        objects.add(item.getCode());
        objects.add(new Integer(item.getQuantity()));
        objects.add(item.getType());
        objects.add(item.getMake());
        if (searchItem(item.getCode()) == null)
            dbSupport.update(sql, objects);
    }

    public synchronized boolean placeOrder(String OrderCode, int quantity) {
        boolean canOrder = false;
        Item item = searchItem(OrderCode);
        if (item != null) {
            if (item.getQuantity() >= quantity) {
                changeQuantity(OrderCode, item.getQuantity() - quantity);
                canOrder = true;
            } else
                canOrder = false;
        }
        return canOrder;
    }

    public void changeQuantity(String code, int quantity) {
        String sql = "UPDATE inventorytable SET quantity=? WHERE code =?";
        Collection<Object> objects = new ArrayList<Object>();
        objects.add(new Integer(quantity));
        objects.add(code);
        dbSupport.update(sql, objects);
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
