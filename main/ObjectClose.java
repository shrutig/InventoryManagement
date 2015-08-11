package com.tuplejump.inventorymanagement;

/**
 * Created by shruti on 8/11/15.
 */
import java.sql.Connection;
import java.sql.Statement;
public class ObjectClose<T> {

    public void CloseOb(T t){
        if(t instanceof Connection) {
            closeConnection((Connection) t);
        }

        else if(t instanceof Statement) {
            closeStatement((Statement) t);
        }

            }
    public void closeConnection(Connection con){
        try{

           con.close();
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    public void closeStatement(Statement stmt){
        try{

            stmt.close();
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
