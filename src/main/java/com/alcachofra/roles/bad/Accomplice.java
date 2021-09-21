package com.alcachofra.roles.bad;

import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class Accomplice extends Role {
    private boolean laughing = false;

    public Accomplice(Player player) {
        super(
            player,
            Language.getRoleName("accomplice"),
            Language.getRoleDescription("accomplice"),
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

                            getPlayer().sendMessage(String.format(Language.getString("isTraitor"), wwh.getPlayer().getName()));
                            wwh.getPlayer().sendMessage(Language.getString("murderedButTraitor"));

                            Utils.soundIndividual(getPlayer(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY);
                            Utils.soundIndividual(wwh.getPlayer(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY);
                        } else {
                            getPlayer().sendMessage(String.format(Language.getString("cannotMurderPal"), wwh.getPlayer().getName()));
                            wwh.getPlayer().sendMessage(String.format(Language.getString("triedMurderPal"), getPlayer().getName()));
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

                        getPlayer().sendMessage(Language.getString("knifeReturnedToMurderer"));
                        wwh.getPlayer().sendMessage(Language.getString("accompliceReturnedKnife"));

                        Utils.soundLocation(wwh.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG);
                    }
                    else if (wwh.getRoleSide() == Side.BAD) {
                        getPlayer().sendMessage(String.format(Language.getString("cannotMurderPal"), wwh.getPlayer().getName()));
                        wwh.getPlayer().sendMessage(String.format(Language.getString("triedMurderPal"), getPlayer().getName()));
                    }
                    else {
                        wwh.kill(this.getPlayer()); // Kill player
                        Xinada.getGame().getRound().checkEnd();
                    }
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
}
