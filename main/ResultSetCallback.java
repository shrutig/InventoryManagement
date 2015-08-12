package com.tuplejump.inventorymanagement;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by shruti on 8/12/15.
 */
public interface ResultSetCallback {

    public Item doWithResultSet(ResultSet rs)throws SQLException;
}
