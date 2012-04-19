package net.evtr.hungergames;

import org.bukkit.plugin.java.JavaPlugin;

public class HungerGames extends JavaPlugin
{
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(new BlockListener(), this);
	}
}
