package com.alcachofra.roles.good;

import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Analyst extends Role {
    public Analyst(Player player) {
        super(
            player,
            Language.getRolesName("analyst"),
            Language.getRolesDescription("analyst"),
            1
        );
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!isDead() && wwh.isDead()) { // If the target is dead...
            if (wwh instanceof Immune) {
                getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("2"));
                wwh.getPlayer().sendMessage(ChatColor.RED + getPlayerName() + " " + Language.getRoleString("71") + ", " + ChatColor.GREEN + Language.getRoleString("1"));
            }

            if (wwh.getKiller().getName().equals(wwh.getPlayerName())) {
                getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("72"));
            }
            else getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("73") + " " + wwh.getKiller().getName() + "!");
        }
    }
}
