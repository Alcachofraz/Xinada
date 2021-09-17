package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Chest implements Listener {
	
	public Chest(Xinada xinada) {}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		if (e.getClickedBlock() == null) return;
		if (
			e.getAction() == Action.RIGHT_CLICK_BLOCK && (
				(e.getClickedBlock().getType() == Material.CHEST) ||
				(e.getClickedBlock().getType() == Material.TRAPPED_CHEST) ||
				(e.getClickedBlock().getType() == Material.DISPENSER) ||
				(e.getClickedBlock().getType() == Material.DROPPER) ||
				(e.getClickedBlock().getType() == Material.CRAFTING_TABLE) ||
				(e.getClickedBlock().getType() == Material.FURNACE) ||
				(e.getClickedBlock().getType() == Material.FURNACE) ||
				(e.getClickedBlock().getType() == Material.ENDER_CHEST) ||
				(e.getClickedBlock().getType() == Material.ANVIL) ||
				(e.getClickedBlock().getType() == Material.BREWING_STAND) ||
				(e.getClickedBlock().getType() == Material.ENCHANTING_TABLE) ||
				(e.getClickedBlock().getType() == Material.TNT) ||
				(e.getClickedBlock().getType() == Material.JUKEBOX)
			)	
		) {
			e.setCancelled(true);
		}
	}
}
