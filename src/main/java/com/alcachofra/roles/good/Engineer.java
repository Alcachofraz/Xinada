package com.alcachofra.roles.good;

import com.alcachofra.roles.bad.Accomplice;
import com.alcachofra.roles.bad.Murderer;
import com.alcachofra.roles.bad.Traitor;
import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class Engineer extends Role {
    public Engineer(Player player) {
        super(
            player,
            Language.getRoleName("engineer"),
            Language.getRoleDescription("engineer"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        setPoints(2);
    }

    @Override
    public void initialise() {
        Utils.addItem(getPlayer(), Material.COMPASS, 8, 1);
        super.initialise();
    }

    @Override
    public void reset() {
        Utils.addItem(getPlayer(), Material.COMPASS, 8, 1);
        super.reset();
    }

    @Override
    public void onInteract(PlayerInteractEvent event, Action action) {
        super.onInteract(event, action);
        if (!isDead()) {
            if (getPlayer().getInventory().getItemInMainHand().getType().equals(Material.COMPASS) ||
                    getPlayer().getInventory().getItemInOffHand().getType().equals(Material.COMPASS)) {
                if ((action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) && event.getHand() == EquipmentSlot.HAND) {
                    getPlayer().sendMessage(
                        String.format(
                                Language.getString("assassinCloseBy"),
                                (int) findNearestAssassin().getPlayer().getLocation().distance(getPlayer().getLocation())
                        )
                    );
                    Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG);
                }
            }
        }
    }

    /**
     * Get nearest assassin (i. e. all bad Roles that can kill), relative to the Player playing this Role.
     * @return The nearest Assassin Role.
     */
    public Role findNearestAssassin() {
        Role nearest = Xinada.getGame().getRound().getCurrentRole(Murderer.class);
        Role aux;
        if ((aux = Xinada.getGame().getRound().getCurrentRole(Accomplice.class)) != null) {
            if (getPlayer().getLocation().distance(aux.getPlayer().getLocation())
                    <
                    getPlayer().getLocation().distance(nearest.getPlayer().getLocation())
            ) nearest = aux;
        }
        if ((aux = Xinada.getGame().getRound().getCurrentRole(Traitor.class)) != null) {
            if (getPlayer().getLocation().distance(aux.getPlayer().getLocation())
                    <
                    getPlayer().getLocation().distance(nearest.getPlayer().getLocation())
            ) nearest = aux;
        }
        return nearest;
    }
}
