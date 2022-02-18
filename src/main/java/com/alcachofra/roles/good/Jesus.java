package com.alcachofra.roles.good;

import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import com.alcachofra.utils.Language;
import com.alcachofra.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Jesus extends Role {

    private Location candle;

    public Jesus(Player player) {
        super(
            player,
            Language.getRoleName("jesus"),
            Language.getRoleDescription("jesus"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        setPoints(2);
    }

    @Override
    public void initialise() {
        setCanPickUp(false);
        Utils.addItem(getPlayer(), Material.CANDLE, 8, 1);
        Utils.addItem(getPlayer(), Material.FLINT_AND_STEEL, 7, 1);
        super.initialise();
    }

    @Override
    public void reset() {
        candle = null;
        setCanPickUp(false);
        Utils.addItem(getPlayer(), Material.CANDLE, 8, 1);
        Utils.addItem(getPlayer(), Material.FLINT_AND_STEEL, 7, 1);
        super.reset();
    }

    @Override
    public void clean() {
        if (candle != null) candle.getBlock().setType(Material.AIR);
        super.clean();
    }

    @Override
    public void onInteract(PlayerInteractEvent event, Action action) {
        super.onInteract(event, action);
        if (!isActivated() && event.getClickedBlock() != null) {
            if (event.getClickedBlock().getType() == Material.CANDLE) {
                if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.FLINT_AND_STEEL ||
                        event.getPlayer().getInventory().getItemInOffHand().getType() == Material.FLINT_AND_STEEL) {
                    setActivated(true);
                    Utils.removeItem(getPlayer(), Material.CANDLE);
                    Utils.removeItem(getPlayer(), Material.FLINT_AND_STEEL);
                    Xinada.getGame().getRound().getCurrentRoles().forEach((p, r) -> {
                        if (r.getRoleSide() == Side.BAD) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 3));
                            p.sendMessage(Language.getString("christStoleYourLeg"));
                            Utils.sendPopup(p, "", Language.getString("typeAmen"));
                        }
                    });
                    getPlayer().sendMessage(Language.getString("youStoleLegs"));
                }
            }
        }
    }

    @Override
    public void onPlaceBlock(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.CANDLE) {
            candle = event.getBlock().getLocation();
        }
        else event.setCancelled(true);
    }
}
