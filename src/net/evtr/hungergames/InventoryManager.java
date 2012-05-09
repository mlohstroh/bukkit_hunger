package net.evtr.hungergames;

import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {
	
	public class VirtualChest {
		public Chest chest;
		
		public class Element {
			public Inventory inventory;
			public Player player;
			
			public Element(Player player, Inventory inventory) {
				this.player = player;
				this.inventory = inventory;
			}
		}
		
		public Vector<Element> playerChests;
		
		public Inventory getInventory(Player player) {
			for ( int i = 0; i < playerChests.size(); i++ ) {
				if ( playerChests.get(i).player == player ) {
					player.sendMessage("Hello again.");
					return playerChests.get(i).inventory;
				}
			}
			player.sendMessage("New here?");
			Inventory inventory = Bukkit.createInventory(chest, InventoryType.CHEST);
			inventory.addItem(new ItemStack(Material.COOKED_BEEF, (int)(Math.random()*64))); // Testing.
			Element ele = new Element(player, inventory);
			playerChests.add(ele);
			return ele.inventory;
		}
		
		public VirtualChest(Chest chest) {
			this.chest = chest;
			playerChests = new Vector<Element>();
		}
	}
	private Vector<VirtualChest> chests;
	
	public Inventory getInventory(Player player, Chest chest) {
		return getChest(chest).getInventory(player);
	}
	
	public VirtualChest getChest(Chest chest) {
		for ( int i = 0; i < chests.size(); i++ ) {
			Block c1 = chests.get(i).chest.getBlock(), c2 = chest.getBlock(); 
			// chest == other chest doesn't work?
			if ( c1.getX() == c2.getX() && c1.getY() == c2.getY() && c1.getZ() == c2.getZ() ) {
				return chests.get(i);
			}
		}
		
		VirtualChest newChest = new VirtualChest(chest);
		chests.add(newChest);
		return newChest;
	}
	
	public InventoryManager() {
		chests = new Vector<VirtualChest>();
	}
}
