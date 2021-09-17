package com.alcachofra.roles.good;

import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import com.alcachofra.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Survivor extends Role {
    public Survivor(Player player) {
        super(
            player,
            Language.getRolesName("survivor"),
            Language.getRolesDescription("survivor"),
            1
        );
    }

    @Override
    public void kill(Player by) {
        if (isActivated()) {
            super.kill(by);
        }
        else {
            if (isDead()) return;
            setCanPickUp(false);
            setDead(true);
            setKiller(by);
            setDeathLocation(getPlayer().getLocation());
            setPotLocation();
            if (hasBow()) dropBow();
            setHasBow(false);
            if (isMerged()) killMerged();

            Utils.messageGlobal(ChatColor.GRAY + " > " + ChatColor.RED + getPlayer().getName() + " " + Language.getRoleString("16"));
            Utils.soundGlobal(Sound.ENTITY_LIGHTNING_BOLT_THUNDER);
            Utils.sendPopup(
                    getPlayer(),
                    ChatColor.RED + Language.getRoleString("17"),
                    ChatColor.RED + (!isActivated() ? Language.getRoleString("18") : Language.getRoleString("19"))
            );

            Xinada.getGame().updateTabList();

            // Spawn pot:
            getPotLocation().getBlock().setType(Material.POTTED_RED_TULIP);

            getPlayer().closeInventory();
            getPlayer().setWalkSpeed(0); // Player whom was hit loses ability to walk
            getPlayer().setFlySpeed(0); // Player whom was hit loses ability to fly
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128)); // Player whom was hit loses ability to jump
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 250)); // Player whom was hit loses ability to see

            Utils.setInvisible(getPlayer());
        }
    }

    @Override
    public void revive() {
        super.revive();
        getPlayer().removePotionEffect(PotionEffectType.JUMP); // Player gains the ability to jump
        getPlayer().removePotionEffect(PotionEffectType.BLINDNESS); // Player gains the ability to see
        getPlayer().setWalkSpeed(0.2f); // Player gains the ability to walk
        getPlayer().setFlySpeed(0.2f);
        getPlayer().setHealth(20); // Player's health is restored
        getPlayer().setFoodLevel(20); // Player's hunger is restored
        Utils.setVisible(getPlayer());
    }
}
