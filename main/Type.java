package com.tuplejump.inventorymanagement;

public enum Type {
	TYPE1,TYPE2;
	public String toString(){
		String s="";
		switch(this){
		case TYPE1:
			s= "TYPE1";
		case TYPE2:
			s= "TYPE2";	
		}
		return s;
	}
}
