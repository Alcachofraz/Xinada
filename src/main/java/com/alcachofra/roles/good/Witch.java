package com.alcachofra.roles.good;

import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Witch extends Role {
    public Witch(Player player) {
        super(
            player,
            Language.getRoleName("witch"),
            Language.getRoleDescription("witch"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        setPoints(2);
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!isActivated()) {
            if (!isDead()) { // If neither of them are dead...
                if (wwh instanceof Immune) {
                    getPlayer().sendMessage(String.format(Language.getString("isImmune"), wwh.getPlayer().getName()));
                    wwh.getPlayer().sendMessage(String.format(Language.getString("triedToCurseYou"), wwh.getPlayer().getName()) + ", " + Language.getString("butImmune"));
                }

                wwh.setBlessed(false);
                wwh.setCursed(true);
                setActivated(true);

                getPlayer().sendMessage(String.format(Language.getString("youCursed"), wwh.getPlayer().getName()));
                wwh.getPlayer().sendMessage(String.format(Language.getString("cursedYou"), getPlayer().getName()));

                Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_GHAST_SCREAM);
            }
        }
    }
}
