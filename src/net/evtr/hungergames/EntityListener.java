package net.evtr.hungergames;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EntityListener implements Listener 
{
	private HungerGames plugin;
	
	public EntityListener(HungerGames instance)
	{
		plugin = instance;
	}
	
	@EventHandler
	public void CreatureSpawn(CreatureSpawnEvent event)
	{
		
	}
	
	@EventHandler
	public void OnPlayerInteract(PlayerInteractEvent event)
	{

	}
	
	@EventHandler
	public void OnPlayerDeath(PlayerDeathEvent event)
	{
		plugin.log.info("player died...");
		plugin.log.info(event.getEntity().getDisplayName() + " has been killed by " + event.getEntity().getKiller().getDisplayName()); //TODO: raises error if not killed by player!
	}
}
