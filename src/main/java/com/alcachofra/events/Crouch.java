package com.alcachofra.events;

import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import com.alcachofra.roles.good.Survivor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffectType;

public class Crouch implements Listener {
	
	public Crouch(Xinada xinada) {}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
		if (Xinada.inGame() && Xinada.getGame().inRound()) {
			Role r = Xinada.getGame().getRound().getCurrentRole(event.getPlayer());
			if (r.getPlayer().isSneaking()) {
				if (r.isExposed()) {
					r.setExposed(false);
					r.getPlayer().removePotionEffect(PotionEffectType.JUMP); // Player gains the ability to jump
					r.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS); // Player gains the ability to see
					r.getPlayer().setWalkSpeed(0.2f); // Player gains the ability to walk
				}
				else if ((r instanceof Survivor) && !r.isActivated()) {
					if (r.isDead()) {
						r.revive();
						r.setActivated(true);
					}
				}
			}
		}
	}
}
