package com.tuplejump.inventorymanagement;

public interface OrderManager {
	public boolean placeOrder(String code, int quantity);
	public boolean canPlaceOrder(String code, int quantity);
}
