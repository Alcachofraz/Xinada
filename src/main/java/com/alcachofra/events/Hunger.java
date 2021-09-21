package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class Hunger implements Listener {
	
	public Hunger() {}
	
	@EventHandler
	public void onHungerDeplete(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (player.getGameMode() == GameMode.CREATIVE) return;
			Xinada.getGame().getRound().getCurrentRole(player).onHungerDeplete(event);
		}
	}
}
