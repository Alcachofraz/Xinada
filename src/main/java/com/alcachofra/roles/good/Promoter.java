package com.alcachofra.roles.good;

import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Promoter extends Role {
    public Promoter(Player player) {
        super(
            player,
            Language.getRolesName("promoter"),
            Language.getRolesDescription("promoter"),
            1
        );
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!isDead() && !wwh.isDead()) { // If neither of them are dead...
            if (!isActivated()) {
                if (wwh.getRoleSide() < 0) {
                    getPlayer().sendMessage(ChatColor.BLUE + Language.getRoleString("115") + " " + wwh.getPlayerName() + ". " + ChatColor.RED + Language.getRoleString("116"));
                    wwh.getPlayer().sendMessage(ChatColor.BLUE + getPlayerName() + " " + Language.getRoleString("117") + " " + ChatColor.RED + Language.getRoleString("118"));
                }
                else {
                    if (wwh instanceof Immune) {
                        getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("2"));
                        wwh.getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("119") + ", " + Language.getRoleString("1"));
                        return;
                    }
                    if (wwh.hasBow()) {
                        getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("120"));
                        return;
                    }

                    wwh.addBow(); // Give promoted player a Bow:wwh.addBow();

                    // Player cannot pick up items anymore:
                    wwh.setCanPickUp(false);


                    wwh.getPlayer().sendMessage(
                        (wwh instanceof Cop) ?
                        (ChatColor.BLUE + getPlayerName() + " " + Language.getRoleString("121")) :
                        (ChatColor.BLUE + getPlayerName() + " " + Language.getRoleString("122"))
                    );
                    getPlayer().sendMessage(ChatColor.BLUE + Language.getRoleString("123") + " " + wwh.getPlayerName() + "!");

                    Utils.soundLocation(wwh.getPlayer().getLocation(), Sound.ENTITY_ELDER_GUARDIAN_FLOP);

                    setActivated(true);
                }
            }
        }
    }
}
