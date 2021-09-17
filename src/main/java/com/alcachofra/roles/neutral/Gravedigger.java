package com.alcachofra.roles.neutral;

import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import com.alcachofra.roles.good.Cop;
import com.alcachofra.roles.good.Immune;
import com.alcachofra.roles.good.Innocent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class Gravedigger extends Role {
    public Gravedigger(Player player) {
        super(
            player,
            Language.getRolesName("gravedigger"),
            Language.getRolesDescription("gravedigger"),
            0
        );
    }

    @Override
    public void award() {
        // No points (neutral)
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!isDead() && wwh.isDead() && !isActivated()) {
            if (wwh instanceof Immune) {
                getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("2"));
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
            if (victim.getRoleSide() < 0) {
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
                if (role.getPlayerName().equals(gravedigger_player.getName())) roles.put(player, victim);
                else if (role.getPlayerName().equals(victim_player.getName())) roles.put(player, this);
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
            innocent.getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("132") + " " + innocent.getRoleName() + "!");
            victim.getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("133") + " " + victim.getRoleName() + "!");
        }
    }
}
