package com.alcachofra.roles.good;

import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Analyst extends Role {
    public Analyst(Player player) {
        super(
            player,
            Language.getRoleName("analyst"),
            Language.getRoleDescription("analyst"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        setPoints(2);
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!isDead() && wwh.isDead()) { // If the target is dead...
            if (wwh instanceof Immune) {
                getPlayer().sendMessage(String.format(Language.getString("isImmune"), wwh.getPlayer().getName()));
                wwh.getPlayer().sendMessage(String.format(Language.getString("triedToAnalyseYou"), getPlayer().getName()) + ", " + Language.getString("butImmune"));
            }

            if (wwh.getKiller().getName().equals(wwh.getPlayer().getName())) {
                getPlayer().sendMessage(String.format(Language.getString("killedHimself"), wwh.getPlayer().getName()));
            }
            else getPlayer().sendMessage(String.format(Language.getString("wasKilledBy"), wwh.getPlayer().getName(), wwh.getKiller().getName()));
        }
    }
}
