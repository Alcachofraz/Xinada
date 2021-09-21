package com.alcachofra.roles.good;

import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
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
            Language.getRoleName("negotiator"),
            Language.getRoleDescription("negotiator"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        addPoint(); // Add 1 point
    }

    @Override
    public void reset() {
        if (partner != null) {
            setPartner(false);
            getPlayer().sendMessage(ChatColor.RED + Language.getString("partnershipEnded"));
        }
        if (isPartner()) {
            setPartner(false);
            getPlayer().sendMessage(ChatColor.RED + Language.getString("partnershipEnded"));
        }
        super.reset();
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!isActivated()) {
            if (!isDead()) { // If neither of them are dead...
                if (wwh instanceof Immune){
                    getPlayer().sendMessage(String.format(Language.getString("isImmune"), wwh.getPlayer().getName()));
                    wwh.getPlayer().sendMessage(String.format(Language.getString("triedToPartnership"), wwh.getPlayer().getName()) + ", " + Language.getString("butImmune"));
                    return;
                }

                setPartner(true);
                wwh.setPartner(true);
                partner = wwh;
                getPlayer().sendMessage(String.format(Language.getString("partnershipWith"), wwh.getPlayer().getName()));
                wwh.getPlayer().sendMessage(String.format(Language.getString("partnershipWithYou"), getPlayer().getName()));

                setActivated(true);

                Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG);
            }
        }
    }
}
