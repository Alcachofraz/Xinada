package com.alcachofra.roles.bad;

import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Traitor extends Role {
    private boolean laughing = false;

    public Traitor(Player player) {
        super(
            player,
            Language.getRolesName("traitor"),
            Language.getRolesDescription("traitor"),
            -1
        );
    }

    public boolean isLaughing() {
        return laughing;
    }

    public void setLaughing(boolean laughing) {
        this.laughing = laughing;
    }

    @Override
    public void initialise() {
        setCanPickUp(false);
        super.initialise();
    }

    @Override
    public void reset() {
        setCanPickUp(false);
        if (isActivated()) {
            addSword();
        }
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (isActivated()) {
            if (!wwh.isDead() && !isDead()) { // If neither of them are dead...
                if (getPlayer().getInventory().getItemInMainHand().getType() == Material.IRON_SWORD) { // If the damage was above 1 heart...
                    if (wwh.getRoleSide() < 0) {
                        getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("27") + " " + wwh.getPlayerName() + ". " + Language.getRoleString("28"));
                        wwh.getPlayer().sendMessage(ChatColor.GREEN + wwh.getPlayerName() + " " + Language.getRoleString("29"));
                    } else {
                        wwh.kill(this.getPlayer()); // Kill player
                        Xinada.getGame().getRound().checkEnd();
                    }
                }
            }
        }
    }
}
