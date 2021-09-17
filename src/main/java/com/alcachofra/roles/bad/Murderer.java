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

public class Murderer extends Role {
    private Role accomplice;
    private boolean laughing = false;

    public Murderer(Player player) {
        super(
            player,
            Language.getRolesName("murderer"),
            Language.getRolesDescription("murderer"),
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
        addSword();
        setCanPickUp(false);
        setActivated(true);
        if ((accomplice = Xinada.getGame().getRound().getCurrentRole(Accomplice.class)) != null) {
            Utils.sendPopup(getPlayer(), getRoleName(), getColor() + accomplice.getPlayerName() + " " + Language.getRoleString("59"));
        } else {
            Utils.sendPopup(getPlayer(), getRoleName(), ChatColor.RED + Language.getRoleString("60"));
        }
    }

    @Override
    public void reset() {
        setCanPickUp(false);
        if (isActivated()) addSword();
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!wwh.isDead() && !isDead()) { // If neither of them are dead...
            if (getPlayer().getInventory().getItemInMainHand().getType() == Material.IRON_SWORD) { // If murderer did not use the sword, return
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
                } else if (wwh instanceof Accomplice) { // If Assassin hits Accomplice:
                    wwh.setActivated(true);
                    setActivated(false);

                    // Remove sword from Assassin:
                    getPlayer().getInventory().clear();

                    // Add sword to Accomplice:
                    wwh.addSword();

                    // Select Accomplice's slot 1
                    wwh.getPlayer().getInventory().setHeldItemSlot(0);

                    getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("61") + ChatColor.GRAY + " " + Language.getRoleString("62"));
                    wwh.getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("63") + ChatColor.GRAY + " " + Language.getRoleString("64"));

                    Utils.soundIndividual(wwh.getPlayer(), Sound.ENTITY_CHICKEN_EGG);
                } else if (wwh instanceof Monster) { // If Assassin hits Monster:
                    wwh.getPlayer().sendMessage(ChatColor.RED + getPlayerName() + " " + Language.getRoleString("65") + "," + ChatColor.GREEN + " " + Language.getRoleString("3"));
                    getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("4"));
                } else { // If Assassin hits anyone else:
                    if (wwh.getRoleSide() > -1) {
                        // Kill player:
                        wwh.kill(getPlayer());
                        Xinada.getGame().getRound().checkEnd();
                    } else {
                        getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("27") + " " + wwh.getPlayerName() + ". " + Language.getRoleString("28"));
                        wwh.getPlayer().sendMessage(ChatColor.RED + getPlayerName() + " " + Language.getRoleString("29"));
                    }
                }
            }
        }
    }

    @Override
    public void sendRole() {
        super.sendRole();
        if (accomplice != null) getPlayer().sendMessage(ChatColor.GRAY + Language.getRoleString("66") + " " + ChatColor.RED + accomplice.getPlayerName() + ChatColor.GRAY + "!");
        else getPlayer().sendMessage(ChatColor.GRAY + " > " + Language.getRoleString("60"));
    }
}
