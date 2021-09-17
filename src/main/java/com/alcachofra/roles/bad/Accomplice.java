package com.alcachofra.roles.bad;

import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Accomplice extends Role {
    private boolean laughing = false;

    public Accomplice(Player player) {
        super(
            player,
            Language.getRolesName("accomplice"),
            Language.getRolesDescription("accomplice"),
            -1
        );
    }

    public boolean isLaughing() {
        return laughing;
    }

    public void setLaughing(boolean laughing) {
        this.laughing = laughing;
    }

    @Override
    public void initialise() {
        setCanPickUp(false);
        super.initialise();
    }

    @Override
    public void reset() {
        setCanPickUp(false);
        if (isActivated()) addSword();
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (isActivated()) {
            if (!wwh.isDead() && !isDead()) { // If neither of them are dead...
                if (getPlayer().getInventory().getItemInMainHand().getType() == Material.IRON_SWORD) { // If the damage was above 1 heart...
                    if (wwh instanceof Traitor) { // If Assassin hits Traitor:
                        if (!wwh.isActivated()) {
                            wwh.setActivated(true);

                            // Player cannot pick up bows anymore:
                            wwh.setCanPickUp(false);

                            // Add sword to Traitor:
                            wwh.addSword();

                            getPlayer().sendMessage(ChatColor.GREEN + wwh.getPlayerName() + " " + Language.getRoleString("24"));
                            wwh.getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("25") + "," + ChatColor.GREEN + " " + Language.getRoleString("26"));

                            Utils.soundIndividual(getPlayer(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY);
                            Utils.soundIndividual(wwh.getPlayer(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY);
                        } else {
                            getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("27") + " " + wwh.getPlayerName() + ". " + Language.getRoleString("28"));
                            wwh.getPlayer().sendMessage(ChatColor.RED + getPlayerName() + " " + Language.getRoleString("29"));
                        }
                    }
                    else if (wwh instanceof Murderer) {
                        setActivated(false);
                        wwh.setActivated(true);

                        // Remove sword from Accomplice:
                        Utils.removeItem(getPlayer(), Material.IRON_SWORD);

                        // Add sword to Assassin:
                        wwh.addSword();

                        // Select slot 1
                        wwh.getPlayer().getInventory().setHeldItemSlot(0);

                        getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("30"));
                        wwh.getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("31"));

                        Utils.soundLocation(wwh.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG);
                    } else if (wwh.getRoleSide() < 0) {
                        getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("27") + " " + wwh.getPlayerName() + ". " + Language.getRoleString("28"));
                        wwh.getPlayer().sendMessage(ChatColor.GREEN + wwh.getPlayerName() + " " + Language.getRoleString("29"));
                    } else {
                        wwh.kill(this.getPlayer()); // Kill player
                        Xinada.getGame().getRound().checkEnd();
                    }
                }
            }
        }
    }
}
