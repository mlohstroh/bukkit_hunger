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

import org.bukkit.entity.Player;

public class HungerPlayer
{
	protected Player mPlayer;
	protected int mKills;
	protected String mPlayerName;
	protected Vector<HungerPlayer> mKilledPlayers;
	protected int mGameID;
	
	public boolean mIsDied, mIsSponsor;
	
	public int mLastTemplateID;
	
	public HungerPlayer() { }
	
	public HungerPlayer(String name, int id, Player player)
	{
		mKills = 0;
		mPlayerName = name;
		mKilledPlayers = new Vector<HungerPlayer>();
		mGameID = id;
		mIsDied = false;
		mIsSponsor = false;
		mPlayer = player;
		mLastTemplateID = 0;
	}
	
	public int getPlayerID()
	{
		return mGameID;
	}
	
	public void KilledPlayer(HungerPlayer player)
	{
		//ya. they killed someone! MAY THE ODDS BE EVER IN YOUR FAVOR!!
		mKilledPlayers.add(player);
		mKills++;
	}

	public String getPlayerName() 
	{
		return mPlayerName;
	}
	public Player getPlayer()
	{
		return mPlayer;
	}
	
	public int getKills()
	{
		return mKills;
	}
}
