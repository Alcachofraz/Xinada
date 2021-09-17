package com.alcachofra.roles.bad;

import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import com.alcachofra.roles.good.Immune;
import org.bukkit.ChatColor;
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
            Language.getRolesName("cupid"),
            Language.getRolesDescription("cupid"),
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
        for (Role role : hearts) {
            if (role.isInLove()) {
                role.setInLove(false);
                role.getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("32"));
            }
        }
        hearts.clear();
        super.reset();
    }

    @Override
    public void award() {
        addPoint(); // Add 1 point
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!isDead() && !wwh.isDead()) {
            if (wwh instanceof Immune) {
                wwh.getPlayer().sendMessage(ChatColor.RED + getPlayerName() + " " + Language.getRoleString("11") + ", " + ChatColor.GREEN + Language.getRoleString("1"));
                getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("2"));
                return;
            }
            if (wwh instanceof Monster) {
                wwh.getPlayer().sendMessage(ChatColor.RED + getPlayerName() + " " + Language.getRoleString("11") + ", " + ChatColor.GREEN + Language.getRoleString("3"));
                getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("4"));
                return;
            }
            if (hearts.size() == 0) {
                hearts.add(wwh);
                wwh.setInLove(true);

                getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("33") + " " + wwh.getPlayerName() + Language.getRoleString("34"));
                wwh.getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("35"));
                Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_CAT_PURREOW);
            }
            else if (hearts.size() == 1) {
                if (hearts.get(0).getPlayerName().equals(wwh.getPlayerName())) {
                    getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("36") + " " + wwh.getPlayerName() + Language.getRoleString("34"));
                    return;
                }
                hearts.add(wwh);
                wwh.setInLove(true);

                hearts.get(0).getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("37") + " " + hearts.get(1).getPlayerName() + " " + Language.getRoleString("38"));
                hearts.get(1).getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("37") + " " + hearts.get(0).getPlayerName() + " " + Language.getRoleString("38"));
                getPlayer().sendMessage(ChatColor.GREEN + hearts.get(0).getPlayerName() + " " + Language.getRoleString("39") + " " + hearts.get(1).getPlayerName() + " " + Language.getRoleString("38"));
                Utils.soundLocation(hearts.get(0).getPlayer().getLocation(), Sound.ENTITY_BAT_AMBIENT);

                hearts.get(0).getPlayer().teleport(hearts.get(1).getPlayer());

                for (Role heart : hearts) {
                    heart.getPlayer().setWalkSpeed(0); // Player whom was hit loses ability to walk
                    heart.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 250)); // Player whom was hit loses ability to jump
                    heart.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 250)); // Player whom was hit loses ability to see

                    Utils.sendPopup(
                            heart.getPlayer(),
                            ChatColor.RED + Language.getRoleString("40"),
                            ChatColor.RED + Language.getRoleString("41")
                    );
                    heart.setExposed(true);
                }

                int cupidDistance = Xinada.getPlugin().getConfig().getInt("game.cupidDistance");
                new BukkitRunnable() {
                    public void run() {
                        if (!Xinada.inGame() || !Xinada.getGame().inRound()) this.cancel();
                        if (hearts.get(0).getPlayer().getLocation().distance(hearts.get(1).getPlayer().getLocation()) > cupidDistance) {
                            for (Role heart : hearts) {
                                heart.kill(getPlayer());
                                heart.getPlayer().sendMessage(
                                        ChatColor.RED + Language.getRoleString("42")
                                );
                            }
                            getPlayer().sendMessage(
                                ChatColor.GREEN + hearts.get(0).getPlayerName() + " " + Language.getRoleString("39") + " " + hearts.get(0).getPlayerName() + " " + Language.getRoleString("43")
                            );
                            setPoints(1);
                            Xinada.getGame().getRound().checkEnd();
                            this.cancel();
                        }
                        else if (hearts.get(0).isDead() || hearts.get(1).isDead()) {
                            for (Role heart : hearts) {
                                heart.setInLove(false);

                                heart.getPlayer().sendMessage(
                                    ChatColor.GREEN + Language.getRoleString("32")
                                );
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
