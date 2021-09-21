package com.alcachofra.roles.good;

import com.alcachofra.utils.Config;
import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
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
            Language.getRoleName("electrician"),
            Language.getRoleDescription("electrician"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        setPoints(2);
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
    public void onInteract(PlayerInteractEvent event, Action action) {
        super.onInteract(event, action);
        if (!isDead()) {
            if(getPlayer().getInventory().getItemInMainHand().getType().equals(Material.COAL) ||
                    getPlayer().getInventory().getItemInOffHand().getType().equals(Material.COAL)) {
                if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) {
                    if (!isActivated()) {
                        setActivated(true);

                        // Remove coal from Electrician
                        Utils.removeItem(getPlayer(), Material.COAL);

                        getPlayer().sendMessage(Language.getString("youCutPower"));
                        Utils.soundIndividual(getPlayer(), Sound.BLOCK_FIRE_EXTINGUISH);

                        int electricianTime = Config.get(Xinada.GAME).getInt("game.electricianTime");
                        for (Role role : Xinada.getGame().getRound().getCurrentRoles().values()) {
                            if (!getPlayer().equals(role.getPlayer()) && !(role instanceof Immune)) {
                                role.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 250));

                                role.getPlayer().sendMessage(String.format(Language.getString("electricianCutPower"), electricianTime));
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
