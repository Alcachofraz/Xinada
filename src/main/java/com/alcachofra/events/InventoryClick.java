package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClick implements Listener {

    public InventoryClick() {}

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (Xinada.inGame() && Xinada.getGame().inRound()) { // If a turn has already started...
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                Xinada.getGame().getRound().getCurrentRole(player).onInventoryClick(event);
            }
        }
    }
}
