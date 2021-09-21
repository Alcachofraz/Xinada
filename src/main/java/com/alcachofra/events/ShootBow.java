package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class ShootBow implements Listener {

    public ShootBow() {}

    @EventHandler
    public void onShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.getGameMode() == GameMode.CREATIVE) return;
            if (Xinada.inGame() && Xinada.getGame().inRound()) {
                Xinada.getGame().getRound().getCurrentRole(player).onShootBow(event);
            }
        }
    }
}
