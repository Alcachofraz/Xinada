package com.alcachofra.events;

import com.alcachofra.utils.Utils;
import com.alcachofra.main.Xinada;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {
	
	public Join(Xinada xinada) {}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.setGameMode(GameMode.SURVIVAL);
		Xinada.teleportLobby(player);
		Utils.cleanPlayer(player);
	}
}
