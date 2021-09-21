package com.alcachofra.roles.neutral;

import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import com.alcachofra.roles.good.Cop;
import com.alcachofra.roles.good.Immune;
import com.alcachofra.roles.good.Innocent;
import com.alcachofra.roles.good.Pirate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class Thief extends Role {
    public Thief(Player player) {
        super(
            player,
            Language.getRoleName("thief"),
            Language.getRoleDescription("thief"),
            Side.NEUTRAL
        );
    }

    @Override
    public void award() {}

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!isDead() && !wwh.isDead() && !isActivated()) {
            if (wwh instanceof Immune) {
                getPlayer().sendMessage(String.format(Language.getString("isImmune"), wwh.getPlayer().getName()));
                return;
            }
            if ((wwh instanceof Pirate) && !wwh.isActivated()) {
                getPlayer().sendMessage(Language.getString("cannotStealPirate"));
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
            if (victim.getRoleSide() == Side.BAD) {
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
                if (role.getPlayer().getName().equals(thief_player.getName())) roles.put(player, victim);
                else if (role.getPlayer().getName().equals(victim_player.getName())) roles.put(player, this);
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
            innocent.getPlayer().sendMessage(String.format(Language.getString("youAreNowA"), innocent.getRoleName()));
            victim.getPlayer().sendMessage(String.format(Language.getString("youAreNowThe"), victim.getRoleName()));
        }
    }
}
