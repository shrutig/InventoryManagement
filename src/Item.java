package com.tuplejump.inventorymanagement;

public class Item {
	String code;
	String type;
	String make;
	int quantity;

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	

	public String getMake() {
		return make;
	}
	
	public void setMake(String make) {
		this.make = make;
	}
	
}
