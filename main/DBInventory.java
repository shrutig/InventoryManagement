package com.tuplejump.inventorymanagement;

/**
 * Created by shruti on 8/10/15.
 */


//import javax.sql.ConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;

public class DBInventory implements Inventory, OrderManager {
    ObjectClose<Connection> objcon;
    ObjectClose<Statement> objstmt;
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/test";
    //private DataSource datasource;
    JdbcConnectionPool jdbcConnectionPool;

    public DBInventory() {
        objcon = new ObjectClose<Connection>(){};
        objstmt = new ObjectClose<Statement>(){};
        try {
            Class.forName(JDBC_DRIVER);
           /* con = DriverManager.getConnection(DB_URL, "username", "password");
            InitialContext initContext  = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            datasource = (DataSource) envContext .lookup("jdbc/MyDataSource");*/
            jdbcConnectionPool = JdbcConnectionPool.create(DB_URL, "username", "password");

            Connection con = jdbcConnectionPool.getConnection();

            Statement stmt = con.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS table1 " +
                    "(code VARCHAR(255) not NULL, " +
                    " quantity INTEGER, " +
                    " type VARCHAR(255), " +
                    " make VARCHAR(255), " +
                    " PRIMARY KEY ( code ))";

            stmt.executeUpdate(sql);
            objcon.CloseOb(con);
            objstmt.CloseOb(stmt);
            //obj.closeConnection(con);
            //obj.closeStatement(stmt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public void deleteDatabase() {
        String sql = "DROP TABLE table1 ";
        try {
            Connection con = jdbcConnectionPool.getConnection();

            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
            objcon.CloseOb(con);
            objstmt.CloseOb(stmt);
           /* obj.closeConnection(con);
            obj.closeStatement(stmt);*/
            jdbcConnectionPool.dispose();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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


                    return item;
                }

            }
            itr.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            objcon.CloseOb(con);
            objstmt.CloseOb(stmt);
           /* obj.closeConnection(con);
            obj.closeStatement(stmt);*/


        }
        item = null;
        return item;
    }

    public void addItem(Item item) {
        try {


            String sql = "INSERT INTO table1 VALUES ('" + item.getCode() + "', " + Integer.toString(item.getQuantity()) + " ,'" + item.getType() + "','" + item.getMake() + "')";
            Connection con = jdbcConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
            objcon.CloseOb(con);
            objstmt.CloseOb(stmt);
         /*   obj.closeConnection(con);
            obj.closeStatement(stmt);*/
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public boolean placeOrder(String OrderCode, int quantity) {
        boolean canOrder = false;
        try {

            Item item = searchItem(OrderCode);
            if (item != null) {
                if (item.getQuantity() >= quantity) {
                    item.setQuantity(item.getQuantity() - quantity);
                    Connection con = jdbcConnectionPool.getConnection();
                    Statement stmt = con.createStatement();
                    String sql = "DELETE FROM table1 WHERE code = '" + OrderCode + "'";
                    stmt.executeUpdate(sql);
                    sql = "INSERT INTO table1 VALUES ('" + item.getCode() + "', " + Integer.toString(item.getQuantity()) + " ,'" + item.getType() + "','" + item.getMake() + "')";
                    stmt.executeUpdate(sql);
                    canOrder = true;
                    objcon.CloseOb(con);
                    objstmt.CloseOb(stmt);
                   /* obj.closeConnection(con);
                    obj.closeStatement(stmt);*/
                } else
                    canOrder = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return canOrder;
        }
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
