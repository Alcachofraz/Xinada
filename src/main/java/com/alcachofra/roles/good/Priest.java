package com.alcachofra.roles.good;

import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Priest extends Role {
    public Priest(Player player) {
        super(
            player,
            Language.getRolesName("priest"),
            Language.getRolesDescription("priest"),
            1
        );
    }

    @Override
    public void award() {
        addPoint(); // Add 1 point
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!isActivated()) {
            if (!isDead()) { // If neither of them are dead...
                if (wwh instanceof Immune) {
                    getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("2"));
                    wwh.getPlayer().sendMessage(ChatColor.GREEN + getPlayerName() + " " + Language.getRoleString("112") + ", " + Language.getRoleString("1"));
                }

                wwh.setBlessed(true);
                wwh.setCursed(false);
                setActivated(true);
                setPoints(1);

                getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("113") + " " + wwh.getPlayerName() + "!");
                wwh.getPlayer().sendMessage(ChatColor.RED + getPlayerName() + " " + Language.getRoleString("114"));

                Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_WITHER_AMBIENT);
            }
        }
    }
}
