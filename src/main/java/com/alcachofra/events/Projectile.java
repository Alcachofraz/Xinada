package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class Projectile implements Listener {
    public Projectile() {}

    @EventHandler
    public void onProjectile(ProjectileHitEvent event) {
        if (Xinada.inGame() && Xinada.getGame().inRound()) { // If a turn has already started...;
            if (event.getHitEntity() instanceof Player && event.getEntity() instanceof FishHook && event.getEntity().getShooter() instanceof Player) {
                Player fished = (Player) event.getHitEntity();
                Player fisherman = (Player) event.getEntity().getShooter();
                Xinada.getGame().getRound().getCurrentRole(fisherman).onFish(event, Xinada.getGame().getRound().getCurrentRole(fished));
            }
            else if ((event.getEntity() instanceof Snowball)) {
                Snowball ball = (Snowball) event.getEntity();
                Player whoShot = (Player) ball.getShooter();
                Xinada.getGame().getRound().getCurrentRole(whoShot).onSnowball(event);
            }
        }
    }
}
