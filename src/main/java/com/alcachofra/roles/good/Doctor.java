package com.alcachofra.roles.good;

import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Doctor extends Role {
    public Doctor(Player player) {
        super(
            player,
            Language.getRolesName("doctor"),
            Language.getRolesDescription("doctor"),
            1
        );
    }

    @Override
    public void award() {
        addPoint(); // Add 1 point
    }

    @Override
    public void onInteract(PlayerInteractEvent e, Action a) {
        if (e.getClickedBlock() == null) return;

        super.onInteract(e, a);

        Location clicked = e.getClickedBlock().getLocation();
        for (Role role : Xinada.getGame().getRound().getCurrentRoles().values()) {
            System.out.println(clicked);
            System.out.println(role.getPotLocation());
            if (role.isDead() && role.getPotLocation() != null &&
                    role.getPotLocation().getX() == clicked.getX() &&
                    role.getPotLocation().getY() == clicked.getY() &&
                    role.getPotLocation().getZ() == clicked.getZ()) {
                if (role instanceof Immune) {
                    role.getPlayer().sendMessage(ChatColor.RED + getPlayerName() + " " + Language.getRoleString("78") + ", " + Language.getRoleString("1"));
                    getPlayer().sendMessage(ChatColor.RED + role.getPlayerName() + " " + Language.getRoleString("2"));
                }
                else {
                    role.revive();
                    role.getPlayer().sendMessage(ChatColor.GREEN + getPlayerName() + " " + Language.getRoleString("79"));
                    getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("12") + " " + role.getPlayerName() + "!");
                    setPoints(1);
                }
            }
        }
    }
}
