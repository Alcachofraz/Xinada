package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class Crouch implements Listener {
	
	public Crouch() {}

	@EventHandler
	public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		if (Xinada.inGame() && Xinada.getGame().inRound()) {
			Xinada.getGame().getRound().getCurrentRole(event.getPlayer()).onCrouch(event);
		}
	}
}
