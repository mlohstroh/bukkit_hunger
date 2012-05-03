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

import java.util.HashMap;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.potion.PotionType;

public class HungerSponser extends HungerPlayer
{		
	public HashMap<Integer, PotionType> sponserGifts;
	private HungerPlayer sponseredPlayer;
	private int sponserTime;
	private Random sponsorRandom;
	
	public HungerSponser(HungerPlayer playerWhoDied) 
	{
		this.mGameID = playerWhoDied.mGameID;
		this.mKilledPlayers = playerWhoDied.mKilledPlayers;
		this.mKills = playerWhoDied.mKills;
		this.mPlayer = playerWhoDied.mPlayer;
		this.mPlayerName = playerWhoDied.mPlayerName;
		sponserGifts = new HashMap<Integer, PotionType>();
		sponsorRandom = new Random(System.currentTimeMillis());
	}
	
	public void SponserPlayer(HungerPlayer hPlayer)
	{
		//set the sponsered player
		sponseredPlayer = hPlayer;
		//load some random gifts
		this.LoadGifts();		
		sponserGifts = new HashMap<Integer, PotionType>();
	}
	
	public void LoadGifts()
	{
		
		//always clear the hashmap
		sponserGifts.clear();
		
		PotionType type1 = PotionType.values()[sponsorRandom.nextInt(PotionType.values().length)];
		sponserGifts.put(1, type1);
		
		PotionType type2 = PotionType.values()[sponsorRandom.nextInt(PotionType.values().length)];
		while(type1 == type2)
		{
			type2 = PotionType.values()[sponsorRandom.nextInt(PotionType.values().length)];
		}
		sponserGifts.put(2, type2);
		PotionType type3 = PotionType.values()[sponsorRandom.nextInt(PotionType.values().length)];
		while(type1 == type3 || type2 == type3)
		{
			type3 = PotionType.values()[sponsorRandom.nextInt(PotionType.values().length)];
		}
		sponserGifts.put(3, type3);
		
		//give them a list of potions they can give
		this.getPlayer().sendMessage(ChatColor.GOLD + "Choose one gift to give your sponser:");
		//TODO: Find a way to randomize this
		
		this.getPlayer().sendMessage(ChatColor.GOLD + "#1 : Potion of " + type1.toString());
		this.getPlayer().sendMessage(ChatColor.GOLD + "#2 : Potion of " + type2.toString());
		this.getPlayer().sendMessage(ChatColor.GOLD + "#3 : Potion of " + type3.toString());
		this.getPlayer().sendMessage(ChatColor.GOLD + "Choose which gift to send by doing /hg give <#>");		
	}
	
	public PotionType getGiftType(Integer index)
	{
		return sponserGifts.get(index);
	}
	public HungerPlayer getSponseredPlayer()
	{
		return sponseredPlayer;
	}
	
	public void UpdateSponserTime()
	{
		sponserTime++;
		if(sponserTime >= 60)
		{
			sponserTime = 0;
			LoadGifts();
		}
	}
}
