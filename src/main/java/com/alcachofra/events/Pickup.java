package com.alcachofra.events;

import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import com.alcachofra.roles.bad.Accomplice;
import com.alcachofra.roles.bad.Murderer;
import com.alcachofra.roles.bad.Traitor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class Pickup implements Listener {

	public Pickup(Xinada xinada) {}
	
	@EventHandler
	public void onPickup(EntityPickupItemEvent e) {
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			if (player.getGameMode() == GameMode.CREATIVE) return;
			if (Xinada.inGame() && Xinada.getGame().inRound()) {
				Role role = Xinada.getGame().getRound().getCurrentRole(player);
				if (e.getItem().getItemStack().getType() == Material.IRON_SWORD) {
					if ((role instanceof Murderer) && !((Murderer) role).isLaughing()) {
						role.setActivated(true); // Murderer is activated
						Xinada.getGame().getRound().subtractSwordsDropped();
						// All good, can pick up
					} else if ((role instanceof Accomplice) && !((Accomplice) role).isLaughing()) {
						role.setActivated(true); // Accomplice is activated
						Xinada.getGame().getRound().subtractSwordsDropped();
						// All good, can pick up
					} else if ((role instanceof Traitor) && !((Traitor) role).isLaughing()) {
						role.setActivated(true); // Accomplice is activated
						Xinada.getGame().getRound().subtractSwordsDropped();
						// All good, can pick up
					} else e.setCancelled(true); // Can't pick up
				} else if (e.getItem().getItemStack().getType() == Material.BOW || e.getItem().getItemStack().getType() == Material.ARROW) {
					if (!role.canPickUp()) {
						e.setCancelled(true); // Can't pick up
					} else role.setHasBow(true); // Can pick up
				} else e.setCancelled(true);
			}
		}
	}
}
