package com.alcachofra.roles.bad;

import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import com.alcachofra.roles.good.Immune;
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
            Language.getRoleName("flutist"),
            Language.getRoleDescription("flutist"),
            Side.BAD
        );
    }

    @Override
    public void award() {
        setPoints(2);
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
            a.getPlayer().sendMessage(Language.getString("outOfTune"));
        }
        attracted.clear();
        Utils.addItem(getPlayer(), Material.STICK, 8, 1);
        super.reset();
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!isDead() && !wwh.isDead()) {
            if (wwh instanceof Immune) {
                wwh.getPlayer().sendMessage(String.format(Language.getString("triedToEnchant"), getPlayer().getName()) + ", " + Language.getString("butImmune"));
                getPlayer().sendMessage(String.format(Language.getString("isImmune"), wwh.getPlayer().getName()));
                return;
            }
            for (Role a : attracted) { // Verify if player is not already selected
                if (wwh.getPlayer().getName().equals(a.getPlayer().getName())) {
                    return;
                }
            }
            attracted.add(wwh);
            getPlayer().sendMessage(String.format(Language.getString("enchanted"), wwh.getPlayer().getName()));
            wwh.getPlayer().sendMessage(Language.getString("enchantedByFlutist"));
        }
    }

    @Override
    public void onInteract(PlayerInteractEvent event, Action action) {
        super.onInteract(event, action);
        if (!isDead()) {
            if(getPlayer().getInventory().getItemInMainHand().getType().equals(Material.STICK) ||
                    getPlayer().getInventory().getItemInOffHand().getType().equals(Material.STICK)) {
                if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) {
                    if (!isActivated()) {
                        if (attracted.size() == 0) {
                            getPlayer().sendMessage(Language.getString("atLeastEnchantOne"));
                            return;
                        }
                        for (Role role : attracted) {
                            if (!role.isDead()) {
                                role.getPlayer().teleport(getPlayer().getLocation());
                                role.getPlayer().sendMessage(Language.getString("summonedByFlutist"));
                                Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT);
                            }
                        }
                        setActivated(true);

                        // Remove flute from Flutist
                        Utils.removeItem(getPlayer(),Material.STICK);

                        getPlayer().sendMessage(Language.getString("youSummonedEnchanted"));
                    }
                }
            }
        }
    }
}
