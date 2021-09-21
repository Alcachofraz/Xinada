package com.alcachofra.roles.good;

import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Priest extends Role {
    public Priest(Player player) {
        super(
            player,
            Language.getRoleName("priest"),
            Language.getRoleDescription("priest"),
            Side.GOOD
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
                    getPlayer().sendMessage(String.format(Language.getString("isImmune"), wwh.getPlayer().getName()));
                    wwh.getPlayer().sendMessage(String.format(Language.getString("triedToBlessYou"), wwh.getPlayer().getName()) + ", " + Language.getString("butImmune"));
                }

                wwh.setBlessed(true);
                wwh.setCursed(false);
                setActivated(true);
                setPoints(1);

                getPlayer().sendMessage(String.format(Language.getString("blessed"), wwh.getPlayer().getName()));
                wwh.getPlayer().sendMessage(String.format(Language.getString("blessedYou"), getPlayer().getName()));

                Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_WITHER_AMBIENT);
            }
        }
    }
}
