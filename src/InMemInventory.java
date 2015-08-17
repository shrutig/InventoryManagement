package com.tuplejump.inventorymanagement;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemInventory implements Inventory, OrderManager {

    volatile CopyOnWriteArrayList<Item> items = new CopyOnWriteArrayList<Item>();

    public Item searchItem(String code) {// return null if item not present
        Iterator<Item> itr;
        itr = items.iterator();// getting Iterator from array
        // list to traverse elements
        Item item = null;
        while (itr.hasNext()) {
            item = (Item) itr.next();
            if ((item.getCode()).equals(code)) {
                return item;
            }
        }
        item = null;
        return item;
    }

    public void addItem(Item item) {
        synchronized (items) {
            items.add(item);
        }
    }

    public void changeQuantity(String code, int quantity) {
        Item item = searchItem(code);
        synchronized (item) {

            int index = items.indexOf(item);

            if (item != null) {
                item.setQuantity(quantity);
                items.set(index, item);
            }
        }
    }

    public boolean placeOrder(String code, int quantity) {
        boolean isOrdered = false;
        Item item = searchItem(code);
        synchronized (item) {

            if (item != null) {
                if (item.getQuantity() >= quantity) {
                    changeQuantity(code, item.getQuantity() - quantity);
                    isOrdered = true;
                } else
                    isOrdered = false;
            }
        }
            return isOrdered;

    }

    public boolean canPlaceOrder(String code, int quantity) {
        boolean canOrder = false;
        Item item = searchItem(code);

            if (item != null) {
                ;
                if (item.getQuantity() >= quantity) {
                    canOrder = true;

                } else {
                    canOrder = false;
                }
            }

        return canOrder;
    }
}
