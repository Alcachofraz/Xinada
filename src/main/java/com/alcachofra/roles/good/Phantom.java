package com.alcachofra.roles.good;

import com.alcachofra.main.Xinada;
import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import org.bukkit.*;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Phantom extends Role {
    public Phantom(Player player) {
        super(
            player,
            Language.getRoleName("phantom"),
            Language.getRoleDescription("phantom"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        setPoints(2);
    }

    @Override
    public void initialise() {
        Utils.addItem(getPlayer(), Material.SNOWBALL, 8, 10);
        super.initialise();
    }

    @Override
    public void reset() {
        Utils.addItem(getPlayer(), Material.SNOWBALL, 8, 10);
        super.reset();
    }

    @Override
    public void kill(Player by) {
        if (isDead()) return;
        setCanPickUp(false);
        setDead(true);
        setKiller(by);
        setDeathLocation(getPlayer().getLocation());
        if (hasBow()) dropBow();
        setHasBow(false);
        if (isMerged()) killMerged();

        Utils.messageGlobal(
                String.format(
                Language.getString("died"),
                getPlayer().getName()
        ));
        Utils.soundGlobal(Sound.ENTITY_LIGHTNING_BOLT_THUNDER);
        Utils.sendPopup(
                getPlayer(),
                Language.getString("youDied"),
                Language.getString("spectatorMode")
        );

        Xinada.getGame().updateTabList();

        getPlayer().closeInventory();
        getPlayer().setWalkSpeed(0); // Player whom was hit loses ability to walk
        getPlayer().setFlySpeed(0); // Player whom was hit loses ability to fly
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128)); // Player whom was hit loses ability to jump
    }

    @Override
    public void revive() {
        super.revive();
        getPlayer().removePotionEffect(PotionEffectType.JUMP); // Player gains the ability to jump
        getPlayer().setWalkSpeed(0.2f); // Player gains the ability to walk
        getPlayer().setFlySpeed(0.2f);
        getPlayer().setHealth(20); // Player's health is restored
        getPlayer().setFoodLevel(20); // Player's hunger is restored
    }

    @Override
    public void onMove(PlayerMoveEvent event) {
        if (isDead()) {
            if (event.getTo() != null) {
                if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY() || event.getFrom().getZ() != event.getTo().getZ()) {
                    event.setTo(event.getFrom());
                }
            }
        }
    }

    @Override
    public void onSnowball(ProjectileHitEvent event) {
        if (event.getHitBlock() == null) return;
        attemptRevive(event.getHitBlock().getLocation());
    }

    @Override
    public void onInteract(PlayerInteractEvent event, Action action) {
        if (event.getClickedBlock() == null) return;
        super.onInteract(event, action);
        attemptRevive(event.getClickedBlock().getLocation());
    }

    public void attemptRevive(Location hitBlock) {
        for (Role role : Xinada.getGame().getRound().getCurrentRoles().values()) {
            if (role.isDead() && role.getPotLocation() != null &&
                    role.getPotLocation().getX() == hitBlock.getX() &&
                    role.getPotLocation().getY() == hitBlock.getY() &&
                    role.getPotLocation().getZ() == hitBlock.getZ()) {
                if (isDead()) {
                    if (role instanceof Immune) { // If who was shot is the Immune...
                        role.getPlayer().sendMessage(String.format(Language.getString("triedToRevive"), getPlayer().getName()) + ", " + Language.getString("butImmune"));
                        getPlayer().sendMessage(String.format(Language.getString("isImmune"), role.getPlayer().getName()));
                        return;
                    }
                    role.revive();
                    role.getPlayer().sendMessage(String.format(Language.getString("revivedYou"), getPlayer().getName()));
                    getPlayer().sendMessage(String.format(Language.getString("youRevived"), role.getPlayer().getName()));
                    setActivated(true);
                    setPoints(1); // Give Phantom 1 point for healing someone
                }
            }
        }
    }
}
