package com.alcachofra.roles.bad;

import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import com.alcachofra.roles.good.Cop;
import com.alcachofra.utils.Utils;
import com.alcachofra.main.WorldManager;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.FireworkMeta;

public class Pyrotechnic extends Role {
    public Pyrotechnic(Player player) {
        super(
                player,
                Language.getRolesName("pyrotechnic"),
                Language.getRolesDescription("pyrotechnic"),
                -1
        );
    }

    @Override
    public void initialise() {
        Utils.addItem(getPlayer(), Material.FIREWORK_ROCKET, 8, 1);
        super.initialise();
    }
    @Override
    public void reset() {
        Utils.addItem(getPlayer(), Material.FIREWORK_ROCKET, 8, 1);
        super.reset();
    }

    public void onInteract(PlayerInteractEvent e, Action a) {
        if (!isDead()) {
            if (getPlayer().getInventory().getItemInMainHand().getType().equals(Material.FIREWORK_ROCKET) ||
                    getPlayer().getInventory().getItemInOffHand().getType().equals(Material.FIREWORK_ROCKET)) {
                if ((a.equals(Action.RIGHT_CLICK_BLOCK) || a.equals(Action.RIGHT_CLICK_AIR)) && !isActivated()) {
                    Utils.removeItem(getPlayer(), Material.FIREWORK_ROCKET);
                    e.setCancelled(true);
                    Role cop = Xinada.getGame().getRound().getCurrentRole(Cop.class);
                    if (!cop.hasBow()) {
                        cop = null;
                        for (Role role : Xinada.getGame().getRound().getCurrentRoles().values()) {
                            if (role.hasBow()) {
                                cop = role;
                            }
                        }
                    }
                    if (cop != null) {
                        Firework firework = (Firework) WorldManager.getWorld()
                                .spawnEntity(cop.getPlayer().getLocation(), EntityType.FIREWORK);
                        FireworkMeta fireworkMeta = firework.getFireworkMeta();

                        fireworkMeta.setPower(1);
                        fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.WHITE).trail(true).build());
                        firework.setFireworkMeta(fireworkMeta);
                    }
                }
            }
        }
    }
}
