package com.tuplejump.inventorymanagement;

public enum Make {
    MAKE1, MAKE2;

    public String toString() {
        String s = "";
        switch (this) {
            case MAKE1:
                s = "MAKE1";
            case MAKE2:
                s = "MAKE2";
        }
        return s;
    }
}
