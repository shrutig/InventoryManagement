package com.tuplejump.inventorymanagement;


import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
public class InventoryManagementTest {
	
		InMemInventory inMem = new InMemInventory();
		Item item = new Item();

		@Before
		public void initialise() {
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
	}
