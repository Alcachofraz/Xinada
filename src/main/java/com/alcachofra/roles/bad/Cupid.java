package com.alcachofra.roles.bad;

import com.alcachofra.utils.Config;
import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import com.alcachofra.roles.good.Immune;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Cupid extends Role {
    ArrayList<Role> hearts = new ArrayList<>();

    public Cupid(Player player) {
        super(
            player,
            Language.getRoleName("cupid"),
            Language.getRoleDescription("cupid"),
            Side.BAD
        );
    }

    @Override
    public void award() {
        addPoint(); // Add 1 point
    }

    @Override
    public void initialise() {
        setCanPickUp(false);
        super.initialise();
    }

    @Override
    public void reset() {
        setCanPickUp(false);
        for (Role role : hearts) {
            if (role.isInLove()) {
                role.setInLove(false);
                role.getPlayer().sendMessage(Language.getString("noLongerInLove"));
            }
        }
        hearts.clear();
        super.reset();
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!isDead() && !wwh.isDead()) {
            if (wwh instanceof Immune) {
                wwh.getPlayer().sendMessage(String.format(Language.getString("triedStealHeart"), getPlayer().getName()) + ", " + Language.getString("butImmune"));
                getPlayer().sendMessage(String.format(Language.getString("isImmune"), wwh.getPlayer().getName()));
            }
            else if (wwh instanceof Monster) {
                wwh.getPlayer().sendMessage(String.format(Language.getString("triedStealHeart"), getPlayer().getName()) + ", " + Language.getString("butImmortal"));
                getPlayer().sendMessage(String.format(Language.getString("isImmortal"), wwh.getPlayer().getName()));
            }
            else if (hearts.size() == 0) {
                hearts.add(wwh);
                wwh.setInLove(true);

                getPlayer().sendMessage(String.format(Language.getString("youStoleHeart"), wwh.getPlayer().getName()));
                wwh.getPlayer().sendMessage(Language.getString("cupidStoleHeart"));
                Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_CAT_PURREOW);
            }
            else if (hearts.size() == 1) {
                if (hearts.get(0).getPlayer().getName().equals(wwh.getPlayer().getName())) {
                    getPlayer().sendMessage(String.format(Language.getString("alreadyStoleHeart"), wwh.getPlayer().getName()));
                    return;
                }
                hearts.add(wwh);
                wwh.setInLove(true);

                hearts.get(0).getPlayer().sendMessage(String.format(Language.getString("youAreInLove"), hearts.get(1).getPlayer().getName()));
                hearts.get(1).getPlayer().sendMessage(String.format(Language.getString("youAreInLove"), hearts.get(0).getPlayer().getName()));
                getPlayer().sendMessage(
                        String.format(
                                Language.getString("theyAreInLove"),
                                hearts.get(0).getPlayer().getName(),
                                hearts.get(1).getPlayer().getName()
                        )
                );
                Utils.soundLocation(hearts.get(0).getPlayer().getLocation(), Sound.ENTITY_BAT_AMBIENT);

                hearts.get(0).getPlayer().teleport(hearts.get(1).getPlayer());

                for (Role heart : hearts) {
                    heart.getPlayer().setWalkSpeed(0); // Player whom was hit loses ability to walk
                    heart.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 250)); // Player whom was hit loses ability to jump
                    heart.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 250)); // Player whom was hit loses ability to see

                    Utils.sendPopup(
                            heart.getPlayer(),
                            Language.getString("keepCloseToLoved"),
                            Language.getString("crouchToNormal")
                    );
                    heart.setExposed(true);
                }

                int cupidDistance = Config.get(Xinada.GAME).getInt("cupidDistance");
                new BukkitRunnable() {
                    public void run() {
                        if (!Xinada.inGame() || !Xinada.getGame().inRound()) this.cancel();
                        if (hearts.get(0).getPlayer().getLocation().distance(hearts.get(1).getPlayer().getLocation()) > cupidDistance) {
                            for (Role heart : hearts) {
                                heart.kill(getPlayer());
                                heart.getPlayer().sendMessage(Language.getString("tooFarFromLoved"));
                            }
                            getPlayer().sendMessage(
                                    String.format(
                                            Language.getString("couldNotLiveWithout"),
                                            hearts.get(0).getPlayer().getName(),
                                            hearts.get(1).getPlayer().getName()
                                    )
                            );
                            setPoints(1);
                            Xinada.getGame().getRound().checkEnd();
                            this.cancel();
                        }
                        else if (hearts.get(0).isDead() || hearts.get(1).isDead()) {
                            for (Role heart : hearts) {
                                heart.setInLove(false);

                                heart.getPlayer().sendMessage(Language.getString("noLongerInLove"));
                                Utils.soundLocation(heart.getPlayer().getLocation(), Sound.ENTITY_BAT_LOOP);
                            }
                            this.cancel();
                        }
                    }
                }.runTaskTimer(Xinada.getPlugin(), 0, 40);
            }

        }
    }
}
