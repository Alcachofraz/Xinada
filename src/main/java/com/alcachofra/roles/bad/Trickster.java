package com.alcachofra.roles.bad;

import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Trickster extends Role {
    public Trickster(Player player) {
        super(
            player,
            Language.getRolesName("trickster"),
            Language.getRolesDescription("trickster"),
            -1
        );
    }

    @Override
    public void initialise() {
        Utils.addItem(getPlayer(), Material.SKELETON_SKULL, 8, 1);
        super.initialise();
    }

    @Override
    public void reset() {
        Utils.addItem(getPlayer(), Material.SKELETON_SKULL, 8, 1);
        super.reset();
    }

    @Override
    public void onInteract(PlayerInteractEvent e, Action a) {
        if (!isDead()) {
            if (getPlayer().getInventory().getItemInMainHand().getType().equals(Material.SKELETON_SKULL) ||
                    getPlayer().getInventory().getItemInOffHand().getType().equals(Material.SKELETON_SKULL)) {
                if ((a.equals(Action.RIGHT_CLICK_BLOCK) || a.equals(Action.RIGHT_CLICK_AIR)) && !isActivated()) {
                    Utils.removeItem(getPlayer(), Material.SKELETON_SKULL); // Remove item
                    setActivated(true);
                    Utils.messageGlobal(ChatColor.GRAY + " > " + ChatColor.RED + getPlayerName() + " " + Language.getRoleString("16"));
                    Utils.soundGlobal(Sound.ENTITY_LIGHTNING_BOLT_THUNDER);
                }
            }
        }
    }
}
