package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlocks implements Listener {
	
	public BreakBlocks(Xinada xinada) {}
	
	@EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		if (Xinada.inGame() && Xinada.getGame().inRound()) {
			Xinada.getGame().getRound().getCurrentRole(event.getPlayer()).onBreakBlock(event);
		}
		event.setCancelled(true);
    }
}
