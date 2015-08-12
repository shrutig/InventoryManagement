package com.tuplejump.inventorymanagement;

import java.util.*;

public class InMemInventory implements Inventory, OrderManager {

    Collection<Item> items = new ArrayList<Item>();

    public Item searchItem(String code) {// return null if item not present
        Iterator<Item> itr = items.iterator();// getting Iterator from array
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
        items.add(item);
    }

    public void changeQuantity(String code, int quantity) {
        Item item = searchItem(code);
        if (item != null) {
            items.remove(item);
            item.setQuantity(quantity);
            items.add(item);
        }
    }

    public boolean placeOrder(String code, int quantity) {
        boolean canOrder = false;
        Item item = searchItem(code);
        if (item != null) {
            if (item.getQuantity() >= quantity) {
                changeQuantity(code, item.getQuantity() - quantity);
                canOrder = true;
            } else
                canOrder = false;
        }
        return canOrder;
    }

    public boolean canPlaceOrder(String code, int quantity) {
        boolean canOrder = false;
        Item item = searchItem(code);
        if (item != null) {
            if (item.getQuantity() >= quantity) {
                canOrder = true;
            } else
                canOrder = false;
        }
        return canOrder;
    }
}
