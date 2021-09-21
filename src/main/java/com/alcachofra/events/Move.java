package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Move implements Listener {
    public Move() {}

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        if (Xinada.inGame() && Xinada.getGame().inRound()) { // If a turn has already started...
            Xinada.getGame().getRound().getCurrentRole(event.getPlayer()).onMove(event);
        }
    }
}