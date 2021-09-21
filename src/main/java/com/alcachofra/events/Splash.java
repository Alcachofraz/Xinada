package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;

public class Splash implements Listener {
    public Splash() {}

    @EventHandler
    public void onSplash(PotionSplashEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Player player = (Player) e.getEntity().getShooter();
            if (player.getGameMode() == GameMode.CREATIVE) return;
            if (Xinada.inGame() && Xinada.getGame().inRound()) { // If a turn has already started...
                Xinada.getGame().getRound().getCurrentRole(player).onSplash(e);
            }
        }
    }
}
