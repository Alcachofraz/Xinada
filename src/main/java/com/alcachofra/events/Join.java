package com.alcachofra.events;

import com.alcachofra.utils.WorldManager;
import com.alcachofra.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {
	
	public Join() {}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.getPlayer().setGameMode(GameMode.SURVIVAL);
		WorldManager.lobby(event.getPlayer());
		Utils.cleanPlayer(event.getPlayer());
	}
}
