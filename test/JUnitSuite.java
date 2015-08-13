package com.tuplejump.inventorymanagement;

/**
 * Created by shruti on 8/10/15.
 */

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        InventoryManagementTest.class,
        DBInventoryManagementTest.class
})
public class JUnitSuite {
}

