package com.tuplejump.inventorymanagement;

public interface Inventory {
	/**
	 * Helps to search an item in the inventory
	 * @param code
	 * @return An Item if found. Null otherwise.
	 */
	public Item searchItem(String code);
	
	public void addItem(Item item);

	public void changeQuantity(String code,int quantity);
}
