package com.alcachofra.roles.bad;

import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Trickster extends Role {
    public Trickster(Player player) {
        super(
            player,
            Language.getRoleName("trickster"),
            Language.getRoleDescription("trickster"),
            Side.BAD
        );
    }

    @Override
    public void award() {
        setPoints(2);
    }

    @Override
    public void initialise() {
        Utils.addItem(getPlayer(), Material.SKELETON_SKULL, 8, 1);
        super.initialise();
    }

    @Override
    public void reset() {
        Utils.addItem(getPlayer(), Material.SKELETON_SKULL, 8, 1);
        super.reset();
    }

    @Override
    public void onInteract(PlayerInteractEvent event, Action action) {
        super.onInteract(event, action);
        if (!isDead()) {
            if (getPlayer().getInventory().getItemInMainHand().getType().equals(Material.SKELETON_SKULL) ||
                    getPlayer().getInventory().getItemInOffHand().getType().equals(Material.SKELETON_SKULL)) {
                if ((action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) && !isActivated()) {
                    Utils.removeItem(getPlayer(), Material.SKELETON_SKULL); // Remove item
                    setActivated(true);
                    Utils.messageGlobal(String.format(Language.getString("died"), getPlayer().getName()));
                    Utils.soundGlobal(Sound.ENTITY_LIGHTNING_BOLT_THUNDER);
                }
            }
        }
    }
}
