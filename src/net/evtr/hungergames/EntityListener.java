/*The MIT License

Copyright (c) 2012 Mark Lohstroh

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package net.evtr.hungergames;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
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
		//throttling the creature spawning to only eggs
		if(event.getSpawnReason() != SpawnReason.SPAWNER_EGG)
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void OnPlayerInteract(PlayerInteractEvent event)
	{
		
	}
	
	@EventHandler
	public void OnPlayerDeath(PlayerDeathEvent event)
	{
		
		Player player = ((Player)event.getEntity());
		HungerPlayer killer = null;
		if(player.getKiller() != null)
		{
			if(plugin.currentGame != null)
			{
				killer = plugin.currentGame.getPlayer(player);
			}
		}

		if(plugin.currentGame != null)
		{
			HungerPlayer hPlayer = plugin.currentGame.getPlayer(player);
			//simulate the cannon :)
			plugin.getServer().getWorld("world").setThundering(true);
			plugin.getServer().getWorld("world").setThunderDuration(10);
			if (hPlayer != null) 
			{
				hPlayer.mIsDied = true;
				//add the player to the sponser list
				plugin.currentGame.AddSponser(new HungerSponser(hPlayer));
				player.sendMessage(ChatColor.GOLD + "You may now become a sponser for someone!");
				player.sendMessage(ChatColor.GOLD + "Type /hg s <playername> to sponser the player of your choice!");
				//TODO: Print the leftover players here
				player.sendMessage(ChatColor.GOLD + "You may only sponser one person at a time and your tribute may die!");
				if(killer != null)
				{
					killer.KilledPlayer(hPlayer);
				}
			}
		}
	}
}
