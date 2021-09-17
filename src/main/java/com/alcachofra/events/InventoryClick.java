package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClick implements Listener {

    public InventoryClick(Xinada xinada) {}

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (Xinada.inGame() && Xinada.getGame().inRound()) { // If a turn has already started...
            if (e.getWhoClicked() instanceof Player) {
                Player player = (Player) e.getWhoClicked();
                Xinada.getGame().getRound().getCurrentRole(player).onInventoryClick(e);
            }
        }
    }
}
