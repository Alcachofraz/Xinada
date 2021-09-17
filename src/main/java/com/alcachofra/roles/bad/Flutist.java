package com.alcachofra.roles.bad;

import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import com.alcachofra.roles.good.Immune;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class Flutist extends Role {
    ArrayList<Role> attracted = new ArrayList<>();

    public Flutist(Player player) {
        super(
            player,
            Language.getRolesName("flutist"),
            Language.getRolesDescription("flutist"),
            -1
        );
    }

    @Override
    public void initialise() {
        setCanPickUp(false);
        Utils.addItem(getPlayer(), Material.STICK, 8, 1);
        super.initialise();
    }

    @Override
    public void reset() {
        setCanPickUp(false);
        for (Role a : attracted) {
            a.getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("52"));
        }
        attracted.clear();
        Utils.addItem(getPlayer(), Material.STICK, 8, 1);
        super.reset();
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!isDead() && !wwh.isDead()) {
            if (wwh instanceof Immune) {
                getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("2"));
                return;
            }
            for (Role a : attracted) { // Verify if player is not already selected
                if (wwh.getPlayerName().equals(a.getPlayerName())) {
                    return;
                }
            }
            attracted.add(wwh);
            getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("53"));
        }
    }

    @Override
    public void onInteract(PlayerInteractEvent e, Action a) {
        if (!isDead()) {
            if(getPlayer().getInventory().getItemInMainHand().getType().equals(Material.STICK) ||
                    getPlayer().getInventory().getItemInOffHand().getType().equals(Material.STICK)) {
                if (a.equals(Action.RIGHT_CLICK_BLOCK) || a.equals(Action.RIGHT_CLICK_AIR)) {
                    if (!isActivated()) {
                        if (attracted.size() == 0) {
                            getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("54"));
                            return;
                        }
                        for (Role role : attracted) {
                            if (!role.isDead()) {
                                role.getPlayer().teleport(getPlayer().getLocation());
                                role.getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("55"));
                                Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT);
                            }
                        }
                        setActivated(true);

                        // Remove flute from Flutist
                        Utils.removeItem(getPlayer(),Material.STICK);

                        getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("56"));
                    }
                }
            }
        }
    }
}
