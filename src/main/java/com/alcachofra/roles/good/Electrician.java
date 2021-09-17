package com.alcachofra.roles.good;

import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Electrician extends Role {
    public Electrician(Player player) {
        super(
            player,
            Language.getRolesName("electrician"),
            Language.getRolesDescription("electrician"),
            1
        );
    }

    @Override
    public void initialise() {
        Utils.addItem(getPlayer(), Material.COAL, 8, 1);
        super.initialise();
    }

    @Override
    public void reset() {
        Utils.addItem(getPlayer(), Material.COAL, 8, 1);
        super.reset();
    }

    @Override
    public void onInteract(PlayerInteractEvent e, Action a) {
        if (!isDead()) {
            if(getPlayer().getInventory().getItemInMainHand().getType().equals(Material.COAL) ||
                    getPlayer().getInventory().getItemInOffHand().getType().equals(Material.COAL)) {
                if (a.equals(Action.RIGHT_CLICK_BLOCK) || a.equals(Action.RIGHT_CLICK_AIR)) {
                    if (!isActivated()) {
                        setActivated(true);

                        // Remove coal from Electrician
                        Utils.removeItem(getPlayer(), Material.COAL);

                        getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("80"));
                        Utils.soundIndividual(getPlayer(), Sound.BLOCK_FIRE_EXTINGUISH);

                        int electricianTime = Xinada.getPlugin().getConfig().getInt("game.electricianTime");
                        for (Role role : Xinada.getGame().getRound().getCurrentRoles().values()) {
                            if (!getPlayer().equals(role.getPlayer()) && !(role instanceof Immune)) {
                                role.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 250));

                                role.getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("81") + " " + electricianTime + " " + Language.getRoleString("75"));
                                Utils.soundIndividual(role.getPlayer(), Sound.ITEM_FLINTANDSTEEL_USE);
                            }
                        }
                        new BukkitRunnable() {
                            private long secsRemaining = electricianTime;

                            public void run() {
                                if (!Xinada.inGame() || !Xinada.getGame().inRound()) this.cancel();
                                if (secsRemaining > 0) {
                                    secsRemaining--;
                                }
                                else {
                                    for (Role role : Xinada.getGame().getRound().getCurrentRoles().values()) {
                                        if (!(role instanceof Immune)) {
                                            role.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS); // Player gains the ability to see
                                        }
                                    }
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(Xinada.getPlugin(), 0, 20);
                    }
                }
            }
        }
    }
}
