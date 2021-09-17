package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Leave implements Listener{
	
	public Leave(Xinada xinada) {}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (Xinada.inGame()) {
			Xinada.getGame().removePlayerFromGame(player);
			if (Xinada.getGame().inRound()) {
				Xinada.getGame().getRound().end(0);
			}
		}
	}
}