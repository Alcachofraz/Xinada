package com.alcachofra.roles.good;

import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import com.alcachofra.utils.Config;
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
            Language.getRoleName("athlete"),
            Language.getRoleDescription("athlete"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        setPoints(2);
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

    public void onSplash(PotionSplashEvent event) {
        if (!isDead() && !isActivated()) {
            setActivated(true);
            int athleteTime = Config.get(Xinada.GAME).getInt("game.athleteTime");

            getPlayer().sendMessage(String.format(Language.getString("pedalToTheMetal"), athleteTime));

            getPlayer().setWalkSpeed(0.8f);

            new BukkitRunnable() {
                public void run() {
                    getPlayer().setWalkSpeed(0.2f);
                    this.cancel();
                }
            }.runTaskLater(Xinada.getPlugin(), 20*athleteTime); // 20 ticks = 1 second
        }
        event.setCancelled(true);
    }
}
