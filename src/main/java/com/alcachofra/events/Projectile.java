package com.alcachofra.events;

import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import org.bukkit.Location;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class Projectile implements Listener {
    public Projectile(Xinada xinada) {}

    @EventHandler
    public void onProjectile(ProjectileHitEvent e) {
        if (Xinada.inGame() && Xinada.getGame().inRound()) { // If a turn has already started...;
            if (e.getHitEntity() instanceof Player && e.getEntity() instanceof FishHook && e.getEntity().getShooter() instanceof Player) {
                Player fished = (Player) e.getHitEntity();
                Player fisherman = (Player) e.getEntity().getShooter();
                Xinada.getGame().getRound().getCurrentRole(fisherman).onFish(e, Xinada.getGame().getRound().getCurrentRole(fished));
            }
            else if ((e.getEntity() instanceof Snowball) && e.getHitBlock() != null) {
                System.out.println("Snowball");
                Snowball ball = (Snowball) e.getEntity();
                Location hitBlock = e.getHitBlock().getLocation();
                Player whoShot = (Player) ball.getShooter();
                for (Role role : Xinada.getGame().getRound().getCurrentRoles().values()) {
                    if (role.isDead() && role.getPotLocation() != null &&
                            role.getPotLocation().getX() == hitBlock.getX() &&
                            role.getPotLocation().getY() == hitBlock.getY() &&
                            role.getPotLocation().getZ() == hitBlock.getZ()) {
                        System.out.println("POT");
                        Xinada.getGame().getRound().getCurrentRole(whoShot).onSnowball(e, Xinada.getGame().getRound().getCurrentRole(whoShot));
                    }
                }
            }
        }
    }
}
