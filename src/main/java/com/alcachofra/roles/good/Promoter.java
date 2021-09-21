package com.alcachofra.roles.good;

import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Promoter extends Role {
    public Promoter(Player player) {
        super(
            player,
            Language.getRoleName("promoter"),
            Language.getRoleDescription("promoter"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        setPoints(2);
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!isDead() && !wwh.isDead()) { // If neither of them are dead...
            if (!isActivated()) {
                if (wwh.getRoleSide() == Side.BAD) {
                    getPlayer().sendMessage(String.format(Language.getString("cannotPromote"), wwh.getPlayer().getName()));
                    wwh.getPlayer().sendMessage(String.format(Language.getString("triedToPromoteYouButBadGuy"), getPlayer().getName()));
                }
                else {
                    if (wwh instanceof Immune) {
                        getPlayer().sendMessage(String.format(Language.getString("isImmune"), wwh.getPlayer().getName()));
                        wwh.getPlayer().sendMessage(String.format(Language.getString("triedToPromoteYou"), wwh.getPlayer().getName()) + ", " + Language.getString("butImmune"));
                        return;
                    }
                    if (wwh.hasBow()) {
                        getPlayer().sendMessage(String.format(Language.getString("alreadyPromoted"), wwh.getPlayer().getName()));
                        return;
                    }

                    wwh.addBow(); // Give promoted player a Bow

                    // Player cannot pick up items anymore:
                    wwh.setCanPickUp(false);


                    wwh.getPlayer().sendMessage(
                        (wwh instanceof Cop) ?
                        (String.format(Language.getString("promotedButAlreadyPromoted"), getPlayer().getName())) :
                        (String.format(Language.getString("promotedYou"), getPlayer().getName()))
                    );
                    getPlayer().sendMessage(String.format(Language.getString("youPromoted"), wwh.getPlayer().getName()));

                    Utils.soundLocation(wwh.getPlayer().getLocation(), Sound.ENTITY_ELDER_GUARDIAN_FLOP);

                    setActivated(true);
                }
            }
        }
    }
}
