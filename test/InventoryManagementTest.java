package com.tuplejump.inventorymanagement;


import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class InventoryManagementTest {

    InMemInventory inMem ;
    Item item ;

    @Before
    public void initialise() {
        inMem = new InMemInventory();
        item = new Item();
        item.setCode("code1");
        item.setQuantity(20);
        item.setMake("MAKE1");
        item.setType("TYPE1");
        inMem.addItem(item);

    }

    @Test
    public void testSearchItem1() {
        Item item2 = inMem.searchItem("code1");
        assertTrue(item.equals(item2));
        item2.setMake("MAKE2");
        item2.setCode("code3");
        inMem.addItem(item2);
        assertTrue(inMem.canPlaceOrder("code3", 20));// as this item just added
    }

    @Test
    public void testInMem() {
        boolean b = inMem.canPlaceOrder("code1", 20);
        assertTrue(b);
        b = inMem.canPlaceOrder("code2", 20);
        assertFalse(b);// code2 not in inventory
        b = inMem.placeOrder("code1", 20);
        assertTrue(b);//as order successful
        b = inMem.canPlaceOrder("code1", 20);
        assertFalse(b);// As no more of code1 left

    }

    @Test
    public void testMultiThreading() throws InterruptedException {
        // for (int run = 0, numberOfThreads = 2; run < 1000; run++) {
        int numberOfThreads = 2;
        final Item itemOrder = new Item();
        itemOrder.setQuantity(0);
        final List<Thread> threads = new ArrayList<Thread>(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            final Thread thread = new Thread(new Runnable() {
                //@Override
                public void run() {
                    synchronized (inMem) {
                        final boolean value = inMem.placeOrder("code1", 20);

                        if (value) {
                            synchronized (itemOrder) {
                                final int valueint = itemOrder.getQuantity();
                                itemOrder.setQuantity(valueint + 1);
                                //System.out.println("entry for " + itemOrder.getQuantity());
                            }
                        }
                    }
                }
            });
            thread.start();
            threads.add(thread);
        }

        for (final Thread thread : threads) {
            thread.join();
        }

        if (itemOrder.getQuantity() == numberOfThreads) {
            System.out.println("Failed while multithreading in inMem");
            // break;
        } else if (itemOrder.getQuantity() ==1){
            System.out.println("Success in inMem");
        }
    }

    @After
    public void deleteInv(){
        inMem = null;
        item = null;
    }
}
