package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class Damage implements Listener {
    public Damage() {}

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if ((event.getEntity() instanceof Player) && (event.getCause() == EntityDamageEvent.DamageCause.FALL)) event.setCancelled(true);
        else if (Xinada.inGame() && Xinada.getGame().inRound()) { // If a turn is playing...
            event.setCancelled(true); // Player doesn't get hit
            if ((event.getEntity() instanceof Player) && (event.getDamager() instanceof Player)) { // If a player hits another player...
                Player whoWasHit = (Player) event.getEntity(); // Gets the player who was hit
                Player whoHit = (Player) event.getDamager(); // Gets the player who hit
                Xinada.getGame().getRound().getCurrentRole(whoHit).onHit(event, Xinada.getGame().getRound().getCurrentRole(whoWasHit));
            }
            else if ((event.getEntity() instanceof Player) && (event.getDamager() instanceof Arrow)) { // If an arrow hits a player...
                Player whoWasShot = (Player) event.getEntity(); // Gets the player who was shot
                Arrow arrow = (Arrow) event.getDamager(); // Gets the arrow
                if (arrow.getShooter() instanceof Player) {
                    Player whoShot = (Player) arrow.getShooter();
                    Xinada.getGame().getRound().getCurrentRole(whoWasShot).onShot(event, Xinada.getGame().getRound().getCurrentRole(whoShot));
                }
            }
        }
        else if (!Xinada.inGame()) event.setDamage(0); // Game intervals
        else event.setCancelled(true); // Round intervals
    }
}
