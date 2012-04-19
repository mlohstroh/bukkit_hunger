package net.evtr.hungergames;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockListener implements Listener
{
	@EventHandler
	public void StopBlockBreaks(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		//all we care about is his OP status
		if (!player.isOp())
		{
			event.setCancelled(true);
		}
		//if he is op, then he can break blocks
	}
}
