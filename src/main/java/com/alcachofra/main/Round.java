package com.alcachofra.main;

import com.alcachofra.roles.bad.*;
import com.alcachofra.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.alcachofra.main.Xinada.GAME;
import static com.alcachofra.main.Xinada.MAPS;

public class Round {
    private final int id;
    private final String map;
    private final Map<Player, Role> currentRoles;
    private final Map<Player, Role> originalRoles = new HashMap<>();
    private RoundCountdown roundCountdown;

    private int swordsDropped = 0;

    /**
     * Enum EndCause. Indicates what caused a Round to end.
     */
    public enum EndCause {
        GOOD_WON,
        BAD_WON,
        TERRORIST_WON,
        FORCED_ROUND_END,
        FORCED_GAME_END
    }

    /**
     * Constructor for Round. Randomizes a Map and draws Roles for this Round.
     * @param id Identifier of this Round.
     * @param players Set of Players to participate in this round.
     * @throws IllegalAccessException When an Instance of a Role can't be accessed.
     * @throws InvocationTargetException When a Role Constructor can't be invoked.
     * @throws InstantiationException When an Instance of a Role can't be created.
     */
    public Round(int id, Set<Player> players) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        this.id = id;
        this.map = randomizeMap();
        this.currentRoles = RoleManager.draw(players);
        this.originalRoles.putAll(currentRoles);
    }

    /**
     * Start this Round. Spawns Players, initialises Roles and starts the Round countdown. Also, outputs the name
     * of this Round's Map.
     */
    public void start() {
        if (Xinada.getGame().getPlayers().keySet().size() < currentRoles.keySet().size()) {
            end(EndCause.FORCED_ROUND_END); // If someone left the Game in Round interval.
        }
        else {
            spawnPlayers(map, currentRoles.keySet());
            currentRoles.forEach((player, role) -> role.initialise());
            Utils.soundGlobal(Sound.ENTITY_PLAYER_LEVELUP);
            roundCountdown(id);
            new BukkitRunnable() {
                public void run() {
                    Utils.messageGlobal(Xinada.getTag() + Language.getString("map") + ": " + ChatColor.WHITE + map);
                }
            }.runTaskLater(Xinada.getPlugin(), 20 * 2);
        }
    }

    /**
     * End this Round. Cancels Round countdown, scores Players' points and outputs, and cleans Players.
     * @param cause Ending cause. What caused the Round to end.
     */
    public void end(EndCause cause) {
        if (roundCountdown != null) roundCountdown.cancel();
        scorePoints(cause);
        clean();
        Utils.soundGlobal(Sound.ENTITY_BLAZE_HURT);
        Utils.messageGlobal(
                Xinada.getTag() +
                        String.format(
                                Language.getString("roundEnded"),
                                id
                        )
        );
        outputPoints();
    }

    /**
     * Starts the round countdown. Starts at "game.roundTime" minutes. This field can
     * be found in game.yml.
     * @param round Round identifier.
     */
    public void roundCountdown(int round) {
        roundCountdown = new RoundCountdown(
                Config.get(GAME).getInt("game.roundTime") * 60,
                1,
                0,
                round,
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        end(EndCause.GOOD_WON);
                    }
                }
        );
        roundCountdown.setMessage(
                Xinada.getTag() + Language.getString("roundEndingIn")
        ).start();
    }

    /**
     * Looks for an instance of a specific Role participating in this Round.
     * @param roleClass Role you're looking for.
     * @param <T> Class of the specified Role.
     * @return Instance of Role participating, or null if Class specified has not been instantiated for this game.
     */
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


    public Map<Player, Role> getCurrentRoles() {
        return currentRoles;
    }

    /**
     * Get number of swords dropped on the floor.
     * @return Number of swords dropped.
     */
    public int getSwordsDropped() {
        return swordsDropped;
    }

    /**
     * Increment number of swords dropped on the floor.
     */
    public void addSwordsDropped() {
        this.swordsDropped++;
    }

    /**
     * Decrement number of swords dropped on the floor.
     */
    public void subtractSwordsDropped() {
        this.swordsDropped--;
    }

    /**
     * Clean Round. Cleans Map and Players.
     */
    public void clean() {
        WorldManager.clean();
        for (Role role : currentRoles.values()) {
            role.clean();
            role.getPlayer().closeInventory();
        }
    }

    /**
     * Get a list of Maps from the Config File, and randomize.
     * @return The rolled map.
     */
    private String randomizeMap() {
        Random rand = new Random();

        ArrayList<String> mapNames = new ArrayList<>(
                Objects.requireNonNull(Config.get(MAPS).getConfigurationSection("")).getKeys(false)
        );
        return mapNames.get(rand.nextInt(mapNames.size()));
    }

    /**
     * Spawn Players in Map.
     * @param map The map where Players will spawn.
     * @param players Set of Players that will spawn.
     */
    private void spawnPlayers(String map, Collection<Player> players) {
        Random rand = new Random();

        int[] used = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // In order to not repeat spawn locations
        int spawnLocation; // Random spawn location

        // Spawn players:
        for (Player player : players) {
            do { // Generate random numbers without repeating
                spawnLocation = rand.nextInt(Config.get(MAPS).getInt(map + ".spawnsNum")) + 1; // From 1 to 10
            } while (used[spawnLocation-1] == 1);
            used[spawnLocation-1] = 1;

            Utils.cleanPlayer(player);
            player.teleport(new Location( // Teleports player
                    player.getWorld(),
                    Config.get(MAPS).getDouble(map + ".location" + spawnLocation + ".x"),
                    Config.get(MAPS).getDouble(map + ".location" + spawnLocation + ".y"),
                    Config.get(MAPS).getDouble(map + ".location" + spawnLocation + ".z")
            ));
        }
    }

    /**
     * Award Players their points, and update game score.
     * @param cause End cause. This variable has information on which side won.
     */
    private void scorePoints(EndCause cause) {
        currentRoles.forEach((player, role) -> {
            switch (cause) {
                case FORCED_GAME_END:
                case FORCED_ROUND_END:
                    role.setPoints(0);
                    break;
                case GOOD_WON:
                    if (role.getRoleSide() == Role.Side.GOOD && !role.isDead()) role.award();
                    break;
                case BAD_WON:
                    if (role.getRoleSide() == Role.Side.BAD && !role.isDead()) role.award();
                    break;
                case TERRORIST_WON:
                    if (role instanceof Terrorist) role.award();
                    else role.setPoints(0);
                    break;
            }
            Xinada.getGame().addPoints(player, role.getPoints());
        });
    }

    /**
     * Print this Round's players points to chat.
     */
    private void outputPoints() {
        Utils.messageGlobal("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "----------------------------------");
        for (Player player : currentRoles.keySet()) {
            Utils.messageGlobal(
                    ChatColor.GRAY + " > " + originalRoles.get(player).getColor() +
                            "{" + originalRoles.get(player).getRoleName() + "} " +
                            ChatColor.GRAY + originalRoles.get(player).getPlayer().getName() + ": " +
                            ChatColor.GOLD + "+ " + currentRoles.get(player).getPoints() + " " + Language.getString("points") + "."
            );
        }
        Utils.messageGlobal("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "----------------------------------");
    }

    /**
     * Check if game has ended. If the case, calls Game.endRound() accordingly.
     */
    public void checkEnd() {
        boolean end = false;

        Role accomplice = getCurrentRole(Accomplice.class);
        Role murderer = getCurrentRole(Murderer.class);
        Role traitor = getCurrentRole(Traitor.class);
        Terrorist terrorist = getCurrentRole(Terrorist.class);

        if (terrorist != null && terrorist.exploded()) {
            Xinada.getGame().endRound(EndCause.TERRORIST_WON);
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
            Xinada.getGame().endRound(EndCause.GOOD_WON); // Game ends and good side wins
        }
        else {
            // Check win for bad side
            for (Role role : currentRoles.values()) {
                if (!role.isDead()) {
                    if (role.getRoleSide() != Role.Side.BAD) { // If there are good roles alive
                        return; // Else, game doesn't end (there's no else {} because when it gets out of end(), this method needs to return ASAP)
                    }
                }
            }
            // There are no good roles alive:
            Xinada.getGame().endRound(EndCause.BAD_WON); // Game ends and the bad side wins
        }
    }
}
