package com.alcachofra.roles.bad;

import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Monster extends Role {
    public Monster(Player player) {
        super(
            player,
            Language.getRoleName("monster"),
            Language.getRoleDescription("monster"),
            Side.BAD
        );
    }
    @Override
    public void award() {}


    @Override
    public void initialise() {
        setCanPickUp(false);
        super.initialise();
    }

    @Override
    public void reset() {
        setCanPickUp(false);
        super.reset();
    }

    @Override
    public void onShot(EntityDamageByEntityEvent event, Role whoShot) {
        if (!getPlayer().isDead() && !whoShot.getPlayer().isDead()) { // If neither of them are dead...
            if (!getPlayer().equals(whoShot.getPlayer())) {
                // Kill who shot:
                whoShot.kill(whoShot.getPlayer());

                // Monster scores 2 points
                setPoints(2);

                whoShot.getPlayer().sendMessage(String.format(Language.getString("immortalYouAreDemoted"), getPlayer().getName()));
                getPlayer().sendMessage(String.format(Language.getString("triedToShootYou"), whoShot.getPlayer().getName()) + ", " + Language.getString("butImmortal"));
                Xinada.getGame().getRound().checkEnd();
            }
            else whoShot.getPlayer().sendMessage(Language.getString("selfShot"));
        }
    }
}
