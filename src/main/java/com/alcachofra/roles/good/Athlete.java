package com.alcachofra.roles.good;

import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

public class Athlete extends Role {
    public Athlete(Player player) {
        super(
            player,
            Language.getRolesName("athlete"),
            Language.getRolesDescription("athlete"),
            1
        );
    }

    @Override
    public void initialise() {
        getPlayer().getInventory().setItem(8, getSpeedPotion());
        super.initialise();
    }

    @Override
    public void reset() {
        getPlayer().getInventory().setItem(8, getSpeedPotion());
        super.reset();
    }

    private ItemStack getSpeedPotion() {
        ItemStack potion = new ItemStack(Material.SPLASH_POTION, 1);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        if (meta != null) meta.setBasePotionData(new PotionData(PotionType.SPEED));
        potion.setItemMeta(meta);
        return potion;
    }

    public void onSplash(PotionSplashEvent e) {
        if (!isDead() && !isActivated()) {
            setActivated(true);
            int athleteTime = Xinada.getPlugin().getConfig().getInt("game.athleteTime");

            getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("74") + " " + athleteTime + " " + Language.getRoleString("75"));

            getPlayer().setWalkSpeed(0.8f);

            new BukkitRunnable() {
                public void run() {
                    getPlayer().setWalkSpeed(0.2f);
                    this.cancel();
                }
            }.runTaskLater(Xinada.getPlugin(), 20*athleteTime); // 20 ticks = 1 second
        }
        e.setCancelled(true);
    }
}
