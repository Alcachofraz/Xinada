package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Interact implements Listener {
    public Interact(Xinada xinada) {}

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (Xinada.inGame() && Xinada.getGame().inRound()) { // If a turn has already started...
            Action action = e.getAction();
            Player player = e.getPlayer();
            Xinada.getGame().getRound().getCurrentRole(player).onInteract(e, action);
        }
    }
}
