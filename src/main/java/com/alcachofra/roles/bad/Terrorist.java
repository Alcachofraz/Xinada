package com.alcachofra.roles.bad;

import com.alcachofra.utils.Config;
import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import com.alcachofra.bomb.Puzzle;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
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
            Language.getRoleName("terrorist"),
            Language.getRoleDescription("terrorist"),
            Side.BAD
        );
    }

    @Override
    public void award() {}

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
        Utils.messageGlobal(Language.getString("bombExploded"));
        Xinada.getGame().getRound().getCurrentRoles().forEach((player, role) -> {
            if (!(role instanceof Terrorist)) role.kill(getPlayer());
        });
        setExploded(true);
        setPoints(2);
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
    public void clean() {
        if (getBombLocation() != null) getBombLocation().getBlock().setType(Material.AIR);
    }

    @Override
    public void onPlaceBlock(BlockPlaceEvent event) {
        if (!isDead()) { // If dead
            setActivated(true);
            if (getPlayer().getInventory().getItemInMainHand().getType().equals(Material.TNT) ||
                    getPlayer().getInventory().getItemInOffHand().getType().equals(Material.TNT)) {
                Utils.messageGlobal(Language.getString("disarmBomb"));
                Utils.sendPopupGlobal("", Language.getString("disarmBombPopup"));
                Utils.soundGlobal(Sound.ITEM_FLINTANDSTEEL_USE);

                setBombLocation(event.getBlock().getLocation());
                puzzle = new Puzzle(Language.getString("activateFuses"), Config.get(Xinada.GAME).getInt("game.terroristRows"));
                puzzle.start();
                getPlayer().openInventory(puzzle.getInventory());
            }
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (!isDead()) {
            if (puzzle != null) { // If a Puzzle's been created
                if (Objects.equals(event.getClickedInventory(), puzzle.getInventory())) { // If it's the puzzle's inventory
                    final ItemStack item = event.getCurrentItem(); // Item clicked

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
    public void onInteract(PlayerInteractEvent event, Action action) {
        super.onInteract(event, action);
        if (!isDead()) {
            if (action.equals(Action.LEFT_CLICK_BLOCK)) {
                if (bombLocation != null && event.getClickedBlock() != null) {
                    if (bombLocation.equals(event.getClickedBlock().getLocation())) {
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
