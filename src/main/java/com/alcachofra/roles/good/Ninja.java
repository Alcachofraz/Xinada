package com.alcachofra.roles.good;

import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import com.alcachofra.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public class Ninja extends Role {
    public Ninja(Player player) {
        super(
            player,
            Language.getRolesName("ninja"),
            Language.getRolesDescription("ninja"),
            1
        );
    }

    @Override
    public void initialise() {
        getPlayer().getInventory().setItem(8, getInvisibilityPotion());
        super.initialise();
    }

    @Override
    public void reset() {
        getPlayer().getInventory().setItem(8, getInvisibilityPotion());
        super.reset();
    }

    private ItemStack getInvisibilityPotion() {
        ItemStack potion = new ItemStack(Material.SPLASH_POTION, 1);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        if (meta != null) meta.setBasePotionData(new PotionData(PotionType.INVISIBILITY));
        potion.setItemMeta(meta);
        return potion;
    }

    public void onSplash(PotionSplashEvent e) {
        if (!isDead() && !isActivated()) {
            setActivated(true);
            int ninjaTime = Xinada.getPlugin().getConfig().getInt("game.ninjaTime");

            getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("102") + " " + ninjaTime + " " + Language.getRoleString("75"));

            Utils.setInvisible(getPlayer());

            new BukkitRunnable() {
                public void run() {
                    Utils.setVisible(getPlayer());
                    this.cancel();
                }
            }.runTaskLater(Xinada.getPlugin(), 20*ninjaTime); // 20 ticks = 1 second
        }
        e.setCancelled(true);
    }
}
