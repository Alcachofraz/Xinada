package com.alcachofra.roles.good;

import com.alcachofra.utils.Config;
import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Objects;

public class Pirate extends Role {
    protected boolean found_treasure = false; // Has found the other pirate's treasure
    protected Location treasure = null; // Location of treasure

    public Pirate(Player player) {
        super(
            player,
            Language.getRoleName("pirate"),
            Language.getRoleDescription("pirate"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        addPoint(); // Add 1 point
    }

    public boolean foundTreasure() {
        return found_treasure;
    }

    public void setFoundTreasure(boolean found_treasure) {
        this.found_treasure = found_treasure;
    }

    public Location getTreasure() {
        return treasure;
    }

    public void setTreasure(Location treasure) {
        this.treasure = treasure;
    }

    @Override
    public void initialise() {
        Utils.addItem(getPlayer(), Material.GOLD_BLOCK, 8, 1);
        super.initialise();
        countDown();
    }

    @Override
    public void reset() {
        Utils.addItem(getPlayer(), Material.GOLD_BLOCK, 8, 1);
        setFoundTreasure(false);

        // Find the other pirate:
        Pirate other_pirate = null;
        Collection<Role> roles = Xinada.getGame().getRound().getCurrentRoles().values();
        for (Role r : roles) {
            if ((r instanceof Pirate) && (!r.getPlayer().getName().equals(getPlayer().getName()))) {
                other_pirate = (Pirate) r;
            }
        }
        if (other_pirate == null) return; // ERROR Pirate not found

        if (other_pirate.getTreasure() != null) {
            setPoints(0);
        }
        else {
            getPlayer().sendMessage(Language.getString("otherPirateFailed"));
            setPoints(1);
        }
        setActivated(false);
    }

    @Override
    public void clean() {
        if (treasure != null) treasure.getBlock().setType(Material.AIR);
        super.clean();
    }

    public void countDown() {
        new BukkitRunnable() {
            private final long pirateTime = Config.get(Xinada.GAME).getInt("game.pirateTime") + 1;
            private long secsRemaining = pirateTime;

            public void run() {
                if (!Xinada.inGame() || !Xinada.getGame().inRound()) this.cancel();

                if (secsRemaining > 0) {
                    if (secsRemaining < pirateTime) { // Ignore first second (kind of buggy)
                        if (secsRemaining != 1) getPlayer().sendMessage(
                                String.format(
                                        Language.getString("secondsToHideTreasure"),
                                        secsRemaining
                                )
                        );
                        else getPlayer().sendMessage(
                                String.format(
                                        Language.getString("secondToHideTreasure"),
                                        secsRemaining
                                )
                        );
                        Utils.soundLocation(getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS);
                    }
                    secsRemaining--;
                }
                else {
                    if (getTreasure() != null) { // If he has hidden his treasure...
                        getPlayer().sendMessage(Language.getString("treasureHidden"));

                        // Find the other pirate:
                        Pirate other_pirate = null;
                        Collection<Role> roles = Xinada.getGame().getRound().getCurrentRoles().values();
                        for (Role r : roles) {
                            if ((r instanceof Pirate) && (!r.getPlayer().getName().equals(getPlayer().getName()))) {
                                other_pirate = (Pirate) r;
                            }
                        }
                        if (other_pirate == null) return; // ERROR Pirate not found

                        if (other_pirate.getTreasure() != null) {
                            getPlayer().sendMessage(Language.getString("findOtherPirateTreasure"));
                        }
                        else {
                            getPlayer().sendMessage(Language.getString("otherPirateFailed"));
                            setPoints(1);
                        }
                    }
                    else { // If he hasn't hidden his treasure...
                        getPlayer().sendMessage(Language.getString("youAreCursed"));
                        Utils.removeItem(getPlayer(), Material.GOLD_BLOCK);
                        setCursed(true);
                    }
                    setActivated(true);
                    this.cancel();
                }
            }
        }.runTaskTimer(Xinada.getPlugin(), 0, 20);
    }

    @Override
    public void onInteract(PlayerInteractEvent event, Action action) {
        super.onInteract(event, action);
        if (!isDead()) {
            if (action.equals(Action.LEFT_CLICK_BLOCK)) {
                if (!foundTreasure()) {
                    // Find the other pirate:
                    Pirate other_pirate = null;
                    Collection<Role> roles = Xinada.getGame().getRound().getCurrentRoles().values();
                    for (Role r : roles) {
                        if ((r instanceof Pirate) && (!r.getPlayer().getName().equals(getPlayer().getName()))) {
                            other_pirate = (Pirate) r;
                        }
                    }
                    if (other_pirate == null) return;
                    if (other_pirate.getTreasure() == null) return;
                    if (Objects.requireNonNull(event.getClickedBlock()).getLocation().equals(other_pirate.getTreasure())) {
                        setFoundTreasure(true);
                        getPlayer().sendMessage(Language.getString("foundOtherPirateTreasure"));
                        setPoints(1);
                    }
                }
            }
        }
    }

    @Override
    public void onPlaceBlock(BlockPlaceEvent event) {
        if (!isDead()) {
            if (getPlayer().getInventory().getItemInMainHand().getType().equals(Material.GOLD_BLOCK) ||
                    getPlayer().getInventory().getItemInOffHand().getType().equals(Material.GOLD_BLOCK)) {
                setTreasure(event.getBlock().getLocation());
            }
        }
    }

    @Override
    public void kill(Player by) {
        if (isDead()) return;
        if (getTreasure() == null) {
            Utils.removeItem(getPlayer(), Material.GOLD_BLOCK);
        }
        super.kill(by);
    }
}
