package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceBlocks implements Listener {
	
	public PlaceBlocks(Xinada xinada) {}
	
	@EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        if (Xinada.inGame() && Xinada.getGame().inRound()) {
        	Xinada.getGame().getRound().getCurrentRole(e.getPlayer()).onPlaceBlock(e);
        }
    }
}
