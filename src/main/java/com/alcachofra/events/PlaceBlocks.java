package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceBlocks implements Listener {
	
	public PlaceBlocks() {}
	
	@EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        if (Xinada.inGame() && Xinada.getGame().inRound()) {
        	Xinada.getGame().getRound().getCurrentRole(event.getPlayer()).onPlaceBlock(event);
        }
    }
}
