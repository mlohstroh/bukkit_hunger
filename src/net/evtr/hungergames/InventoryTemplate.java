package net.evtr.hungergames;

import java.util.Vector;

import org.bukkit.inventory.ItemStack;

public class InventoryTemplate 
{
	public static int nextID = 1;
	public int chance; // 0 to 100. Not definite.
	public Vector<ItemStack> items;
	public int id;
	public int maxItems;
	
	public InventoryTemplate() 
	{ 
		items = new Vector<ItemStack>();
	}
	
	public InventoryTemplate(ItemStack[] items, int chance, int max) 
	{
		this.maxItems = max;
		this.items = new Vector<ItemStack>();
		this.chance = chance;
		for ( int i = 0; i < Math.min(items.length, 27); i++ ) 
		{
			this.items.add(items[i]);
		}
		id = ++nextID;
	}
}