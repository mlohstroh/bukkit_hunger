package net.evtr.hungergames;

import java.util.Vector;

import org.bukkit.entity.Player;

public class HungerPlayer
{
	private int mKills;
	private String mPlayerName;
	private Vector<HungerPlayer> mKilledPlayers;
	private int mGameID;
	
	public HungerPlayer(String name, int id)
	{
		mKills = 0;
		mPlayerName = name;
		mKilledPlayers = new Vector<HungerPlayer>();
		mGameID = id;
	}
	
	public int getPlayerID()
	{
		return mGameID;
	}
}
