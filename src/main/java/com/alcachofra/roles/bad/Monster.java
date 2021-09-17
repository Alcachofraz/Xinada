package com.alcachofra.roles.bad;

import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Monster extends Role {
    public Monster(Player player) {
        super(
            player,
            Language.getRolesName("monster"),
            Language.getRolesDescription("monster"),
            -1
        );
    }

    @Override
    public void initialise() {
        setCanPickUp(false);
        super.initialise();
    }

    @Override
    public void reset() {
        setCanPickUp(false);
        super.reset();
    }

    @Override
    public void onShot(EntityDamageByEntityEvent e, Role ws) {
        if (!getPlayer().isDead() && !ws.getPlayer().isDead()) { // If neither of them are dead...
            if (!getPlayer().equals(ws.getPlayer())) {
                // Kill who shot:
                ws.kill(ws.getPlayer());

                // Monster scores 2 points
                setPoints(2);

                ws.getPlayer().sendMessage(ChatColor.RED + getPlayerName() + " " + Language.getRoleString("57"));
                getPlayer().sendMessage(ChatColor.RED + ws.getPlayerName() + " " + Language.getRoleString("58") + ", " + ChatColor.GREEN + Language.getRoleString("3"));
                Xinada.getGame().getRound().checkEnd();
            }
            else ws.getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("9") + " :)");
        }
    }
}
