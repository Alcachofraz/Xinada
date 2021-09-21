package com.alcachofra.roles.good;

import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import com.alcachofra.utils.Config;
import com.alcachofra.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

public class Ninja extends Role {
    public Ninja(Player player) {
        super(
            player,
            Language.getRoleName("ninja"),
            Language.getRoleDescription("ninja"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        setPoints(2);
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

    public void onSplash(PotionSplashEvent event) {
        if (!isDead() && !isActivated()) {
            setActivated(true);
            int ninjaTime = Config.get(Xinada.GAME).getInt("game.ninjaTime");

            getPlayer().sendMessage(String.format(Language.getString("invisibleFor"), ninjaTime));

            Utils.setInvisible(getPlayer());

            new BukkitRunnable() {
                public void run() {
                    Utils.setVisible(getPlayer());
                    this.cancel();
                }
            }.runTaskLater(Xinada.getPlugin(), 20*ninjaTime); // 20 ticks = 1 second
        }
        event.setCancelled(true);
    }
}
