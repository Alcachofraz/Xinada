package com.alcachofra.roles.good;

import com.alcachofra.main.Xinada;
import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.util.Vector;

public class Fisherman extends Role {

    public Fisherman(Player player) {
        super(
            player,
            Language.getRolesName("fisherman"),
            Language.getRolesDescription("fisherman"),
            1
        );
    }

    @Override
    public void initialise() {
        Utils.addItem(getPlayer(), Material.FISHING_ROD, 8, 1);
        super.initialise();
    }

    @Override
    public void reset() {
        Utils.addItem(getPlayer(), Material.FISHING_ROD, 8, 1);
        super.reset();
    }

    @Override
    public void onFish(ProjectileHitEvent e, Role fished) {
        if (!isDead() && !fished.isDead()) { // If neither of them are dead...
            if (fished instanceof Immune) {
                getPlayer().sendMessage(ChatColor.RED + fished.getPlayerName() + " " + Language.getRoleString("10") + ", " + ChatColor.GREEN + Language.getRoleString("1"));
                fished.getPlayer().sendMessage(ChatColor.RED + getPlayerName() + " " + Language.getRoleString("2"));
            }
            else {
                FishHook hook = (FishHook) e.getEntity();

                Vector v = fished.getPlayer().getLocation().toVector()
                        .subtract(hook.getLocation().toVector())
                        .setY(0)
                        .normalize()
                        .multiply(Xinada.getPlugin().getConfig().getInt("game.fisherKnockback"))
                        .setY(0.2);

                fished.getPlayer().setVelocity(v);
                hook.remove();

                ItemStack rod;
                if (!getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) rod = getPlayer().getInventory().getItemInMainHand();
                else rod = getPlayer().getInventory().getItemInOffHand();

                rod.setDurability((short) (rod.getDurability() + Material.FISHING_ROD.getMaxDurability() / Xinada.getPlugin().getConfig().getInt("game.fisherTimes")));
                if (rod.getDurability() > Material.FISHING_ROD.getMaxDurability()) {
                    Utils.soundIndividual(getPlayer(), Sound.ENTITY_ITEM_BREAK);
                    Utils.removeItem(getPlayer(), Material.FISHING_ROD);
                }
            }
        }
    }
}
