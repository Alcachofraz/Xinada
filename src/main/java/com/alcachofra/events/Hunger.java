package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class Hunger implements Listener {
	
	public Hunger(Xinada xinada) {}
	
	@EventHandler
	public void onHungerDeplete(FoodLevelChangeEvent e) {
			e.setCancelled(true);
			Player player = (Player) e.getEntity();
			player.setFoodLevel(20);
	}
}
