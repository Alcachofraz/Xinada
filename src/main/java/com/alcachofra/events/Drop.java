package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class Drop implements Listener {
	
	public Drop(Xinada xinada) {}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event){
		event.setCancelled(true);
	}
}
