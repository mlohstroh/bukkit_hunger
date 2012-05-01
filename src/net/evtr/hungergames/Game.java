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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Game
{
	private HashMap<Player, HungerPlayer> hungerPlayers;
	private HungerGames plugin;
	private HashMap<Integer, Location> startingPositions;
	private int lastPlayerID = 0;
	private Player mGameHost;
	
	public HungerPlayer getPlayer(Player player) {
		return hungerPlayers.get(player);
	}
	
	//horrible name, I know! please feel free to change it
	public Game(HungerGames instance)
	{
		hungerPlayers = new HashMap<Player, HungerPlayer>();
		startingPositions = new HashMap<Integer, Location>();
		plugin = instance;
		LoadPositions();
	}
	
	private void LoadPositions()
	{
		//make the data folder
		File dataFolder = plugin.getDataFolder();
		if(!dataFolder.exists())
		{
			if(dataFolder.mkdir())
			{
				plugin.log.info("[HungerGames] Creating data folder for HungerGames");
			}
			else
			{
				plugin.log.warning("[HungerGames] Could not create folder for HungerGames! Check folder permissions!");
			}
		}
		
		File positions = new File(plugin.getDataFolder() + File.separator + "positions.txt");
		
		if(!positions.exists())
		{
			try 
			{
				//TODO: These should be changable
				BufferedWriter writer = new BufferedWriter(new FileWriter(positions));
				//write default positions
				writer.write("0:-1558.5,59.0,-628.5");
				writer.newLine();
				writer.write("1:-1564.5,59.0,-632.5");
				writer.newLine();
				writer.write("2:-1570.5,59.0,-634.5");
				writer.newLine();
				writer.write("3:-1576.5,59.0,-635.5");
				writer.newLine();
				writer.write("4:-1582.5,59.0,-634.5");
				writer.newLine();
				writer.write("5:-1588.5,59.0,-632.5");
				writer.newLine();
				writer.write("6:-1594.5,59.0,-628.5");
				writer.newLine();
				writer.write("7:-1598.5,59.0,-622.5");
				writer.newLine();
				writer.write("8:-1600.5,59.0,-616.5");
				writer.newLine();
				writer.write("9:-1601.5,59.0,-610.5");
				writer.newLine();
				writer.write("10:-1600.5,59.0,-604.5");
				writer.newLine();
				writer.write("11:-1598.5,59.0,-598.5");
				writer.newLine();
				writer.write("12:-1594.5,59.0,-592.5");
				writer.newLine();
				writer.write("13:-1588.5,59.0,-588.5");
				writer.newLine();
				writer.write("14:-1582.5,59.0,-586.5");
				writer.newLine();
				writer.write("15:-1576.5,59.0,-585.5");
				writer.newLine();
				writer.write("16:-1570.5,59.0,-586.5");
				writer.newLine();
				writer.write("17:-1564.5,59.0,-588.5");
				writer.newLine();
				writer.write("18:-1558.5,59.0,-592.5");
				writer.newLine();
				writer.write("19:-1554.5,59.0,-598.5");
				writer.newLine();
				writer.write("20:-1552.5,59.0,-604.5");
				writer.newLine();
				writer.write("21:-1551.5,59.0,-610.5");
				writer.newLine();
				writer.write("22:-1552.5,59.0,-616.5");
				writer.newLine();
				writer.write("23:-1554.5,59.0,-622.5");
				
				writer.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		try 
		{
			BufferedReader reader = new BufferedReader(new FileReader(positions));
			
			String line = null;
			
			int lastLocationIndex = 0;
			
			while((line = reader.readLine()) != null)
			{
				plugin.log.info(line);
				if (!line.equalsIgnoreCase(""))
				{
					String[] lines = line.split(":");
					
					String[] vectors = lines[1].split(",");
					
					Location tempLoc = new Location(plugin.getDefaultWorld(), Double.valueOf(vectors[0]), Double.valueOf(vectors[1]), Double.valueOf(vectors[2]));
					
					startingPositions.put(lastLocationIndex, tempLoc);
					lastLocationIndex++;
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void HostGame(Player player)
	{
		if(mGameHost != null)
		{
			player.sendMessage(ChatColor.RED + "You cannot be host this game!");
			player.sendMessage(ChatColor.RED + "Someone else is host!");
		}
		else
		{
			mGameHost = player;
			
			//TODO: Setup permissions so not everyone can be a host
			player.teleport(new Location(plugin.getDefaultWorld(), -1573.5, 67.0, -642.5));	//TODO: Find a way to somehow not hard code that vector
			player.setAllowFlight(true);
			player.setFlying(true);
			player.sendMessage(ChatColor.GOLD + "You may now fly!");
			player.sendMessage(ChatColor.GOLD + "You can still take damage though!");
		}
	}
	
	public void PlayerJoin(Player player)
	{
		if(player == mGameHost)
		{
			player.sendMessage(ChatColor.RED + "You can't join if you're the host!");
		}
		if(hungerPlayers.get(player) != null)
		{
			player.sendMessage(ChatColor.RED + "You can't join twice!");
		}
		//add him to the list
		if((lastPlayerID + 1) >= 24)
		{
			player.sendMessage(ChatColor.RED + "There are already 24 players in this match!");
			player.sendMessage(ChatColor.RED + "Please wait until match is over!");
			return;
		}
		hungerPlayers.put(player, new HungerPlayer(player.getDisplayName(), lastPlayerID++));
		
		plugin.getServer().broadcastMessage(ChatColor.GOLD + player.getDisplayName() + " has just joined the survivial games!");
		//should work... very confusing
		
		HungerPlayer tempPlayer = hungerPlayers.get(player);
		
		player.teleport(startingPositions.get(tempPlayer.getPlayerID()));
	}
}
