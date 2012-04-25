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
				BufferedWriter writer = new BufferedWriter(new FileWriter(positions));
				
				writer.write("0:-1559.0,59.0,-629.0");
				writer.newLine();
				writer.write("1:-1565.0,59.0,-633.0");
				writer.newLine();
				writer.write("2:-1571.0,59.0,-635.0");
				writer.newLine();
				writer.write("3:-1577.0,59.0,-636.0");
				writer.newLine();
				writer.write("4:-1583.0,59.0,-635.0");
				writer.newLine();
				writer.write("5:-1589.0,59.0,-633.0");
				writer.newLine();
				writer.write("6:-1595.0,59.0,-629.0");
				writer.newLine();
				writer.write("7:-1599.0,59.0,-623.0");
				writer.newLine();
				writer.write("8:-1601.0,59.0,-617.0");
				writer.newLine();
				writer.write("9:-1602.0,59.0,-611.0");
				writer.newLine();
				writer.write("10:-1601.0,59.0,-605.0");
				writer.newLine();
				writer.write("11:-1599.0,59.0,-599.0");
				writer.newLine();
				writer.write("12:-1595.0,59.0,-593.0");
				writer.newLine();
				writer.write("13:-1589.0,59.0,-589.0");
				writer.newLine();
				writer.write("14:-1583.0,59.0,-587.0");
				writer.newLine();
				writer.write("15:-1577.0,59.0,-586.0");
				writer.newLine();
				writer.write("16:-1571.0,59.0,-587.0");
				writer.newLine();
				writer.write("17:-1565.0,59.0,-589.0");
				writer.newLine();
				writer.write("18:-1559.0,59.0,-593.0");
				writer.newLine();
				writer.write("19:-1555.0,59.0,-599.0");
				writer.newLine();
				writer.write("20:-1553.0,59.0,-605.0");
				writer.newLine();
				writer.write("21:-1552.0,59.0,-611.0");
				writer.newLine();
				writer.write("22:-1553.0,59.0,-617.0");
				writer.newLine();
				writer.write("23:-1555.0,59.0,-623.0");
				
				writer.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		try 
		{
			plugin.log.info("I'm over herE!!!!");
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
					
					plugin.log.info(Integer.valueOf(lastLocationIndex) + ":" + tempLoc.toString());
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void PlayerJoin(Player player)
	{
		if(hungerPlayers.get(player) == null)
		{
			player.sendMessage(ChatColor.RED + "You can't join twice!");
		}
		//add him to the list
		hungerPlayers.put(player, new HungerPlayer(player.getDisplayName(), lastPlayerID++));
		
		plugin.getServer().broadcastMessage(ChatColor.GOLD + player.getDisplayName() + " has just joined the survivial games!");
		//should work... very confusing
		
		HungerPlayer tempPlayer = hungerPlayers.get(player);
		
		player.teleport(startingPositions.get(tempPlayer.getPlayerID()));
	}
}
