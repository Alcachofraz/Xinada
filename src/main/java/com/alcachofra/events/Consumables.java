package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class Consumables implements Listener {
	public Consumables(Xinada xinada) {}
	
	@EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
		event.setCancelled(true);
    }
}
