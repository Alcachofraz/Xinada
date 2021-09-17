package com.alcachofra.roles.bad;

import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import com.alcachofra.roles.good.Immune;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class Devil extends Role {
    private final ArrayList<Role> souls = new ArrayList<>();

    public Devil(Player player) {
        super(
            player,
            Language.getRolesName("devil"),
            Language.getRolesDescription("devil"),
            -1
        );
    }

    @Override
    public void initialise() {
        setCanPickUp(false);
        super.initialise();
    }

    @Override
    public void reset() {
        setCanPickUp(false);
        for (Role role : souls) {
            if (role.isMerged()) {
                role.setMerged(false);
                role.getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("44"));
            }
        }
        souls.clear();
        super.reset();
    }

    @Override
    public void award() {
        addPoint(); // Add 1 point
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!wwh.isDead() && !isDead()) { // If neither of them are dead...
            if (wwh instanceof Monster) {
                wwh.getPlayer().sendMessage(ChatColor.RED + getPlayerName() + " " + Language.getRoleString("45") + ", " + ChatColor.GREEN + Language.getRoleString("3"));
                getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("4"));
                return;
            }
            if (wwh instanceof Immune) {
                wwh.getPlayer().sendMessage(ChatColor.RED + getPlayerName() + " " + Language.getRoleString("45") + ", " + ChatColor.GREEN + Language.getRoleString("1"));
                getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("2"));
                return;
            }
            if (souls.size() == 0) {
                souls.add(wwh);
                wwh.setMerged(true);

                getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("33") + " " + wwh.getPlayerName() + Language.getRoleString("46"));
                wwh.getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("47"));
                Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_PLAYER_BURP);
            }
            else if (souls.size() == 1) {
                if (souls.get(0).getPlayerName().equals(wwh.getPlayerName())) {
                    getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("36") + " " + wwh.getPlayerName() + Language.getRoleString("46"));
                    return;
                }
                souls.add(wwh);
                wwh.setMerged(true);

                getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("48") + " " + souls.get(0).getPlayerName() + Language.getRoleString("49") + " " + souls.get(1).getPlayerName() + Language.getRoleString("50"));
                souls.get(0).getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("51") + " " + souls.get(1).getPlayerName() + Language.getRoleString("50"));
                souls.get(1).getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("51") + " " + souls.get(0).getPlayerName() + Language.getRoleString("50"));
                Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_PLAYER_BURP);
            }
        }
    }
}
