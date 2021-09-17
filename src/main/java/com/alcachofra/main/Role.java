package com.alcachofra.main;

import com.alcachofra.roles.bad.Devil;
import com.alcachofra.utils.Utils;
import com.alcachofra.roles.bad.Terrorist;
import com.alcachofra.roles.good.*;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class Role {

    private Player player;

    protected  String name;
    protected  String description;
    protected  int side;
    protected  ChatColor color;

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


    /**
     * Constructor for Role. A Role as a lifetime equivalent to a single round.
     * That means methods like getPoints() always refer to a single round (the current one).
     * Throughout the documentation, be aware of the Role vs Player designation. They mean different things.
     * @param player Player that will play this Role.
     * @param name Name of this Role.
     * @param description Description of this Role.
     * @param side Side of this Role. 1 for Good, 0 for Neutral and -1 for Bad.
     */
    public Role(Player player, String name, String description, int side) {
        switch (side) {
            case 1:
                color = ChatColor.BLUE;
                break;
            case -1:
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
     * @return 1 if Good, 0 if Neutral, -1 if Bad.
     */
    public int getRoleSide() {
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
     * Get Player name. Role name and Player name are different things.
     * @return Player name.
     */
    public String getPlayerName() {
        return player.getName();
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
    public void award() { // This method will be called if Role won the round.
        addPoint(); // Add 1 point
        addPoint(); // Add 1 point
    }

    /**
     * OnHit Event. Whenever this Player hits another, this method is called.
     * @param e Event.
     * @param wwh The Role of the Player who was hit.
     */
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {}

    /**
     * OnInteract Event. This method is called upon interaction.
     * @param e Event.
     * @param a Action.
     */
    public void onInteract(PlayerInteractEvent e, Action a) {
        if (e.getClickedBlock() != null) {
            if (e.getClickedBlock().getType() == Material.FLOWER_POT || e.getClickedBlock().getType().name().startsWith("POTTED_")) {
                e.setCancelled(true);
            }
        }
    }

    /**
     * OnSplash Event. This method is called when this Player splashes a potion.
     * @param e Event.
     */
    public void onSplash(PotionSplashEvent e) {
        e.setCancelled(true);
    }

    /**
     * OnInteractEntity. This method is called upon interaction with another entity.
     * @param e Event.
     * @param clicked The Role of the interacted Player.
     */
    public void onInteractEntity(PlayerInteractAtEntityEvent e, Role clicked) {
        if (clicked.isDead() && !isDead()) {
            if (isPartner() && clicked.isPartner()) {
                clicked.revive();
                getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("5"));
                clicked.getPlayer().sendMessage(ChatColor.GREEN + Language.getRoleString("6"));
                if (clicked instanceof Negotiator) clicked.setPoints(1);
                if (this instanceof Negotiator) setPoints(1);
            }
        }
    }

    /**
     * OnShot. This method is called when this Player is shot.
     * @param e Event.
     * @param ws Role of the Player who shot.
     */
    public void onShot(EntityDamageByEntityEvent e, Role ws) {
        if (!ws.isDead() && !isDead()) { // If neither of them are dead...
            if (!ws.getPlayer().equals(getPlayer())) {
                if (getRoleSide() < 0) { // Cop killed a bad guy:
                    // Kill who was shot:
                    kill(ws.getPlayer());
                    ws.getPlayer().sendMessage(ChatColor.GREEN + getPlayerName() + " " + Language.getRoleString("7") + " " + getRoleName() + ".");
                }
                else { // Cop killed a good guy:
                    // Kill who shot:
                    ws.kill(getPlayer());
                    ws.getPlayer().sendMessage(ChatColor.RED + getPlayerName() + " " + Language.getRoleString("8"));
                    // Kill who was shot:
                    kill(getPlayer());
                }
                Xinada.getGame().getRound().checkEnd();
            }
            else ws.getPlayer().sendMessage(ChatColor.RED + Language.getRoleString("9") + ". :)");
        }
        e.getDamager().remove();
    }

    /**
     *OnFished. This method is called when Player is hit by a fishing rod.
     * @param e Event.
     * @param fished Who launched the fishing rod.
     */
    public void onFish(ProjectileHitEvent e, Role fished) {}

    /**
     * OnSnowball. This method is called when Player is hit by a snowball.
     * @param e Event.
     * @param snowballed Who got shot.
     */
    public void onSnowball(ProjectileHitEvent e, Role snowballed) {}

    /**
     * OnPlaceBlock. This method is called when Player tries to place a block.
     * @param e Event.
     */
    public void onPlaceBlock(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

    /**
     * OnBreakBlock. This method is called when Player tries to break a block
     * @param e Event.
     */
    public void onBreakBlock(BlockBreakEvent e) {
        if (!isDead()) {
            Terrorist terrorist = Xinada.getGame().getRound().getCurrentRole(Terrorist.class);
            if ((terrorist != null) && (terrorist.getPuzzle() != null) && (!terrorist.exploded())) {
                if (e.getBlock().getLocation().equals(terrorist.getBombLocation())) {
                    terrorist.getPlayer().closeInventory();
                    terrorist.getPuzzle().stop();
                    terrorist.getPlayer().sendMessage(ChatColor.RED + getPlayerName() + " " + Language.getRoleString("14") + " " + Language.getRoleString("15"));
                    Utils.messageGlobal(ChatColor.GREEN + getPlayerName() + " " + Language.getRoleString("14"));
                }
            }
        }
    }

    /**
     * OnMove. This method is called when Player tries to move.
     */
    public void onMove(PlayerMoveEvent e) {}

    /**
     * OnInventoryClick. This method is called when Player clicks somewhere
     * in his inventory.
     * @param e Event.
     */
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
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

        Utils.messageGlobal(ChatColor.GRAY + " > " + ChatColor.RED + player.getName() + " " + Language.getRoleString("16"));
        Utils.soundGlobal(Sound.ENTITY_LIGHTNING_BOLT_THUNDER);
        Utils.sendPopup(
            getPlayer(),
            ChatColor.RED + Language.getRoleString("17"),
            ChatColor.RED + (((this instanceof Survivor) && !isActivated()) ? Language.getRoleString("18") : Language.getRoleString("19"))
        );

        Xinada.getGame().updateTabList();

        // Spawn pot:
        getPotLocation().getBlock().setType(Material.POTTED_RED_TULIP);

        // Spectate:
        getPlayer().setGameMode(GameMode.SPECTATOR);
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

        if (getRoleSide() > -1) {
            setCanPickUp(true);
        }

        Utils.messageGlobal(ChatColor.GRAY + " > " + ChatColor.GREEN + getPlayerName() + " " + Language.getRoleString("20"));
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

        Utils.messageGlobal(ChatColor.GRAY + " > " + ChatColor.BLUE + Language.getRoleString("21"));
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
            if (!this.getPlayerName().equals(role.getPlayerName()) && role.isMerged()) {
                role.setMerged(false);
                role.kill(devil.getPlayer());
                role.getPlayer().sendMessage(ChatColor.RED + this.getPlayerName() + " " + Language.getRoleString("22"));
                devil.getPlayer().sendMessage(ChatColor.GREEN + role.getPlayerName() + " " + Language.getRoleString("23"));
                devil.addPoint();
            }
        }
    }

    /**
     * prompt Player with his Role name and Role description.
     */
    public void sendRole() {
        getPlayer().sendMessage(ChatColor.GRAY + " > " + Language.getRoleString("role") + ": " + getRoleName());
        getPlayer().sendMessage(ChatColor.GRAY + " > " + Language.getRoleString("description") + ": " + getRoleDescription());
    }
}
