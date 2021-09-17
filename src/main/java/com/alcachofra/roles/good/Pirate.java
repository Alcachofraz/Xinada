package com.alcachofra.roles.good;

import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import org.bukkit.ChatColor;
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
            Language.getRolesName("pirate"),
            Language.getRolesDescription("pirate"),
            1
        );
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
            if ((r instanceof Pirate) && (!r.getPlayerName().equals(getPlayerName()))) {
                other_pirate = (Pirate) r;
            }
        }
        if (other_pirate == null) return; // ERROR Pirate not found

        if (other_pirate.getTreasure() != null) {
            setPoints(0);
        }
        else {
            getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("103") + ChatColor.GREEN + " " + Language.getRoleString("104"));
            setPoints(1);
        }
        setActivated(false);
    }

    @Override
    public void award() {
        addPoint(); // Add 1 point
    }

    @Override
    public void clean() {
        if (treasure != null) treasure.getBlock().setType(Material.AIR);
    }

    public void countDown() {
        new BukkitRunnable() {
            private final long pirateTime = Xinada.getPlugin().getConfig().getInt("game.pirateTime") + 1;
            private long secsRemaining = pirateTime;

            public void run() {
                if (!Xinada.inGame() || !Xinada.getGame().inRound()) this.cancel();

                if (secsRemaining > 0) {
                    if (secsRemaining < pirateTime) { // Ignore first second (kind of buggy)
                        if (secsRemaining != 1) getPlayer().sendMessage(
                            ChatColor.GRAY + Language.getRoleString("105") + " " +
                            ChatColor.GOLD + secsRemaining +
                            ChatColor.GRAY + " " + Language.getRoleString("106")
                        );
                        else getPlayer().sendMessage(
                            ChatColor.GRAY + Language.getRoleString("105") + " " +
                            ChatColor.GOLD + secsRemaining +
                            ChatColor.GRAY + " " + Language.getRoleString("107")
                        );
                        Utils.soundLocation(getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS);
                    }
                    secsRemaining--;
                }
                else {
                    if (getTreasure() != null) { // If he has hidden his treasure...
                        getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("108"));

                        // Find the other pirate:
                        Pirate other_pirate = null;
                        Collection<Role> roles = Xinada.getGame().getRound().getCurrentRoles().values();
                        for (Role r : roles) {
                            if ((r instanceof Pirate) && (!r.getPlayerName().equals(getPlayerName()))) {
                                other_pirate = (Pirate) r;
                            }
                        }
                        if (other_pirate == null) return; // ERROR Pirate not found

                        if (other_pirate.getTreasure() != null) {
                            getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("109"));
                        }
                        else {
                            getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("103") + ChatColor.GREEN + " " + Language.getRoleString("104"));
                            setPoints(1);
                        }
                    }
                    else { // If he hasn't hidden his treasure...
                        getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("110"));
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
    public void onInteract(PlayerInteractEvent e, Action a) {
        if (!isDead()) {
            if (a.equals(Action.LEFT_CLICK_BLOCK)) {
                if (!foundTreasure()) {
                    // Find the other pirate:
                    Pirate other_pirate = null;
                    Collection<Role> roles = Xinada.getGame().getRound().getCurrentRoles().values();
                    for (Role r : roles) {
                        if ((r instanceof Pirate) && (!r.getPlayerName().equals(getPlayerName()))) {
                            other_pirate = (Pirate) r;
                        }
                    }
                    if (other_pirate == null) return;
                    if (other_pirate.getTreasure() == null) return;
                    if (Objects.requireNonNull(e.getClickedBlock()).getLocation().equals(other_pirate.getTreasure())) {
                        setFoundTreasure(true);
                        getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("111"));
                        setPoints(1);
                    }
                }
            }
        }
    }

    @Override
    public void onPlaceBlock(BlockPlaceEvent e) {
        if (!isDead()) {
            if (getPlayer().getInventory().getItemInMainHand().getType().equals(Material.GOLD_BLOCK) ||
                    getPlayer().getInventory().getItemInOffHand().getType().equals(Material.GOLD_BLOCK)) {
                setTreasure(e.getBlock().getLocation());
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
