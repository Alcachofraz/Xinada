package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Damage implements Listener {
    public Damage(Xinada xinada) {}

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (Xinada.inGame() && Xinada.getGame().inRound()) { // If a turn is playing...
            e.setCancelled(true); // Player doesn't get hit
            if ((e.getEntity() instanceof Player) && (e.getDamager() instanceof Player)) { // If a player hits another player...
                Player whoWasHit = (Player) e.getEntity(); // Gets the player who was hit
                Player whoHit = (Player) e.getDamager(); // Gets the player who hit
                Xinada.getGame().getRound().getCurrentRole(whoHit).onHit(e, Xinada.getGame().getRound().getCurrentRole(whoWasHit));
            }
            else if ((e.getEntity() instanceof Player) && (e.getDamager() instanceof Arrow)) { // If an arrow hits a player...
                Player whoWasShot = (Player) e.getEntity(); // Gets the player who was shot
                Arrow arrow = (Arrow) e.getDamager(); // Gets the arrow
                if (arrow.getShooter() instanceof Player) {
                    Player whoShot = (Player) arrow.getShooter();
                    Xinada.getGame().getRound().getCurrentRole(whoWasShot).onShot(e, Xinada.getGame().getRound().getCurrentRole(whoShot));
                }
            }
            /*else if ((e.getEntity() instanceof Player) && (e.getDamager() instanceof Snowball)) { // If a fish hook hits a player...
                Player whoWasShot = (Player) e.getEntity(); // Gets the player who was shot
                Snowball ball = (Snowball) e.getDamager();
                if (ball.getShooter() instanceof Player) {
                    Player whoShot = (Player) ball.getShooter();
                    Xinada.getGame().getRound().getCurrentRole(whoWasShot).onSnowballed(e, Xinada.getGame().getRound().getCurrentRole(whoShot));
                }
            }*/
        }
        else if (!Xinada.inGame()) e.setDamage(0);
        else e.setCancelled(true); // Entity doesn't get hit
    }
}
