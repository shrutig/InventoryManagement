package com.tuplejump.inventorymanagement;

/**
 * Created by shruti on 8/10/15.
 */

import static org.junit.Assert.assertFalse;

import org.junit.*;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DBInventoryManagementTest {
    DBInventory dbMem;
    Item item;

    @Before
    public void initialise() {
        dbMem = new DBInventory();

        item = new Item();
        item.setCode("code1");
        item.setQuantity(20);
        item.setMake("MAKE1");
        item.setType("TYPE1");


        dbMem.addItem(item);
    }

    @Test
    public void testSearchItem() {
        Item item2 = dbMem.searchItem("code1");
        boolean b = false;
        if (item2.getMake().equals(item.getMake()) && item2.getType().equals(item.getType()) && item2.getQuantity() == item.getQuantity()) {
            b = true;
        }
        assertTrue(b);
    }

    @Test
    public void testCanPlaceOrderPresent(){
        Item item2 = dbMem.searchItem("code1");
        item2.setMake("MAKE2");
        item2.setCode("code3");
        dbMem.addItem(item2);
        assertTrue(dbMem.canPlaceOrder("code3", 20));// as this item just added
    }

    @Test
    public void testCanPlaceOrderAbsent() {
        boolean b;
        b = dbMem.canPlaceOrder("code3", 20);
        assertFalse(b);// As code3 added in @Test 1 and not in @Before code
    }

    @Test
    public void testPlaceOrder() {
        boolean b;
        b = dbMem.placeOrder("code1", 20);
        assertTrue(b);//as order successful
        b = dbMem.canPlaceOrder("code1", 20);
        assertFalse(b);// As no more of code1 left
    }

    @After
    public void CloseDatabase(){
       dbMem.deleteDatabase();
    }

}
