package com.alcachofra.main;

import com.alcachofra.roles.bad.*;
import com.alcachofra.utils.Language;
import com.alcachofra.utils.Utils;
import com.alcachofra.roles.good.*;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.text.Collator;

public abstract class Role {

    private Player player;

    protected String name;
    protected String description;
    protected Side side;
    protected ChatColor color;

    private int points = 0;
    private boolean dead = false;
    private boolean canPickUp = true; // Can pick up items
    private boolean cursed = false; // Cursed by the Witch
    private boolean blessed = false; // Blessed by the Priest
    private boolean exposed = false; // Exposed by the Guru
    private boolean merged = false; // Merged by the Devil
    private boolean partner = false; // Partner with Negotiator
    private boolean inLove = false; // Set in love by the Cupid
    private boolean hasBow = false;
    private Player killer;
    private boolean activated = false; // Has used ability

    private Location deathLocation;
    private Location potLocation;

    public enum Side {
        GOOD,
        BAD,
        NEUTRAL
    }


    /**
     * Constructor for Role. A Role as a lifetime equivalent to a single round.
     * That means methods like getPoints() always refer to a single round (the current one).
     * Throughout the documentation, be aware of the Role vs Player designation. They mean different things.
     * @param player Player that will play this Role.
     * @param name Name of this Role.
     * @param description Description of this Role.
     * @param side Side of this Role. 1 for Good, 0 for Neutral and -1 for Bad.
     */
    public Role(Player player, String name, String description, Side side) {
        switch (side) {
            case GOOD:
                color = ChatColor.BLUE;
                break;
            case BAD:
                color = ChatColor.RED;
                break;
            default:
                color = ChatColor.GRAY;
        }
        this.player = player;
        this.side = side;
        this.name = color + name;
        this.description = color + description;
    }


    /**
     * Get this Role's name.
     * @return Role name.
     */
    public String getRoleName() {
        return name;
    }

    /**
     * Get this Role's description.
     * @return Role description.
     */
    public String getRoleDescription() {
        return description;
    }

    /**
     * Get this Role's side.
     * @return Side.
     */
    public Side getRoleSide() {
        return side;
    }

    /**
     * Get color associated to this Role. The color varies with the side of a Role.
     * @return Blue if Good, Gray if Neutral, Red if Bad.
     */
    public ChatColor getColor() {
        return color;
    }

    /**
     * Get Player who is playing this Role.
     * @return Player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Set a new Player to start playing this Role.
     * @param player New Player.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Get points currently scored by this Role.
     * @return Current points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Set points currently scored by this Role.
     * @param points Amount of points to be set.
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Add one point to this Role.
     */
    public void addPoint() {
        this.points++;
    }

    /**
     * Check if this Role is dead. Notice that a Role death is not equivalent to a Player death.
     * Players are not supposed to actually die, but become blind and immobile. That's called a Role death.
     * @return True if this Role is dead, false otherwise.
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * Set whether this Role is dead or not. Notice that a Role death is not equivalent to a Player death.
     * Players are not supposed to actually die, but become blind and immobile. That's called a Role death.
     * @param dead Whether the Role should currently be dead ot not.
     */
    public void setDead(boolean dead) {
        this.dead = dead;
    }

    /**
     * Get Player ability to pick up items.
     * @return True if Player can pick up items, false otherwise.
     */
    public boolean canPickUp() {
        return canPickUp;
    }

    /**
     * Set Player ability to pick up items.
     * @param can_pick_up Whether Player can pick up items or not.
     */
    public void setCanPickUp(boolean can_pick_up) {
        this.canPickUp = can_pick_up;
    }

    /**
     * Check if this Role is cursed. This means the Player will always win no points at the end
     * of the round.
     * @return True if this Role is cursed, false otherwise.
     */
    public boolean isCursed() {
        return cursed;
    }

    /**
     * Curse this Role. This means the Player will always win no points at the end of the round.
     * @param cursed Whether this Role should be cursed or not.
     */
    public void setCursed(boolean cursed) {
        this.cursed = cursed;
    }

    /**
     * Check if this Role is blessed. This means the Player will always win the maximum amount of
     * points at the end of the round.
     * @return True if this Role is blessed, false otherwise.
     */
    public boolean isBlessed() {
        return blessed;
    }

    /**
     * Bless this Role. This means the Player will always win the maximum amount of points at
     * the end of the round.
     * @param blessed Whether this Role should be blessed or not.
     */
    public void setBlessed(boolean blessed) {
        this.blessed = blessed;
    }

    /**
     * Check if this Role is currently exposed. In this state, if this Role is the Assassin,
     * the knife is revealed and the player is blind and immobile until he crouches.
     * @return True if this Role is exposed, false otherwise.
     */
    public boolean isExposed() {
        return exposed;
    }

    /**
     * Expose this Role. In this state, if this Role is the Assassin, the knife is revealed
     * and the player is blind and immobile until he crouches.
     * @param exposed Whether this Role should be exposed or not.
     */
    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    /**
     * Check if this Role's soul has been merged with another. When two souls are merged,
     * when one dies, the other dies as well.
     * @return True if this Role's soul has been merged, false otherwise.
     */
    public boolean isMerged() {
        return merged;
    }

    /**
     * Merge this Role's soul. When two souls are merged, when one dies, the other dies
     * as well.
     * @param merged Whether this Role should be merged or not.
     */
    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    /**
     * Check if this Role has a partnership with others. When partners, Roles can
     * revive each other.
     * @return True if partner, false otherwise.
     */
    public boolean isPartner() {
        return partner;
    }

    /**
     * Add this Role to the partnership. When partners, Roles can revive each other.
     * @param partner Whether this Role should be partner or not.
     */
    public void setPartner(boolean partner) {
        this.partner = partner;
    }

    /**
     * Check if this Role is in love with another. When in love, Role can't be away
     * from each other.
     * @return True if in love, false otherwise.
     */
    public boolean isInLove() {
        return inLove;
    }

    /**
     * Set this Role in love. When in love, Role can't be away from each other.
     * @param inLove Whether this Role should be in love or not.
     */
    public void setInLove(boolean inLove) {
        this.inLove = inLove;
    }

    /**
     * Check if this Role is in possession of a Gun (Bow).
     * @return True if Role has bow, false otherwise.
     */
    public boolean hasBow() {
        return hasBow;
    }

    /**
     * Set this Role in possession of a Gun (Bow).
     * @param hasBow Whether this Role should be in possession of a bow or not.
     */
    public void setHasBow(boolean hasBow) {
        this.hasBow = hasBow;
    }

    /**
     * Get this Role's killer.
     * @return The Player (not the Role) who killed this Role, or null if
     * this Role isn't dead.
     */
    public Player getKiller() {
        return killer;
    }

    /**
     * Set this Role's killer.
     * @param killer The Player (not the Role) who killed this Role.
     */
    public void setKiller(Player killer) {
        this.killer = killer;
    }

    /**
     * Check if this Role's ability has been activated.
     * @return True if this Role's ability has been used, false otherwise.
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * Set this Role's ability activated.
     * @param activated Whether this Role's ability has been activated.
     */
    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    /**
     * Get Death Location.
     * @return Death Location, or null if Role hasn't died.
     */
    public Location getDeathLocation() {
        return deathLocation;
    }

    /**
     * Set Death Location.
     * @param deathLocation Death location.
     */
    public void setDeathLocation(Location deathLocation) {
        this.deathLocation = deathLocation;
    }

    /**
     * Get Pot Location. This is the potted tulip that stands for the player death, like a grave.
     * @return Location of the pot.
     */
    public Location getPotLocation() {
        return potLocation;
    }

    /**
     * Set Pot Location. This is the potted tulip that stands for the player death, like a grave.
     * If player died on top of a rail, for example, pot must not replace it.
     */
    public void setPotLocation() {
        potLocation = new Location(
                getPlayer().getLocation().getWorld(),
                Math.floor(getPlayer().getLocation().getBlockX()),
                Math.floor(getPlayer().getLocation().getBlockY()),
                Math.floor(getPlayer().getLocation().getBlockZ())
        );
        while (potLocation.getBlock().getType() != Material.AIR) {
            potLocation.setY(potLocation.getY() + 1);
        }
    }

    /**
     * Set Pot Location to null.
     */
    public void clearPotLocation() {
        potLocation = null;
    }

    /**
     * Initialise Role. This method should be called upon Player
     * teleportation to the map. Sends a Popup with the Role name.
     */
    public void initialise() {
        Utils.sendPopup(getPlayer(), getRoleName(), "");
        sendRole();
    }

    /**
     * Reset this Role. Resets points and ability.
     */
    public void reset() {
        setActivated(false);
        setPoints(0);
    }

    /**
     * Clean trash left in map by this Role (for instance, Hunter's traps).
     */
    public void clean() {
        if (getPotLocation() != null) {
            getPotLocation().getBlock().setType(Material.AIR);
        }
    }

    /**
     * Award this Role because his side won this round.
     */
    public abstract void award();

    /**
     * OnBreakBlock. Called when Player tries to break a block
     * @param event Event.
     */
    public void onBreakBlock(BlockBreakEvent event) {
        if (!isDead()) {
            Terrorist terrorist = Xinada.getGame().getRound().getCurrentRole(Terrorist.class);
            if ((terrorist != null) && (terrorist.getPuzzle() != null) && (!terrorist.exploded())) {
                if (event.getBlock().getLocation().equals(terrorist.getBombLocation())) {
                    terrorist.getPlayer().closeInventory();
                    terrorist.getPuzzle().stop();
                    Xinada.getGame().getRound().getCurrentRoles().forEach((p, r) -> {
                        if (r instanceof Terrorist) p.sendMessage(ChatColor.RED +
                                String.format(
                                        Language.getString("bombDisarmed"),
                                        getPlayer().getName()
                                )
                        );
                        else p.sendMessage(ChatColor.GREEN +
                                String.format(
                                        Language.getString("bombDisarmed"),
                                        getPlayer().getName()
                                )
                        );
                    });
                }
            }
        }
        event.setCancelled(true);
    }

    /**
     * OnCommand Event. Called when Player issues command in-game.
     * @param event Event.
     */
    public void onMessage(AsyncPlayerChatEvent event) {
        Collator instance = Collator.getInstance();
        instance.setStrength(Collator.NO_DECOMPOSITION);

        if (instance.compare(event.getMessage(), "amen") == 0) {
            getPlayer().removePotionEffect(PotionEffectType.SLOW);
        }
    }

    /**
     * OnConsume Event. Called when Player consumes an item.
     * @param event Event.
     */
    public void onConsume(PlayerItemConsumeEvent event) {
        event.setCancelled(true);
    }

    /**
     * OnCrouch Event. Called when Player enters or leaves sneaking mode.
     * @param event Event.
     */
    public void onCrouch(PlayerToggleSneakEvent event) {
        if (getPlayer().isSneaking()) {
            if (isExposed()) {
                setExposed(false);
                getPlayer().removePotionEffect(PotionEffectType.JUMP); // Player gains the ability to jump
                getPlayer().removePotionEffect(PotionEffectType.BLINDNESS); // Player gains the ability to see
                getPlayer().setWalkSpeed(0.2f); // Player gains the ability to walk
            }
        }
    }

    /**
     * OnHit Event. Whenever this Player hits another, this method is called.
     * @param event Event.
     * @param wwh The Role of the Player who was hit.
     */
    public void onHit(EntityDamageByEntityEvent event, Role wwh) {}

    /**
     * OnShot. Called when this Player is shot at.
     * @param event Event.
     * @param whoShot Role of the Player who shot.
     */
    public void onShot(EntityDamageByEntityEvent event, Role whoShot) {
        if (!whoShot.isDead() && !isDead()) { // If neither of them are dead...
            if (!whoShot.getPlayer().equals(getPlayer())) {
                if (getRoleSide() == Side.BAD) { // Cop killed a bad guy:
                    // Kill who was shot:
                    kill(whoShot.getPlayer());
                    whoShot.getPlayer().sendMessage(
                            String.format(
                                    Language.getString("wasThe"),
                                    getPlayer().getName(),
                                    getRoleName()
                            )
                    );
                }
                else { // Cop killed a good guy:
                    // Kill who shot:
                    whoShot.kill(getPlayer());
                    whoShot.getPlayer().sendMessage(
                            String.format(
                                    Language.getString("wasGood"),
                                    getPlayer().getName()
                            )
                    );
                    // Kill who was shot:
                    kill(getPlayer());
                }
                Xinada.getGame().getRound().checkEnd();
            }
            else whoShot.getPlayer().sendMessage(Language.getString("selfShot"));
        }
        event.getDamager().remove();
    }

    /**
     * OnDrop. Called when a Player drops an item.
     * @param event Event.
     */
    public void onDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }


    public void onHungerDeplete(FoodLevelChangeEvent event) {
        getPlayer().setFoodLevel(20);
        event.setCancelled(true);
    }

    /**
     * OnInteract Event. Called upon interaction.
     * @param event Event.
     * @param action Action.
     */
    public void onInteract(PlayerInteractEvent event, Action action) {
        if (event.getClickedBlock() == null) return;
        if ( // If player opened any interactive block:
            event.getAction() == Action.RIGHT_CLICK_BLOCK && (
                (event.getClickedBlock().getType() == Material.CHEST) ||
                (event.getClickedBlock().getType() == Material.TRAPPED_CHEST) ||
                (event.getClickedBlock().getType() == Material.DISPENSER) ||
                (event.getClickedBlock().getType() == Material.DROPPER) ||
                (event.getClickedBlock().getType() == Material.CRAFTING_TABLE) ||
                (event.getClickedBlock().getType() == Material.FURNACE) ||
                (event.getClickedBlock().getType() == Material.FURNACE) ||
                (event.getClickedBlock().getType() == Material.ENDER_CHEST) ||
                (event.getClickedBlock().getType() == Material.ANVIL) ||
                (event.getClickedBlock().getType() == Material.BREWING_STAND) ||
                (event.getClickedBlock().getType() == Material.ENCHANTING_TABLE) ||
                (event.getClickedBlock().getType() == Material.TNT) ||
                (event.getClickedBlock().getType() == Material.JUKEBOX) ||
                (event.getClickedBlock().getType() == Material.FLOWER_POT) ||
                (event.getClickedBlock().getType().name().startsWith("POTTED_"))
            )
        ) {
            event.setCancelled(true);
        }
    }

    /**
     * OnInteractEntity. Called upon interaction with another entity.
     * @param event Event.
     * @param clicked The Role of the interacted Player.
     */
    public void onInteractEntity(PlayerInteractAtEntityEvent event, Role clicked) {
        if (clicked.isDead() && !isDead()) {
            if (isPartner() && clicked.isPartner()) {
                clicked.revive();
                getPlayer().sendMessage(ChatColor.GREEN + Language.getString("youRevivedPartner"));
                clicked.getPlayer().sendMessage(ChatColor.GREEN + Language.getString("partnerRevivedYou"));
                if (clicked instanceof Negotiator) clicked.setPoints(1);
                if (this instanceof Negotiator) setPoints(1);
            }
        }
    }

    /**
     * OnInventoryClick. Called when Player clicks somewhere
     * in his inventory.
     * @param event Event.
     */
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    /**
     * OnMove. Called when Player tries to move.
     */
    public void onMove(PlayerMoveEvent event) {}

    /**
     * OnPickup. Called when a Player picks up an Item.
     */
    public void onPickup(EntityPickupItemEvent event) {
        Material item = event.getItem().getItemStack().getType();
        if (canPickUp()) {
            if (item == Material.BOW) {
                setHasBow(true);
                return; // Can pick up
            }
            if (item == Material.ARROW) {
                return; // Can pick up
            }
        }
        event.setCancelled(true);
    }

    /**
     * OnPlaceBlock. Called when Player tries to place a block.
     * @param event Event.
     */
    public void onPlaceBlock(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    /**
     *OnFished. Called when Player is hit by a fishing rod.
     * @param event Event.
     * @param fished Who launched the fishing rod.
     */
    public void onFish(ProjectileHitEvent event, Role fished) {}

    /**
     * OnSnowball. Called when Player is hit by a snowball.
     * @param event Event.
     */
    public void onSnowball(ProjectileHitEvent event) {}

    public void onShootBow(EntityShootBowEvent event) {
        Utils.soundLocation(player.getLocation(), Sound.ENTITY_DOLPHIN_DEATH);
    }

    /**
     * OnSplash Event. Called when this Player splashes a potion.
     * @param event Event.
     */
    public void onSplash(PotionSplashEvent event) {
        event.setCancelled(true);
    }

    /**
     * Kill this Role. Player becomes blind and immobile. Also, player can't
     * jump or pick up items. If Player had a bow, it gets dropped right next
     * to him.
     * @param by Player who killed this Role.
     */
    public void kill(Player by) {
        if (isDead()) return;
        setCanPickUp(false);
        setDead(true);
        setKiller(by);
        setDeathLocation(getPlayer().getLocation());
        setPotLocation();
        if (hasBow()) dropBow();
        setHasBow(false);
        if (isMerged()) killMerged();

        // Notify:
        Utils.messageGlobal(
                String.format(
                        Language.getString("died"),
                        getPlayer().getName()
                )
        );
        Utils.soundGlobal(Sound.ENTITY_LIGHTNING_BOLT_THUNDER);
        Utils.sendPopup(
            getPlayer(),
            Language.getString("youDied"),
            Language.getString("spectatorMode")
        );

        // Spawn pot:
        getPotLocation().getBlock().setType(Material.POTTED_RED_TULIP);

        // Spectate:
        getPlayer().setGameMode(GameMode.SPECTATOR);

        Xinada.getGame().updateTabList();
    }

    /**
     * Revive this Role. Player loses blindness and can now move. He can also
     * jump and pick up items.
     */
    public void revive() {
        if (getPlayer().getGameMode() == GameMode.SPECTATOR) getPlayer().setSpectatorTarget(null);
        getPlayer().setGameMode(GameMode.SURVIVAL);

        if (getPotLocation() != null) {
            getPotLocation().getBlock().setType(Material.AIR);
        }
        clearPotLocation();

        getPlayer().teleport(getDeathLocation());
        setDeathLocation(null);

        if (getRoleSide() != Side.BAD) {
            setCanPickUp(true);
        }

        Utils.messageGlobal(
                String.format(
                        Language.getString("revived"),
                        getPlayer().getName()
                )
        );
        Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_SPLASH_POTION_BREAK);

        setDead(false);
        Xinada.getGame().updateTabList();
    }

    /**
     * Give bow to Player who's playing this role.
     */
    public void addBow() {
        // Add bow
        ItemStack bow = new ItemStack(Material.BOW, 1); // Create bow
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1); // Add Infinite Enchantment
        getPlayer().getInventory().addItem(bow); // Add bow to player's inventory
        getPlayer().getInventory().setItem(9, new ItemStack(Material.ARROW, 1)); // Add 1 arrow to player's inventory (slot 9)

        setHasBow(true);
    }

    /**
     * Give Sword to Player who's playing this role.
     */
    public void addSword() {
        Utils.addItem(getPlayer(), Material.IRON_SWORD, 0, 1);
    }

    /**
     * Drop this Role's Player bow.
     */
    public void dropBow() {
        // Remove bow and arrow:
        Utils.removeItem(getPlayer(), Material.BOW);
        Utils.removeItem(getPlayer(), Material.ARROW);
        setHasBow(false);

        // Drop Bow:
        ItemStack bow = new ItemStack(Material.BOW, 1); // Create bow
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1); // Add Infinite Enchantment
        Utils.dropItem(bow, getPlayer().getLocation()); // Drop bow
        Utils.dropItem(new ItemStack(Material.ARROW, 1), getPlayer().getLocation()); // Drop arrow

        Utils.messageGlobal(Language.getString("weaponDropped"));
    }

    /**
     * Drop this Role's Player sword.
     */
    public void dropSword() {
        Utils.soundIndividual(getPlayer(), Sound.ENTITY_CHICKEN_EGG);
        Utils.removeItem(getPlayer(), Material.IRON_SWORD); // Remove sword
        Utils.dropItem(new ItemStack(Material.IRON_SWORD, 1), getPlayer().getLocation()); // Drop sword
    }

    /**
     * Kill merged souls.
     */
    public void killMerged() {
        setMerged(false);

        Role devil = Xinada.getGame().getRound().getCurrentRole(Devil.class);
        if (devil == null) return; // Error

        for (Role role : Xinada.getGame().getRound().getCurrentRoles().values()) {
            if (!this.getPlayer().getName().equals(role.getPlayer().getName()) && role.isMerged()) {
                role.setMerged(false);
                role.kill(devil.getPlayer());
                role.getPlayer().sendMessage(
                        String.format(
                                Language.getString("diedMerged"),
                                getPlayer().getName()
                        )
                );
                devil.getPlayer().sendMessage(
                        String.format(
                                Language.getString("diedOfYourSorcery"),
                                role.getPlayer().getName()
                        )
                );
                devil.addPoint();
            }
        }
    }

    /**
     * prompt Player with his Role name and Role description.
     */
    public void sendRole() {
        getPlayer().sendMessage(
                String.format(
                        Language.getString("sendRole"),
                        getRoleName()
                )
        );
        getPlayer().sendMessage(
                String.format(
                        Language.getString("sendDescription"),
                        getRoleDescription()
                )
        );
    }
}
