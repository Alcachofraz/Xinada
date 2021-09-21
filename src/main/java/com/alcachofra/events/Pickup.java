package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class Pickup implements Listener {

	public Pickup() {}
	
	@EventHandler
	public void onPickup(EntityPickupItemEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (player.getGameMode() == GameMode.CREATIVE) return;
			if (Xinada.inGame() && Xinada.getGame().inRound()) {
				Xinada.getGame().getRound().getCurrentRole(player).onPickup(event);
			}
		}
	}
}
