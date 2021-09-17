package com.alcachofra.roles.good;

import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Witch extends Role {
    public Witch(Player player) {
        super(
            player,
            Language.getRolesName("witch"),
            Language.getRolesDescription("witch"),
            1
        );
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!isActivated()) {
            if (!isDead()) { // If neither of them are dead...
                if (wwh instanceof Immune) {
                    getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("2"));
                    wwh.getPlayer().sendMessage(ChatColor.GREEN + getPlayerName() + " " + Language.getRoleString("129") + ", " + ChatColor.GREEN + Language.getRoleString("1"));
                }

                wwh.setBlessed(false);
                wwh.setCursed(true);
                setActivated(true);

                getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("130") + " " + wwh.getPlayerName() + "!");
                wwh.getPlayer().sendMessage(ChatColor.RED + getPlayerName() + " " + Language.getRoleString("131"));

                Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_GHAST_SCREAM);
            }
        }
    }
}
