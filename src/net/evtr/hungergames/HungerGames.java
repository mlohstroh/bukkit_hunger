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
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class HungerGames extends JavaPlugin
{
	public Vector<Player> killedPlayers;
	public Timer mainTimer;
	BlockListener blockListener;
	Logger log = Logger.getLogger("Minecraft");
	EntityListener entityListener;
	public Game currentGame = null;
	
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
			if(args[0] != null)
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
				if (args[0].equalsIgnoreCase("s")) //|| args[0].equalsIgnoreCase("sp"))
				{
					if(currentGame != null)
					{
						if(args.length == 2)
						{							
							if(currentGame.getPlayer(player).mIsDied)
							{
								HungerPlayer hPlayer = currentGame.getHungerPlayerByName(args[1]);
								if(hPlayer != null)
								{
									if(hPlayer.mIsDied)
									{
										player.sendMessage(ChatColor.RED + "You can't sponser a dead person!");
										return true;
									}
									else
									{
										//give them a list of potions they can give
										player.sendMessage(ChatColor.GOLD + "You are now sponsering " + hPlayer.getPlayerName());
										Potion potion = new Potion(PotionType.STRENGTH);
										//instantly apply the potion
										potion.apply(hPlayer.getPlayer());
										hPlayer.getPlayer().sendMessage(ChatColor.GOLD + "You have been sponsered and have been gifted " + potion.toString());
									}
								}
								else
								{
									player.sendMessage(ChatColor.RED + args[1] + " doesn't exist in this hunger games!");
									player.sendMessage(ChatColor.RED + "Please try again! Use /hg s to view the people left.");
									return true;
								}
								
								player.setAllowFlight(true);
								player.setFlying(true);
								player.sendMessage(ChatColor.GOLD + "You may now fly!");
								player.sendMessage(ChatColor.GOLD + "You can still take damage though!");
								return true;
							}						
							player.sendMessage(ChatColor.RED + "You aren't allowed in spectator mode!");
						}
						else
						{
							//grab the alive players and display them
							for(HungerPlayer hPlayer : currentGame.getAlivePlayers())
							{
								player.sendMessage(ChatColor.GOLD + hPlayer.getPlayerName());
							}
						}
					}
					else
					{
						player.sendMessage(ChatColor.RED + "You can't spectate a game that is not going on!");
					}
				}
				if(args[0].equalsIgnoreCase("p"))
				{
					Potion potion = new Potion(PotionType.STRENGTH);
					potion.apply(player);
					player.sendMessage(ChatColor.GOLD + "You have been sponsered and have been gifted " + potion.toString());
				}
				
//				if ( args[0].equalsIgnoreCase("sp") ) 
//				{
//					if ( currentGame != null ) 
//					{
//						HungerPlayer hPlayer = currentGame.getPlayer(player);
//						if ( hPlayer.mIsDied ) 
//						{
//							hPlayer.mIsSponsor = true;
//							player.sendMessage(ChatColor.GOLD + "You can now give people some MONIES.");
//							//TODO: Make them a proper sponsor.
//						}
//						else 
//						{
//							player.sendMessage(ChatColor.RED + "You're not dead yet!");
//						}
//					}
//				}
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
		
		//TODO: The timer was throwing exceptions
//		if(currentGame != null)
//		{
//			currentGame.ForcePlayersTogether();
//		}
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
