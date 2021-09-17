package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Move implements Listener {
    public Move(Xinada xinada) {}

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (Xinada.inGame() && Xinada.getGame().inRound()) { // If a turn has already started...
            Xinada.getGame().getRound().getCurrentRole(e.getPlayer()).onMove(e);
        }
    }
}