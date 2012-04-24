package net.evtr.hungergames;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EntityListener implements Listener 
{
	private HungerGames plugin;
	public HashMap<Integer, Location> startingLocations;
	private int lastLocationIndex = 0;
	
	public EntityListener(HungerGames instance)
	{
		plugin = instance;
		startingLocations = new HashMap<Integer, Location>();
	}
	
	public void CreatureSpawn(CreatureSpawnEvent event)
	{
		
	}
	
	@EventHandler
	public void OnPlayerInteract(PlayerInteractEvent event)
	{
		//this will be temporary for setting starting positions
		if(event.getAction() == Action.LEFT_CLICK_BLOCK)
		{
			if(event.getPlayer().isOp())
			{
				plugin.log.info("Location:" + event.getClickedBlock().getLocation().toString());
				startingLocations.put(lastLocationIndex, event.getClickedBlock().getLocation());
				lastLocationIndex++;
			}
		}
	}
	
	public void savepos()
	{
		File dataFolder = plugin.getDataFolder();
		if(!dataFolder.exists())
		{
			if(dataFolder.mkdir())
			{
				plugin.log.info("Creating data folder for HungerGames");
			}
			else
			{
				plugin.log.warning("Could not create folder for HungerGames! Check folder permissions!");
			}
		}
		
		File positions = new File(plugin.getDataFolder() + File.separator + "positions.txt");
		try 
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(positions));
			
			for(int i = 0; i < startingLocations.size(); i++)
			{
				Location location = startingLocations.get(i);
				out.write(Integer.valueOf(i) + ":" + location.toString());
				out.newLine();
			}
			out.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
