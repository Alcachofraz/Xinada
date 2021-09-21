package com.alcachofra.roles.good;

import com.alcachofra.utils.Config;
import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import com.alcachofra.roles.bad.Accomplice;
import com.alcachofra.roles.bad.Murderer;
import com.alcachofra.roles.bad.Traitor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Clown extends Role {
    public Clown(Player player) {
        super(
            player,
            Language.getRoleName("clown"),
            Language.getRoleDescription("clown"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        setPoints(2);
    }

    @Override
    public void initialise() {
        Utils.addItem(getPlayer(), Material.PAPER, 8, 1);
        super.initialise();
    }

    @Override
    public void reset() {
        Utils.addItem(getPlayer(), Material.PAPER, 8, 1);
        super.reset();
    }

    @Override
    public void onInteract(PlayerInteractEvent event, Action action) {
        super.onInteract(event, action);
        if (!isDead()) {
            if (getPlayer().getInventory().getItemInMainHand().getType().equals(Material.PAPER) ||
                    getPlayer().getInventory().getItemInOffHand().getType().equals(Material.PAPER)) {
                if ((action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) && !isActivated()) {
                    String joke = Language.getRandomJoke();

                    if (joke == null) return;

                    getPlayer().chat(joke);
                    Utils.removeItem(getPlayer(), Material.PAPER); // Remove item
                    setActivated(true);
                    Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_PIG_AMBIENT);

                    new BukkitRunnable() { // Drop knife in
                        public void run() {
                            if (!Xinada.inGame() || !Xinada.getGame().inRound()) this.cancel();
                            Utils.messageGlobal(Language.getString("badGuysLaughing"));

                            // Murderer:
                            Murderer murderer = Xinada.getGame().getRound().getCurrentRole(Murderer.class); // Get Murderer
                            murderer.setLaughing(true);
                            if (murderer.isActivated()) { // See if it's activated
                                murderer.dropSword();
                                murderer.setActivated(false);
                                murderer.getPlayer().sendMessage(Language.getString("quicklyPickUpKnife"));
                                Xinada.getGame().getRound().addSwordsDropped();
                            }

                            // Accomplice:
                            Accomplice accomplice = Xinada.getGame().getRound().getCurrentRole(Accomplice.class); // Get Accomplice
                            if (accomplice != null) {
                                accomplice.setLaughing(true);
                                if (accomplice.isActivated()) { // See if there's an Accomplice and if it's activated
                                    murderer.dropSword();
                                    murderer.setActivated(false);
                                    murderer.getPlayer().sendMessage(Language.getString("quicklyPickUpKnife"));
                                    Xinada.getGame().getRound().addSwordsDropped();
                                }
                            }

                            // Traitor
                            Traitor traitor = Xinada.getGame().getRound().getCurrentRole(Traitor.class); // Get Accomplice
                            if (traitor != null) {
                                traitor.setLaughing(true);
                                if (traitor.isActivated()) { // See if there's an Accomplice and if it's activated
                                    traitor.dropSword();
                                    traitor.setActivated(false);
                                    traitor.getPlayer().sendMessage(Language.getString("quicklyPickUpKnife"));
                                    Xinada.getGame().getRound().addSwordsDropped();
                                }
                            }

                            new BukkitRunnable() { // Can pick knife back up in
                                public void run() {
                                    if (!Xinada.inGame() || !Xinada.getGame().inRound()) this.cancel();
                                    murderer.setLaughing(false);
                                    if (accomplice != null) accomplice.setLaughing(false);
                                    if (traitor != null) traitor.setLaughing(false);
                                    this.cancel();
                                }
                            }.runTaskLater(Xinada.getPlugin(), 20*(Config.get(Xinada.GAME).getInt("game.clownTime"))); // 20 ticks = 1 second
                            this.cancel();
                        }
                    }.runTaskLater(Xinada.getPlugin(), 20); // 20 ticks = 1 second
                }
            }
        }
    }
}
