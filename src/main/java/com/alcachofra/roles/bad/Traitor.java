package com.alcachofra.roles.bad;

import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class Traitor extends Role {
    private boolean laughing = false;

    public Traitor(Player player) {
        super(
            player,
            Language.getRoleName("traitor"),
            Language.getRoleDescription("traitor"),
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
        if (isActivated()) {
            addSword();
        }
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (isActivated()) {
            if (!wwh.isDead() && !isDead()) { // If neither of them are dead...
                if (getPlayer().getInventory().getItemInMainHand().getType() == Material.IRON_SWORD) { // If the damage was above 1 heart...
                    if (wwh.getRoleSide() == Side.BAD) {
                        getPlayer().sendMessage(String.format(Language.getString("cannotMurderPal"), wwh.getPlayer().getName()));
                        wwh.getPlayer().sendMessage(String.format(Language.getString("triedMurderPal"), getPlayer().getName()));
                    } else {
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
