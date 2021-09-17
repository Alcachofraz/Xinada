package com.alcachofra.roles.good;

import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Negotiator extends Role {
    private Role partner;

    public Negotiator(Player player) {
        super(
            player,
            Language.getRolesName("negotiator"),
            Language.getRolesDescription("negotiator"),
            1
        );
    }

    @Override
    public void reset() {
        if (partner != null) {
            setPartner(false);
            getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("98"));
        }
        if (isPartner()) {
            setPartner(false);
            getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("98"));
        }
        super.reset();
    }

    @Override
    public void award() {
        addPoint(); // Add 1 point
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!isActivated()) {
            if (!isDead()) { // If neither of them are dead...
                if (wwh instanceof Immune){
                    getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("2"));
                    wwh.getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("99") + ", " + Language.getRoleString("1"));
                    return;
                }

                setPartner(true);
                wwh.setPartner(true);
                partner = wwh;
                getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("100") + " " + wwh.getPlayerName() + "!");
                wwh.getPlayer().sendMessage(ChatColor.GREEN + getPlayerName() + " " + Language.getRoleString("101"));

                setActivated(true);

                Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG);
            }
        }
    }
}
