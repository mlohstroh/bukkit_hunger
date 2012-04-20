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

import java.util.Vector;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HungerGames extends JavaPlugin
{
	public Vector<Player> killedPlayers;
	
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(new BlockListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		killedPlayers = new Vector<Player>();
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
		
		return true;
	}

}
