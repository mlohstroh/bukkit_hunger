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
import org.bukkit.World;
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
	Game currentGame = null;
	
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
		
		//essentially only allowing players to send messages
		if(!(sender instanceof Player))
		{
			return false;
		}
		
		Player player = (Player)sender;
		
		if(command.equalsIgnoreCase("hg"))
		{	
			if(args[0].equalsIgnoreCase("join"))
			{
				if(currentGame != null)
				{
					currentGame.PlayerJoin(player);
					return true;
				}
				else
				{
					player.sendMessage(ChatColor.RED + "A game is not going on! Why don't you start one?");
				}
			}
			if(args[0].equalsIgnoreCase("create"))
			{
				if(currentGame == null)
				{
					//make a game
					currentGame = new Game(this);
					player.sendMessage(ChatColor.GOLD + "You have created a game! You may either join or host!");
					player.sendMessage(ChatColor.GOLD + "/hg join OR /hg host");
					return true;
				}
				else
				{
					player.sendMessage(ChatColor.RED + "A game is already in progress!");
				}
			}
			if(args[0].equalsIgnoreCase("host"))
			{
				if(currentGame != null)
				{
					currentGame.HostGame(player);
				}
				else
				{
					player.sendMessage(ChatColor.RED + "A game is not going on! Why don't you start one?");
				}
			}
			//command for spectator mode
			if (args[0].equalsIgnoreCase("s"))
			{
				if(currentGame != null)
				{
					//TODO: We will need to revamp this once Game.java gets working correctly 
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
				}
				else
				{
					player.sendMessage(ChatColor.RED + "You can't spectate a game that is not going on!");
				}
			}
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
	
	
	private void ForcePlayersTogether()
	{
		
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
				if(player.getFoodLevel() == 0)
				{
					//killing the player quite quickly :) this might need to change...
					player.setHealth(player.getHealth() - 1);
				}
				if(player.getExhaustion() < 3.0f)
				{
					player.setExhaustion(3.0f);
				}
			}
		}
	}
	
	public World getDefaultWorld()
	{
		return getServer().getWorlds().get(0);
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
