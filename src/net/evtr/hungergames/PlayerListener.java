package net.evtr.hungergames;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener
{
	HungerGames hgInstance;
	
	public PlayerListener(HungerGames instance)
	{
		hgInstance = instance;
	}
	
	@EventHandler
	public void PlayerDied(EntityDeathEvent event)
	{
		if (event.getEntity() instanceof Player)
		{
			//then a player died so let's add him to the list
			hgInstance.killedPlayers.add((Player)event.getEntity());
			Player player = (Player)event.getEntity();
			player.sendMessage(ChatColor.AQUA + "You are now allowed to spectate! /s");
		}
	}
	
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent event)
	{
		
	}
}
