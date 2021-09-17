package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class InteractEntity implements Listener {
    public InteractEntity(Xinada xinada) {}

    @EventHandler
    public void onInteractEntity(PlayerInteractAtEntityEvent e) {
        if (Xinada.inGame() && Xinada.getGame().inRound()) { // If a turn has already started...
            if (e.getRightClicked() instanceof Player) { // If a player hits another player...
                Player clicked = (Player) e.getRightClicked();
                Player clicker = e.getPlayer();
                Xinada.getGame().getRound().getCurrentRole(clicker).onInteractEntity(e, Xinada.getGame().getRound().getCurrentRole(clicked));
            }
        }
    }
}
