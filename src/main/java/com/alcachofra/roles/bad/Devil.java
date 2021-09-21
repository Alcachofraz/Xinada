package com.alcachofra.roles.bad;

import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
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
            Language.getRoleName("devil"),
            Language.getRoleDescription("devil"),
            Side.BAD
        );
    }

    @Override
    public void award() {
        addPoint(); // Add 1 point
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
                role.getPlayer().sendMessage(ChatColor.GREEN + Language.getString("devilFreedSoul"));
            }
        }
        souls.clear();
        super.reset();
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!wwh.isDead() && !isDead()) { // If neither of them are dead...
            if (wwh instanceof Immune) {
                wwh.getPlayer().sendMessage(String.format(Language.getString("triedStealSoul"), getPlayer().getName()) + ", " + Language.getString("butImmune"));
                getPlayer().sendMessage(String.format(Language.getString("isImmune"), wwh.getPlayer().getName()));
            }
            else if (wwh instanceof Monster) {
                wwh.getPlayer().sendMessage(String.format(Language.getString("triedStealSoul"), getPlayer().getName()) + ", " + Language.getString("butImmortal"));
                getPlayer().sendMessage(String.format(Language.getString("isImmortal"), wwh.getPlayer().getName()));
            }
            else if (souls.size() == 0) {
                souls.add(wwh);
                wwh.setMerged(true);

                getPlayer().sendMessage(String.format(Language.getString("youStoleSoul"), wwh.getPlayer().getName()));
                wwh.getPlayer().sendMessage(Language.getString("devilStoleSoul"));
                Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_PLAYER_BURP);
            }
            else if (souls.size() == 1) {
                if (souls.get(0).getPlayer().getName().equals(wwh.getPlayer().getName())) {
                    getPlayer().sendMessage(String.format(Language.getString("alreadyStoleSoul"), wwh.getPlayer().getName()));
                    return;
                }
                souls.add(wwh);
                wwh.setMerged(true);

                getPlayer().sendMessage(String.format(Language.getString("soulsMerged"), souls.get(0).getPlayer().getName(), souls.get(1).getPlayer().getName()));
                souls.get(0).getPlayer().sendMessage(String.format(Language.getString("yourSoulMergedWith"), souls.get(1).getPlayer().getName()));
                souls.get(1).getPlayer().sendMessage(String.format(Language.getString("yourSoulMergedWith"), souls.get(0).getPlayer().getName()));
                Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_PLAYER_BURP);
            }
        }
    }
}
