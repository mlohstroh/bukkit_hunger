package net.evtr.hungergames;

import java.util.Vector;

import org.bukkit.entity.Player;

public class HungerPlayer
{
	private int mKills;
	private String mPlayerName;
	private Vector<HungerPlayer> mKilledPlayers;
	
	public HungerPlayer(String name)
	{
		mKills = 0;
		mPlayerName = name;
		mKilledPlayers = new Vector<HungerPlayer>();
	}
}
