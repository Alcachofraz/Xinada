package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class Interact implements Listener {
    public Interact() {}

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        if (Xinada.inGame() && Xinada.getGame().inRound()) { // If a turn has already started...
            Xinada.getGame().getRound().getCurrentRole(event.getPlayer()).onInteract(event, event.getAction());
        }
    }
}
