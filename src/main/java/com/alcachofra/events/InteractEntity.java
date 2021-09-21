package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class InteractEntity implements Listener {
    public InteractEntity() {}

    @EventHandler
    public void onInteractEntity(PlayerInteractAtEntityEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        if (Xinada.inGame() && Xinada.getGame().inRound()) { // If a turn has already started...
            if (event.getRightClicked() instanceof Player) { // If a player hits another player...
                Player clicked = (Player) event.getRightClicked();
                Player clicker = event.getPlayer();
                Xinada.getGame().getRound().getCurrentRole(clicker).onInteractEntity(event, Xinada.getGame().getRound().getCurrentRole(clicked));
            }
        }
    }
}
