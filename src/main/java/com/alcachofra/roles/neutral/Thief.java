package com.alcachofra.roles.neutral;

import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import com.alcachofra.roles.good.Cop;
import com.alcachofra.roles.good.Immune;
import com.alcachofra.roles.good.Innocent;
import com.alcachofra.roles.good.Pirate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class Thief extends Role {
    public Thief(Player player) {
        super(
            player,
            Language.getRolesName("thief"),
            Language.getRolesDescription("thief"),
            0
        );
    }

    @Override
    public void award() {
        // No points (neutral)
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!isDead() && !wwh.isDead() && !isActivated()) {
            if (wwh instanceof Immune) {
                getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("2"));
                return;
            }
            if ((wwh instanceof Pirate) && !wwh.isActivated()) {
                getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("134"));
                return;
            }
            // Roles:
            Map<Player, Role> roles = Xinada.getGame().getRound().getCurrentRoles();

            // Victim Role:
            Role victim = wwh;

            Player victim_player = victim.getPlayer(); // Stolen player
            Player thief_player = getPlayer(); // Stealing player

            victim_player.getInventory().clear(); // Clear victim's inventory
            victim_player.closeInventory(); // Close inventory, in case of Terrorist

            // If victim is a bad role, remove bow thief may have:
            if (victim.getRoleSide() < 0) {
                Utils.removeItem(thief_player, Material.BOW);
                Utils.removeItem(thief_player, Material.ARROW);
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
            if (victim instanceof Cop) { // If victim was a Cop, he will not keep the bow
                // Remove bow from thief, cuz he'll get it when Cop resets:
                Utils.removeItem(thief_player, Material.BOW);
                Utils.removeItem(thief_player, Material.ARROW);
                setHasBow(false);

                innocent.setHasBow(false); // Innocent will not keep the bow
                innocent.setCanPickUp(true);
            }
            else if (innocent.hasBow()) { // Innocent keeps the bow, if he had one
                innocent.addBow();
            }

            // Before anything, switch positions (to not mess the points up)
            roles.forEach((player, role) -> {
                if (role.getPlayerName().equals(thief_player.getName())) roles.put(player, victim);
                else if (role.getPlayerName().equals(victim_player.getName())) roles.put(player, this);
            });

            // Copy Thief status to the victim Role:
            victim.setPlayer(thief_player);
            victim.setCursed(isCursed());
            victim.setBlessed(isBlessed());
            victim.setMerged(isMerged());
            victim.setExposed(isExposed());
            victim.setPartner(isPartner());
            victim.setInLove(isInLove());
            victim.setCanPickUp(canPickUp());
            victim.setDead(isDead());

            // Thief will keep the bow, if he had one (unless nes role is bad, which has been already dealt with):
            victim.setHasBow(hasBow());

            // Reset victim Role parameters:
            victim.reset();

            // Once it gets here, thief has taken over the victim position already
            roles.forEach((player, role) -> { // Replace old Thief position with Innocent (with player victim)
                if (role instanceof Thief) roles.put(player, innocent);
            });
            innocent.getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("132") + " " + innocent.getRoleName() + "!");
            victim.getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("133") + " " + victim.getRoleName() + "!");
        }
    }
}
