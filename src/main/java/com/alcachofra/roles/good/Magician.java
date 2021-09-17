package com.alcachofra.roles.good;

import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import com.alcachofra.roles.bad.Murderer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Magician extends Role {
    public Magician(Player player) {
        super(
            player,
            Language.getRolesName("magician"),
            Language.getRolesDescription("magician"),
            1
        );
    }

    @Override
    public void initialise() {
        Utils.addItem(getPlayer(), Material.RED_TULIP, 8, 1);
        super.initialise();
    }

    @Override
    public void reset() {
        Utils.addItem(getPlayer(), Material.RED_TULIP, 8, 1);
        super.reset();
    }

    @Override
    public void onInteract(PlayerInteractEvent e, Action a) {
        if (!isDead()) {
            if (getPlayer().getInventory().getItemInMainHand().getType().equals(Material.RED_TULIP) ||
                    getPlayer().getInventory().getItemInOffHand().getType().equals(Material.RED_TULIP)) {
                if ((a.equals(Action.RIGHT_CLICK_BLOCK) || a.equals(Action.RIGHT_CLICK_AIR)) && !isActivated()) {
                    Utils.removeItem(getPlayer(), Material.RED_TULIP); // Remove item
                    setActivated(true);

                    Murderer m = Xinada.getGame().getRound().getCurrentRole(Murderer.class);
                    Utils.soundLocation(m.getPlayer().getLocation(), Sound.ENTITY_SHEEP_AMBIENT);
                    m.getPlayer().getInventory().clear();
                    Utils.addItem(m.getPlayer(), Material.RED_TULIP, 0, 1);

                    Utils.messageGlobal(ChatColor.GREEN + Language.getRoleString("96"));

                    new BukkitRunnable() {
                        public void run() {
                            if (!Xinada.inGame() || !Xinada.getGame().inRound()) this.cancel();
                            m.getPlayer().getInventory().clear();
                            m.addSword();
                            this.cancel();
                        }
                    }.runTaskLater(Xinada.getPlugin(), 20*(Xinada.getPlugin().getConfig().getInt("game.magicianTime"))); // 20 ticks = 1 second
                }
            }
        }
    }
}
