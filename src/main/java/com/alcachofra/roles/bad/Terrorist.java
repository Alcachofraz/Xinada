package com.alcachofra.roles.bad;

import com.alcachofra.roles.good.Negotiator;
import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import com.alcachofra.bomb.Puzzle;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class Terrorist extends Role {
    private Location bombLocation = null;
    private boolean exploded = false;
    private Puzzle puzzle = null;

    public Terrorist(Player player) {
        super(
            player,
            Language.getRolesName("terrorist"),
            Language.getRolesDescription("terrorist"),
            -1
        );
    }

    public Location getBombLocation() {
        return bombLocation;
    }

    public void setBombLocation(Location bombLocation) {
        this.bombLocation = bombLocation;
    }

    public boolean exploded() {
        return exploded;
    }

    public void setExploded(boolean exploded) {
        this.exploded = exploded;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public void explode() {
        Utils.soundGlobal(Sound.ENTITY_DRAGON_FIREBALL_EXPLODE);
        Utils.messageGlobal(ChatColor.RED + Language.getRoleString("67"));
        Xinada.getGame().getRound().getCurrentRoles().forEach((player, role) -> {
            if (!(role instanceof Terrorist)) role.kill(getPlayer());
        });
        setExploded(true);
        addPoint();
        Xinada.getGame().getRound().checkEnd();
    }

    @Override
    public void initialise() {
        Utils.addItem(getPlayer(), Material.TNT, 8, 1);
        super.initialise();
    }

    @Override
    public void reset() {
        Utils.addItem(getPlayer(), Material.TNT, 8, 1);
        getPuzzle().setSolved(true);
        setPuzzle(null);

        clean();
        setBombLocation(null);

        setExploded(false);
        super.reset();
    }

    @Override
    public void award() {
        addPoint();
    }

    @Override
    public void clean() {
        if (getBombLocation() != null) getBombLocation().getBlock().setType(Material.AIR);
    }

    @Override
    public void onPlaceBlock(BlockPlaceEvent e) {
        if (!isDead()) { // If dead
            setActivated(true);
            if (getPlayer().getInventory().getItemInMainHand().getType().equals(Material.TNT) ||
                    getPlayer().getInventory().getItemInOffHand().getType().equals(Material.TNT)) {
                Utils.messageGlobal(ChatColor.RED + Language.getRoleString("68"));
                Utils.sendPopupGlobal("", ChatColor.RED + Language.getRoleString("69"));
                Utils.soundGlobal(Sound.ITEM_FLINTANDSTEEL_USE);

                setBombLocation(e.getBlock().getLocation());
                puzzle = new Puzzle(Language.getRoleString("70"), Xinada.getPlugin().getConfig().getInt("game.terroristRows"));
                puzzle.start();
                getPlayer().openInventory(puzzle.getInventory());
            }
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (!isDead()) {
            if (puzzle != null) { // If a Puzzle's been created
                if (Objects.equals(e.getClickedInventory(), puzzle.getInventory())) { // If it's the puzzle's inventory
                    final ItemStack item = e.getCurrentItem(); // Item clicked

                    if (item != null) {
                        item.setType(
                                item.getType() == Material.RED_STAINED_GLASS_PANE ?
                                        Material.LIME_STAINED_GLASS_PANE :
                                        Material.RED_STAINED_GLASS_PANE);

                        if (puzzle.isSolved()) {
                            puzzle.stop();
                            getPlayer().closeInventory();
                            explode();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onInteract(PlayerInteractEvent e, Action a) {
        if (!isDead()) {
            if (a.equals(Action.LEFT_CLICK_BLOCK)) {
                if (bombLocation != null && e.getClickedBlock() != null) {
                    if (bombLocation.equals(e.getClickedBlock().getLocation())) {
                        getPlayer().openInventory(puzzle.getInventory());
                    }
                }
            }
        }
    }

    @Override
    public void kill(Player by) {
        puzzle.stop();
        super.kill(by);
    }
}
