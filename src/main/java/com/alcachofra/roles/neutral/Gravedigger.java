package com.alcachofra.roles.neutral;

import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import com.alcachofra.roles.good.Cop;
import com.alcachofra.roles.good.Immune;
import com.alcachofra.roles.good.Innocent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class Gravedigger extends Role {
    public Gravedigger(Player player) {
        super(
            player,
            Language.getRoleName("gravedigger"),
            Language.getRoleDescription("gravedigger"),
            Side.NEUTRAL
        );
    }

    @Override
    public void award() {}

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!isDead() && wwh.isDead() && !isActivated()) {
            if (wwh instanceof Immune) {
                getPlayer().sendMessage(String.format(Language.getString("isImmune"), wwh.getPlayer().getName()));
                return;
            }
            // Roles:
            Map<Player, Role> roles = Xinada.getGame().getRound().getCurrentRoles();

            // Victim Role:
            Role victim = wwh;

            Player victim_player = victim.getPlayer(); // Stolen player
            Player gravedigger_player = getPlayer(); // Stealing player

            victim_player.getInventory().clear();
            victim_player.closeInventory(); // Close inventory, in case of Terrorist

            // If a bad Role is stolen, remove bow Gravedigger may have:
            if (victim.getRoleSide() == Side.BAD) {
                Utils.removeItem(gravedigger_player, Material.BOW);
                Utils.removeItem(gravedigger_player, Material.ARROW);
                setHasBow(false);
            }

            // Create a new Innocent and copy victim status to it:
            Role innocent = new Innocent(victim_player);
            innocent.setCursed(victim.isCursed());
            innocent.setBlessed(victim.isBlessed());
            innocent.setMerged(victim.isMerged());
            innocent.setExposed(victim.isExposed());
            innocent.setPartner(victim.isPartner());
            innocent.setInLove(victim.isInLove());
            innocent.setCanPickUp(victim.canPickUp());
            innocent.setDead(victim.isDead());
            innocent.setHasBow(victim.hasBow());
            if (victim instanceof Cop) { // If stolen was a Cop, he will not keep the bow
                // Remove bow from Gravedigger, cuz he'll get it when Cop resets:
                Utils.removeItem(gravedigger_player, Material.BOW);
                Utils.removeItem(gravedigger_player, Material.ARROW);
                setHasBow(false);
            }

            // Before anything, switch positions (to not fuck the points up)
            roles.forEach((player, role) -> {
                if (role.getPlayer().getName().equals(gravedigger_player.getName())) roles.put(player, victim);
                else if (role.getPlayer().getName().equals(victim_player.getName())) roles.put(player, this);
            });

            // Copy Gravedigger status to the victim Role:
            victim.setPlayer(gravedigger_player);
            victim.setCursed(isCursed());
            victim.setBlessed(isBlessed());
            victim.setMerged(isMerged());
            victim.setExposed(isExposed());
            victim.setPartner(isPartner());
            victim.setInLove(isInLove());
            victim.setCanPickUp(canPickUp());
            victim.setDead(isDead());

            // Gravedigger will keep the bow, if he had one:
            victim.setHasBow(hasBow());

            // Reset stolen Role parameters:
            victim.reset();

            // Once it gets here, Gravedigger has taken over the stolen position already
            roles.forEach((player, role) -> { // Replace old Gravedigger position with Innocent (with player victim)
                if (role instanceof Gravedigger) roles.put(player, innocent);
            });
            innocent.getPlayer().sendMessage(String.format(Language.getString("youAreNowA"), innocent.getRoleName()));
            victim.getPlayer().sendMessage(String.format(Language.getString("youAreNowThe"), victim.getRoleName()));
        }
    }
}
