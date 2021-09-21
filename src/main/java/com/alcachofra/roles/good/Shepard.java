package com.alcachofra.roles.good;

import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Shepard extends Role {
    public Shepard(Player player) {
        super(
            player,
            Language.getRoleName("shepard"),
            Language.getRoleDescription("shepard"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        addPoint();
        if (isActivated()) addPoint();
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
    public void onInteractEntity(PlayerInteractAtEntityEvent event, Role clicked) {
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

                    setActivated(true);
                    clicked.setActivated(false);

                    getPlayer().sendMessage(String.format(Language.getString("foundSheep"), clicked.getPlayer().getName()));
                    clicked.getPlayer().sendMessage(String.format(Language.getString("cutWool"), getPlayer().getName()));
                }
            }
        }
    }
}
