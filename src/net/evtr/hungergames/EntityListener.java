package net.evtr.hungergames;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EntityListener implements Listener 
{
	private HungerGames plugin;
	
	public EntityListener(HungerGames instance)
	{
		plugin = instance;
	}
	
	public void CreatureSpawn(CreatureSpawnEvent event)
	{
		
	}
	
	@EventHandler
	public void OnPlayerInteract(PlayerInteractEvent event)
	{

	}
}
