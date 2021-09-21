package com.alcachofra.roles.good;

import com.alcachofra.utils.Config;
import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import com.alcachofra.roles.bad.Murderer;
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
            Language.getRoleName("magician"),
            Language.getRoleDescription("magician"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        setPoints(2);
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
    public void onInteract(PlayerInteractEvent event, Action action) {
        super.onInteract(event, action);
        if (!isDead()) {
            if (getPlayer().getInventory().getItemInMainHand().getType().equals(Material.RED_TULIP) ||
                    getPlayer().getInventory().getItemInOffHand().getType().equals(Material.RED_TULIP)) {
                if ((action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) && !isActivated()) {
                    Utils.removeItem(getPlayer(), Material.RED_TULIP); // Remove item
                    setActivated(true);

                    Murderer m = Xinada.getGame().getRound().getCurrentRole(Murderer.class);
                    Utils.soundLocation(m.getPlayer().getLocation(), Sound.ENTITY_SHEEP_AMBIENT);
                    m.getPlayer().getInventory().clear();
                    Utils.addItem(m.getPlayer(), Material.RED_TULIP, 0, 1);

                    Utils.messageGlobal(Language.getString("knifeTurnedIntoRose"));

                    new BukkitRunnable() {
                        public void run() {
                            if (!Xinada.inGame() || !Xinada.getGame().inRound()) this.cancel();
                            m.getPlayer().getInventory().clear();
                            m.addSword();
                            this.cancel();
                        }
                    }.runTaskLater(Xinada.getPlugin(), 20*(Config.get(Xinada.GAME).getInt("game.magicianTime"))); // 20 ticks = 1 second
                }
            }
        }
    }
}
