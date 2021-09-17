package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import com.alcachofra.utils.Utils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class ShootBow implements Listener {

    public ShootBow(Xinada xinada) {}

    @EventHandler
    public void onShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Utils.soundLocation(player.getLocation(), Sound.ENTITY_DOLPHIN_DEATH);
        }
    }
}
