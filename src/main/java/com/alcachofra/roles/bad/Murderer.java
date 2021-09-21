package com.alcachofra.roles.bad;

import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class Murderer extends Role {
    private Role accomplice;
    private boolean laughing = false;

    public Murderer(Player player) {
        super(
            player,
            Language.getRoleName("murderer"),
            Language.getRoleDescription("murderer"),
            Side.BAD
        );
    }

    @Override
    public void award() {
        setPoints(2);
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
            Utils.sendPopup(getPlayer(), getRoleName(), String.format(Language.getString("isAccomplice"), accomplice.getPlayer().getName()));
        } else {
            Utils.sendPopup(getPlayer(), getRoleName(), ChatColor.RED + Language.getString("noAccomplice"));
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

                        getPlayer().sendMessage(String.format(Language.getString("isTraitor"), wwh.getPlayer().getName()));
                        wwh.getPlayer().sendMessage(Language.getString("murderedButTraitor"));

                        Utils.soundIndividual(getPlayer(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY);
                        Utils.soundIndividual(wwh.getPlayer(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY);
                    } else {
                        getPlayer().sendMessage(String.format(Language.getString("cannotMurderPal"), wwh.getPlayer().getName()));
                        wwh.getPlayer().sendMessage(String.format(Language.getString("triedMurderPal"), getPlayer().getName()));
                    }
                }
                else if (wwh instanceof Accomplice) { // If Assassin hits Accomplice:
                    wwh.setActivated(true);
                    setActivated(false);

                    // Remove sword from Assassin:
                    getPlayer().getInventory().clear();

                    // Add sword to Accomplice:
                    wwh.addSword();

                    // Select Accomplice's slot 1
                    wwh.getPlayer().getInventory().setHeldItemSlot(0);

                    getPlayer().sendMessage(Language.getString("knifeReturnedToAccomplice"));
                    wwh.getPlayer().sendMessage(Language.getString("borrowedMurdererKnife"));

                    Utils.soundIndividual(wwh.getPlayer(), Sound.ENTITY_CHICKEN_EGG);
                }
                else if (wwh.getRoleSide() == Side.BAD) {
                    getPlayer().sendMessage(String.format(Language.getString("cannotMurderPal"), wwh.getPlayer().getName()));
                    wwh.getPlayer().sendMessage(String.format(Language.getString("triedMurderPal"), getPlayer().getName()));
                }
                else {
                    // Kill player:
                    wwh.kill(getPlayer());
                    Xinada.getGame().getRound().checkEnd();
                }
            }
        }
    }

    @Override
    public void onPickup(EntityPickupItemEvent event) {
        if (event.getItem().getItemStack().getType() == Material.IRON_SWORD) {
            if (!isLaughing()) { // Not laughing
                setActivated(true); // Murderer is activated
                Xinada.getGame().getRound().subtractSwordsDropped();
                // All good, can pick up:
                return;
            }
        }
        event.setCancelled(true);
    }

    @Override
    public void sendRole() {
        super.sendRole();
        if (accomplice != null) getPlayer().sendMessage(String.format(Language.getString("accompliceIs"), accomplice.getPlayer().getName()));
        else getPlayer().sendMessage(ChatColor.GRAY + Language.getString("noAccomplice"));
    }
}
