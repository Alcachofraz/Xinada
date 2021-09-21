package com.alcachofra.roles.good;

import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Doctor extends Role {
    public Doctor(Player player) {
        super(
            player,
            Language.getRoleName("doctor"),
            Language.getRoleDescription("doctor"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        addPoint(); // Add 1 point
    }

    @Override
    public void onInteract(PlayerInteractEvent event, Action action) {
        if (event.getClickedBlock() == null) return;

        super.onInteract(event, action);

        Location clicked = event.getClickedBlock().getLocation();
        for (Role role : Xinada.getGame().getRound().getCurrentRoles().values()) {
            if (role.isDead() && role.getPotLocation() != null &&
                    role.getPotLocation().getX() == clicked.getX() &&
                    role.getPotLocation().getY() == clicked.getY() &&
                    role.getPotLocation().getZ() == clicked.getZ()) {
                if (role instanceof Immune) {
                    role.getPlayer().sendMessage(String.format(Language.getString("triedToRevive"), getPlayer().getName()) + ", " + Language.getString("butImmune"));
                    getPlayer().sendMessage(String.format(Language.getString("isImmune"), role.getPlayer().getName()));
                }
                else {
                    role.revive();
                    role.getPlayer().sendMessage(String.format(Language.getString("revivedYou"), getPlayer().getName()));
                    getPlayer().sendMessage(String.format(Language.getString("youRevived"), role.getPlayer().getName()));
                    setPoints(1);
                }
            }
        }
    }
}
