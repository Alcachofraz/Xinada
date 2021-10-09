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

import java.util.Collection;
import java.util.Objects;

public class Illusionist extends Role {
    public Illusionist(Player player) {
        super(
            player,
            Language.getRoleName("illusionist"),
            Language.getRoleDescription("illusionist"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        setPoints(2);
    }

    @Override
    public void initialise() {
        Utils.addItem(getPlayer(), Material.SUGAR, 8, 1);
        super.initialise();
    }

    @Override
    public void reset() {
        Utils.addItem(getPlayer(), Material.SUGAR, 8, 1);
        super.reset();
    }

    @Override
    public void onInteract(PlayerInteractEvent event, Action action) {
        super.onInteract(event, action);
        if (!isDead()) {
            if (getPlayer().getInventory().getItemInMainHand().getType().equals(Material.SUGAR) ||
                    getPlayer().getInventory().getItemInOffHand().getType().equals(Material.SUGAR)) {
                if (action.equals(Action.RIGHT_CLICK_BLOCK) && !isActivated()) {
                    getPlayer().sendMessage(Language.getString("usedSmokeBomb"));
                    Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_BAT_TAKEOFF);

                    Utils.removeItem(getPlayer(), Material.SUGAR); // Subtract item in hand

                    int radius = Config.get(Xinada.GAME).getInt("illusionistRange");
                    Collection<Role> roles = Xinada.getGame().getRound().getCurrentRoles().values();
                    for (Role r : roles) {
                        if ((r.getPlayer().getLocation().distance(Objects.requireNonNull(event.getClickedBlock()).getLocation()) <= radius) && !r.getPlayer().getName().equals(getPlayer().getName())) {
                            if (r instanceof Immune) {
                                r.getPlayer().sendMessage(String.format(Language.getString("triedToSmokeBombYou"), getPlayer().getName()) + ", " + Language.getString("butImmune"));
                                continue;
                            }
                            r.getPlayer().setWalkSpeed(0.02f); // Player starts to walk slowly
                            r.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 250)); // Player whom was hit loses ability to jump
                            r.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1)); // Player whom was hit loses ability to see
                            r.getPlayer().sendMessage(Language.getString("caughtInSmokeBomb"));

                            new BukkitRunnable() {
                                private long secsRemaining = Config.get(Xinada.GAME).getInt("illusionistTime");

                                public void run() {
                                    if (!Xinada.inGame() || !Xinada.getGame().inRound()) this.cancel();

                                    if (secsRemaining > 0) {
                                        secsRemaining--;
                                    } else {
                                        if (!isDead()) {
                                            r.getPlayer().setWalkSpeed(0.2f); // Player can walk again
                                            r.getPlayer().removePotionEffect(PotionEffectType.JUMP); // Player gains the ability to jump
                                            r.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS); // Player gains the ability to see
                                        }
                                        this.cancel();
                                    }
                                }
                            }.runTaskTimer(Xinada.getPlugin(), 0, 20); // 20 ticks = 1 second
                        }
                    }
                    setActivated(true);
                }
            }
        }
    }
}
