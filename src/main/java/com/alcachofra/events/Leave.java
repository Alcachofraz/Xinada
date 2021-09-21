package com.alcachofra.events;

import com.alcachofra.main.Round;
import com.alcachofra.main.Xinada;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Leave implements Listener{
	
	public Leave() {}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (Xinada.inGame()) {
			Xinada.getGame().removePlayer(player);
			if (Xinada.getGame().inRound()) {
				Xinada.getGame().endRound(Round.EndCause.FORCED_ROUND_END);
			}
		}
	}
}