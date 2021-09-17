package com.alcachofra.roles.good;

import com.alcachofra.main.Xinada;
import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import org.bukkit.ChatColor;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Phantom extends Role {
    public Phantom(Player player) {
        super(
            player,
            Language.getRolesName("phantom"),
            Language.getRolesDescription("phantom"),
            1
        );
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

        Utils.messageGlobal(ChatColor.GRAY + " > " + ChatColor.RED + getPlayer().getName() + " " + Language.getRoleString("16"));
        Utils.soundGlobal(Sound.ENTITY_LIGHTNING_BOLT_THUNDER);
        Utils.sendPopup(
                getPlayer(),
                ChatColor.RED + Language.getRoleString("17"),
                ChatColor.RED + (Language.getRoleString("19"))
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
    public void onMove(PlayerMoveEvent e) {
        if (isDead()) {
            if (e.getTo() != null) {
                if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getY() != e.getTo().getY() || e.getFrom().getZ() != e.getTo().getZ()) {
                    e.setTo(e.getFrom());
                }
            }
        }
    }

    @Override
    public void onSnowball(ProjectileHitEvent e, Role snowballed) {
        if (isDead() && snowballed.isDead()) { // If both of them are dead...
            System.out.println("Phantom");
            if (snowballed instanceof Immune) { // If who was shot is the Immune...
                snowballed.getPlayer().sendMessage(ChatColor.RED + getPlayerName() + " " + Language.getRoleString("136") + ", " + ChatColor.GREEN + Language.getRoleString("1"));
                getPlayer().sendMessage(ChatColor.RED + snowballed.getPlayerName() + " " + Language.getRoleString("2"));
                return;
            }
            snowballed.revive();
            getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("12") + " " + snowballed.getPlayerName() + "!");
            snowballed.getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("13"));
            setActivated(true);
            setPoints(1); // Give Phantom 1 point for healing someone
        }
    }
}
