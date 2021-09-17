package com.alcachofra.roles.neutral;

import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Shepard extends Role {
    public Shepard(Player player) {
        super(
            player,
            Language.getRolesName("shepard"),
            Language.getRolesDescription("shepard"),
            0
        );
    }

    @Override
    public void initialise() {
        Utils.fillInventory(getPlayer(), Material.SHEARS, 1);
        super.initialise();
    }

    @Override
    public void reset() {
        Utils.fillInventory(getPlayer(), Material.SHEARS, 1);
        super.reset();
    }

    @Override
    public void award() {
        // No points (neutral)
    }

    @Override
    public void onInteractEntity(PlayerInteractAtEntityEvent e, Role clicked) {
        if (!isDead()) { // If not dead...
            if (clicked instanceof Sheep) {
                if (getPlayer().getInventory().getItemInMainHand().getType().equals(Material.SHEARS) ||
                        getPlayer().getInventory().getItemInOffHand().getType().equals(Material.SHEARS)) {
                    setActivated(true);
                    clicked.setActivated(true);

                    Utils.removeItem(getPlayer(), Material.SHEARS);
                    Utils.removeItem(clicked.getPlayer(), Material.WHITE_WOOL);

                    Utils.soundLocation(clicked.getPlayer().getLocation(), Sound.ENTITY_SHEEP_SHEAR);
                    Utils.dropItem(new ItemStack(Material.WHITE_WOOL, 1), clicked.getPlayer().getLocation());

                    setPoints(2);

                    getPlayer().sendMessage(ChatColor.GREEN + clicked.getPlayerName() + " " + Language.getRoleString("127"));
                    clicked.getPlayer().sendMessage(ChatColor.RED + getPlayerName() + " " + Language.getRoleString("128"));
                }
            }
        }
    }
}
