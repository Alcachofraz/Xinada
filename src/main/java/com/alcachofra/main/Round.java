package com.alcachofra.main;

import com.alcachofra.roles.neutral.Sheep;
import com.alcachofra.utils.Utils;
import com.alcachofra.roles.bad.*;
import com.alcachofra.utils.Countdown;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Round {

    private int id;
    private String map = null;
    private final Set<Player> players;

    private Map<Player, Role> currentRoles;
    private final Map<Player, Role> originalRoles = new HashMap<>();

    private int swords_dropped = 0;

    Countdown roundCountdown;


    /**
     * Unique constructor for Round. Takes an Identifier and a list of Players that will participate.
     * @param id Identifier.
     * @param players Participants.
     */
    public Round(int id, Set<Player> players) {
        this.players = players;
        this.id = id;
    }


    /**
     * Get Round Identifier.
     * @return Identifier.
     */
    public int getID() {
        return id;
    }

    /**
     * Set Round Identifier.
     * @param id Identifier.
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * Get the map name this round is being played on.
     * @return Map name.
     */
    public String getMap() {
        return map;
    }

    /**
     * Set this round's current map.
     * @param map Name of the map.
     */
    public void setMap(String map) {
        this.map = map;
    }

    /**
     * Get a Map of this round's Players and their current Roles.
     * @return Map of Players and their current Roles.
     */
    public Map<Player, Role> getCurrentRoles() {
        return currentRoles;
    }

    /**
     * Get a Map of this round's Players and their original Roles.
     * @return Map of Players and their original Roles.
     */
    public Map<Player, Role> getOriginalRoles() {
        return originalRoles;
    }

    public int getSwordsDropped() {
        return swords_dropped;
    }

    public void addSwordsDropped() {
        this.swords_dropped++;
    }

    public void subtractSwordsDropped() {
        this.swords_dropped--;
    }

    public Countdown getCountdown() {
        return roundCountdown;
    }

    /**
     * Get nearest assassin (i. e. all bad Roles that can kill), relative to a certain Role's Player.
     * @param r Role.
     * @return The nearest Assassin Role.
     */
    public Role getNearestAssassin(Role r) {
        Role nearest = getCurrentRole(Murderer.class);
        Role aux;
        if ((aux = getCurrentRole(Accomplice.class)) != null) {
            if (r.getPlayer().getLocation().distance(aux.getPlayer().getLocation())
                <
                r.getPlayer().getLocation().distance(nearest.getPlayer().getLocation())
            ) nearest = aux;
        }
        if ((aux = getCurrentRole(Traitor.class)) != null) {
            if (
                r.getPlayer().getLocation().distance(aux.getPlayer().getLocation())
                <
                r.getPlayer().getLocation().distance(nearest.getPlayer().getLocation())
            ) nearest = aux;
        }
        return nearest;
    }

    public <T extends Role> T getCurrentRole(Class<T> roleClass) {
        for (Role role : currentRoles.values()) {
            if (roleClass.isInstance(role)) {
                return roleClass.cast(role);
            }
        }
        return null;
    }

    /**
     * Get current Role of a certain Player.
     * @param p Player.
     * @return Current Role.
     */
    public Role getCurrentRole(Player p) {
        return currentRoles.get(p);
    }

    /**
     * Get original Role of a certain Player.
     * @param p Player.
     * @return Original Role.
     */
    public Role getOriginalRole(Player p) {
        return originalRoles.get(p);
    }

    /**
     * Start this Round. Draws roles, spawns players, initialises roles and
     * starts the round countdown.
     */
    public void start() {
        System.out.println("Round - Starting Round");
        if ((currentRoles = RoleManager.draw(players)) != null) {
            originalRoles.putAll(currentRoles);
            spawnPlayers();
            currentRoles.forEach((player, role) -> role.initialise());
            Utils.soundGlobal(Sound.ENTITY_PLAYER_LEVELUP);
            Xinada.getGame().updateTabList();
            startRoundCountdown(getID());
            new BukkitRunnable() {
                public void run() {
                    Utils.messageGlobal(ChatColor.DARK_PURPLE + "[Xinada] " + Language.getRoundString("map") + ": " + ChatColor.WHITE + getMap());
                }
            }.runTaskLater(Xinada.getPlugin(), 20*2);
        }
        else {
            // Error drawing roles...
            Utils.messageGlobal(
                    ChatColor.DARK_PURPLE + "[Xinada] " +
                            ChatColor.GRAY + Language.getXinadaString("errorOccurred")
            );
            Xinada.getGame().endRound(true);
        }
    }

    /**
     * End this Round. Updates and prints points, cleans world and players and
     * @param side Winning side. If 1, good guys win and if -1 bad guys win.
     *             If 0, no one wins.
     */
    public void end(int side) { // side -> winning side (if 0, all players receive 0 points)
        System.out.println("Round - Ending Round");
        Xinada.getGame().setInRound(false); // Game no longer in round
        if (roundCountdown != null) roundCountdown.cancel();

        updatePoints(side);

        Utils.soundGlobal(Sound.ENTITY_BLAZE_HURT);
        Utils.messageGlobal(
                ChatColor.DARK_PURPLE + "[Xinada] " +
                ChatColor.GRAY + Language.getGameString("round") + " " +
                ChatColor.DARK_PURPLE + getID() +
                ChatColor.GRAY + " " + Language.getRoundString("ended")
        );

        WorldManager.clean();
        for (Role role : currentRoles.values()) {
            role.clean();
            role.getPlayer().closeInventory();
        }

        Xinada.getGame().updateTabList();
        printPoints();

        // End round in 5 seconds:
        new BukkitRunnable() {
            public void run() {
                Xinada.getGame().endRound(false);
                this.cancel();
            }
        }.runTaskLater(Xinada.getPlugin(), 20*5); // 20 ticks = 1 second
    }

    /**
     * Starts the round countdown. Starts at "game.roundTime" minutes. This field can
     * be found in config.yml.
     * @param round Round identifier.
     */
    public void startRoundCountdown(int round) {
        roundCountdown = new Countdown(
                Xinada.getPlugin().getConfig().getInt("game.roundTime") * 60,
                1,
                0,
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        end(1);
                    }
                }
        );
        roundCountdown.setMessage(
                ChatColor.DARK_PURPLE + "[Xinada] " +
                ChatColor.GRAY + Language.getGameString("round") + " " +
                ChatColor.DARK_PURPLE + round +
                ChatColor.GRAY + " " + Language.getRoundString("endingIn") + " " +
                ChatColor.GOLD + "%s" +
                ChatColor.GRAY + " %s..."
        ).start();
    }

    /**
     * Rolls a map, cleans Players and teleports them to the rolled map.
     */
    private void spawnPlayers() {
        Random rand = new Random();

        // Get map names:
        ArrayList<String> mapNames = new ArrayList<>(
            Objects.requireNonNull(Xinada.getMapsConfig().getConfigurationSection("")).getKeys(false)
        );

        int[] used = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // In order to not repeat spawn locations
        int spawnLocation; // Random spawn location

        // Randomize new world:
        setMap(mapNames.get(rand.nextInt(mapNames.size())));

        // Spawn players:
        for (Player player : players) {
            do { // Generate random numbers without repeating
                spawnLocation = rand.nextInt(Xinada.getMapsConfig().getInt(getMap() + ".spawnsNum")) + 1; // From 1 to 10
            } while (used[spawnLocation-1] == 1);
            used[spawnLocation-1] = 1;

            Utils.cleanPlayer(player);
            player.teleport(new Location( // Teleports player
                player.getWorld(),
                Xinada.getMapsConfig().getDouble(getMap() + ".location" + spawnLocation + ".x"),
                Xinada.getMapsConfig().getDouble(getMap() + ".location" + spawnLocation + ".y"),
                Xinada.getMapsConfig().getDouble(getMap() + ".location" + spawnLocation + ".z")
            ));
        }
    }

    /**
     * Check if game has ended.
     */
    public void checkEnd() {
        boolean end = false;

        Role accomplice = getCurrentRole(Accomplice.class);
        Role murderer = getCurrentRole(Murderer.class);
        Role traitor = getCurrentRole(Traitor.class);
        Terrorist terrorist = getCurrentRole(Terrorist.class);

        if (terrorist != null && terrorist.exploded()) {
            end(-1);
            return;
        }

        // Check win for good side:
        if (accomplice != null) { // If there is an Accomplice
            if (accomplice.isDead() && accomplice.isActivated()) { // If Accomplice died with the knife
                end = true; // Game ends
            }
            else if (murderer.isDead() && murderer.isActivated()) { // If Murderer died with the knife
                end = true; // Game ends
            }
            else if (murderer.isDead() && accomplice.isDead()) { // If Murderer and Accomplice died
                end = true; // Game ends
            }
        }
        else if (murderer.isDead()) { // If there is no Accomplice and Murderer is dead
            end = true; // Game ends
        }
        if (traitor != null) { // If there is a Traitor
            if (!traitor.isDead()) { // If Traitor is alive
                if (traitor.isActivated()) { // If Traitor has a knife
                    end = false; // Game doesn't end
                }
                else if (getSwordsDropped() > 0) end = false; // If there are knives on the floor
            }
        }

        if (end) { // If the good side has already won up there
            end(1); // Game ends and good side wins
        }
        else {
            // Check win for bad side
            for (Role role : currentRoles.values()) {
                if (!role.isDead()) {
                    if (role.getRoleSide() >= 0) { // If there are good roles alive
                        return; // Else, game doesn't end (there's no else {} because when it gets out of end(), this method needs to return ASAP)
                    }
                }
            }
            // There are no good roles alive:
            end(-1); // Game ends and the bad side wins
        }
    }

    /**
     * Award players their points, and update game score.
     * @param side Winning side.
     */
    private void updatePoints(int side) {
        // No winners:
        if (side == 0) {
            for (Role role : currentRoles.values()) {
                role.setPoints(0);
            }
        }
        else {
            currentRoles.forEach((player, role) -> {
                if (!(role instanceof Monster)) { // If not the Monster
                    if (role.getRoleSide() == side) { // If his side won
                        if (!role.isDead()) {
                            role.award();
                        }
                    }
                    if ((role instanceof Sheep) && (!role.isActivated())) role.setPoints(2); // If Sheep still has its wool
                    if (role instanceof Terrorist) {
                        Terrorist t = (Terrorist) role;
                        if (t.exploded()) t.setPoints(2);
                    }
                    if (role.isBlessed()) role.setPoints(3); // Blessed
                    if (role.isCursed()) role.setPoints(0); // Cursed
                }
                Xinada.getGame().addPoints(player, role.getPoints());
            });
        }
    }

    /**
     * Print this Round's players points to chat.
     */
    private void printPoints() {
        Utils.messageGlobal("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "----------------------------------");
        for (Player player : currentRoles.keySet()) {
            Utils.messageGlobal(
                ChatColor.GRAY + " > " + originalRoles.get(player).getColor() +
                        "{" + originalRoles.get(player).getRoleName() + "} " +
                        ChatColor.GRAY + originalRoles.get(player).getPlayerName() + ": " +
                        ChatColor.GOLD + "+ " + currentRoles.get(player).getPoints() + " " + Language.getRoundString("points") + "."
            );
        }
        Utils.messageGlobal("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "----------------------------------");
    }
}
