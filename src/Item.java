package com.tuplejump.inventorymanagement;

public class Item {
    String code;
    String type;
    String make;
    int quantity;

    public String getCode() {
        return code;
    }

    public synchronized void setCode(String code) {
        this.code = code;
    }

    public synchronized int getQuantity() {
        return quantity;
    }

    public synchronized void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public synchronized String getType() {
        return type;
    }

    public synchronized void setType(String type) {
        this.type = type;
    }


    public synchronized String getMake() {
        return make;
    }

    public synchronized void setMake(String make) {
        this.make = make;
    }

}
