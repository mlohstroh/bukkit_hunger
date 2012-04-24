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

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.logging.Logger;

import net.minecraft.server.Packet29DestroyEntity;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HungerGames extends JavaPlugin
{
	public Vector<Player> killedPlayers;
	public Timer mainTimer;
	BlockListener blockListener;
	Logger log = Logger.getLogger("Minecraft");
	EntityListener entityListener;
	
	
	public void onEnable()
	{
		entityListener = new EntityListener(this);
		blockListener = new BlockListener();
		getServer().getPluginManager().registerEvents(blockListener, this);
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		getServer().getPluginManager().registerEvents(entityListener, this);
		killedPlayers = new Vector<Player>();
		mainTimer = new Timer();
		mainTimer.schedule(new HungerTimer(true), 1);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		String command = cmd.getName();
		
		if(!(sender instanceof Player))
		{
			return false;
		}
		
		//command for spectator mode
		if (command.equalsIgnoreCase("s"))
		{
			Player player = (Player)sender;
			
			for(Player tempPlayer : killedPlayers)
			{
				if(tempPlayer == player)
				{
					player.setAllowFlight(true);
					player.setFlying(true);
					player.sendMessage(ChatColor.AQUA + "You may now fly!");
					player.sendMessage(ChatColor.AQUA + "You can still take damage though!");
					return true;
				}
			}
			//if we got here, return so the server doesn't flip
			player.sendMessage(ChatColor.RED + "You aren't allowed in spectator mode!");
			return true;
			
		}
		if(command.equalsIgnoreCase("join"))
		{
			
		}
		if(command.equalsIgnoreCase("savepos"))
		{
			
		}
		
		return true;
	}

	private void handleAllTimerTasks(boolean schedule)
	{
		if (schedule)
		{
			//reschedule every second
			mainTimer.schedule(new HungerTimer(true), 1000);
		}
		this.MakePlayerInvisible();
		this.AccelerateHunger();
	}
	
	private void AccelerateHunger()
	{
		Player[] players = getServer().getOnlinePlayers();
		for(Player player : players)
		{
			if(!IsPlayerDead(player))
			{
				//exhaustion ranges from 0 - 4. when a person moves, he gains exhaustion.
				//when it hits 4, his hunger goes down a point. hunger ranges from 0-20 like health
				if(player.getExhaustion() < 3.0f)
				{
					player.setExhaustion(3.0f);
				}
			}
		}
	}
	
	
	private boolean IsPlayerDead(Player player)
	{
		for(Player temp : killedPlayers)
		{
			if(temp == player)
			{
				return true;
			}
		}
		return false;
	}
	
	private void MakePlayerInvisible()
	{
		Player[] players = getServer().getOnlinePlayers();
		
		for (Player invisiblePlayer : this.killedPlayers)
		{
			for(Player watchingPlayer : players)
			{
				if(watchingPlayer != invisiblePlayer)
				{
					//if they can see them, make them invisible
					if(watchingPlayer.canSee(invisiblePlayer))
					{
						CraftPlayer p1 = (CraftPlayer)invisiblePlayer;
						CraftPlayer p2 = (CraftPlayer)watchingPlayer;
						p2.getHandle().netServerHandler.sendPacket(new Packet29DestroyEntity(p1.getEntityId()));
					}
				}
			}
		}
	}
	
	public class HungerTimer extends TimerTask
	{
		 private boolean startTimer = false;

		  public HungerTimer() { }

		  public HungerTimer(boolean startTimer)
		  {
		    this.startTimer = startTimer;
		  }

		  public void run()
		  {
			  HungerGames.this.handleAllTimerTasks(startTimer);
		  }
	}
	
}
