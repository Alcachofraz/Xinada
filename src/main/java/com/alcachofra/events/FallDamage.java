package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class FallDamage implements Listener {
    public FallDamage() {}

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if ((event.getEntity() instanceof Player) && (event.getCause() == EntityDamageEvent.DamageCause.FALL)) {
            if (Xinada.inGame() && Xinada.getGame().inRound()) { // If a turn is playing...
                Xinada.getGame().getRound().getCurrentRole((Player) event.getEntity()).onFallDamage(event);
            }
            else event.setCancelled(true);
        }
    }
}