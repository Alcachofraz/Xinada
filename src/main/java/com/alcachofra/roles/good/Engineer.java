package com.alcachofra.roles.good;

import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class Engineer extends Role {
    public Engineer(Player player) {
        super(
            player,
            Language.getRolesName("engineer"),
            Language.getRolesDescription("engineer"),
            1
        );
    }

    @Override
    public void initialise() {
        Utils.addItem(getPlayer(), Material.COMPASS, 8, 1);
        super.initialise();
    }

    @Override
    public void reset() {
        Utils.addItem(getPlayer(), Material.COMPASS, 8, 1);
        super.reset();
    }

    @Override
    public void onInteract(PlayerInteractEvent e, Action a) {
        if (!isDead()) {
            if (getPlayer().getInventory().getItemInMainHand().getType().equals(Material.COMPASS) ||
                    getPlayer().getInventory().getItemInOffHand().getType().equals(Material.COMPASS)) {
                if ((a.equals(Action.RIGHT_CLICK_BLOCK) || a.equals(Action.RIGHT_CLICK_AIR)) && e.getHand() == EquipmentSlot.HAND) {
                    getPlayer().sendMessage(
                        ChatColor.GRAY + Language.getRoleString("82") + " " +
                        ChatColor.GOLD + (int) Xinada.getGame().getRound().getNearestAssassin(this).getPlayer().getLocation().distance(getPlayer().getLocation()) +
                        ChatColor.GRAY + " " + Language.getRoleString("83")
                    );
                    Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG);
                }
            }
        }
    }
}
