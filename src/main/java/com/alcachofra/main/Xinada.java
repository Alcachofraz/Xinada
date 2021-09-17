package com.alcachofra.main;

import com.alcachofra.utils.Utils;
import com.alcachofra.utils.FileManager;
import com.alcachofra.events.*;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Objects;

public class Xinada extends JavaPlugin implements Listener {
    private static Plugin plugin;
    private static Game game;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        plugin = this;

        new FileManager.Builder()
                .setPlugin(this)
                .setPaths(
                        "strings" + getConfig().getString("game.language") + ".yml",
                        "maps.yml",
                        "jokes" + getConfig().getString("game.language") + ".yml"
                ).build();

        new WorldManager.Builder()
                .setPlugin(this)
                .setWorldName(getConfig().getString("world.name"))
                .build();

        Bukkit.getServer().createWorld(new WorldCreator("Xinada"));
        Objects.requireNonNull(Bukkit.getServer().getWorld("Xinada")).setDifficulty(Difficulty.PEACEFUL);

        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new BreakBlocks(this), this);
        pm.registerEvents(new Chest(this), this);
        pm.registerEvents(new Consumables(this), this);
        pm.registerEvents(new Crouch(this), this);
        pm.registerEvents(new Damage(this), this);
        pm.registerEvents(new Drop(this), this);
        pm.registerEvents(new Hunger(this), this);
        pm.registerEvents(new Interact(this), this);
        pm.registerEvents(new InteractEntity(this), this);
        pm.registerEvents(new InventoryClick(this), this);
        pm.registerEvents(new Join(this), this);
        pm.registerEvents(new Leave(this), this);
        pm.registerEvents(new Move(this), this);
        pm.registerEvents(new Pickup(this), this);
        pm.registerEvents(new PlaceBlocks(this), this);
        pm.registerEvents(new Projectile(this), this);
        pm.registerEvents(new ShootBow(this), this);
        pm.registerEvents(new Splash(this), this);

        plugin = this;
    }

    @Override
    public void onDisable() {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Get plugin instance.
     * @return plugin instance.
     */
    public static Plugin getPlugin() {
        return plugin;
    }

    /**
     * Get current game.
     * @return Game instance or null if a game hasn't started.
     */
    public static Game getGame() {
        return game;
    }

    /**
     * Drop current game instance.
     */
    public static void dropGame() { game = null;}

    /**
     * Check if a game has started.
     * @return true if in game, false otherwise.
     */
    public static boolean inGame() {
        return game != null;
    }

    /**
     * Get Strings config file. This file contains all strings in the language defined in config.yml.
     * @return Strings config file.
     */
    public static FileConfiguration getStringsConfig() {
        return FileManager.getConfigs().get(0);
    }

    /**
     * Get Maps config file. This file contains maps names and spawn positions.
     * @return Maps config file.
     */
    public static FileConfiguration getMapsConfig() {
        return FileManager.getConfigs().get(1);
    }

    /**
     * Get Jokes config file. This file contains all jokes in the language defined in config.yml.
     * @return Jokes config file.
     */
    public static FileConfiguration getJokesConfig() {
        return FileManager.getConfigs().get(2);
    }

    /**
     * Teleport player to Lobby.
     * @param player Player to teleport.
     */
    public static void teleportLobby(Player player) {
        player.teleport(new Location(player.getWorld(), 570.5, 54, 943.5));
    }

    /**
     * Start a new game. Creates a game instance and calls method start().
     * @return true if game was successfully created, false if not enough/too many players.
     */
    public boolean startGame() {
            HashSet<Player> players = Utils.getOnlinePlayers();
            if (RoleManager.DEBUG || (players.size() > 2 && players.size() <= 10)) {
                game = new Game(players); // Create game with players and maps
                game.start(); // Start game
                return true;
            }
            return false;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) { // If command is executed by a Player

            Player player = (Player) sender;

            if (label.equalsIgnoreCase("start")) { // /start
                if (inGame()) player.sendMessage(
                        ChatColor.DARK_PURPLE + "[Xinada] " +
                        ChatColor.GRAY + Language.getXinadaString("gameAlreadyStarted"));
                else if (!startGame()) Utils.messageGlobal(
                        ChatColor.DARK_PURPLE + "[Xinada] " +
                        ChatColor.GRAY + Language.getXinadaString("notEnoughPlayers"));
                return true;
            }
            if (label.equalsIgnoreCase("role")) { // /role
                if (inGame()) {
                    if (game.inRound()) { // If a round has started...
                        Role role = game.getRound().getCurrentRole(player);
                        if (role == null) player.sendMessage(ChatColor.DARK_PURPLE + "[Xinada] " + ChatColor.GRAY + Language.getXinadaString("notPartOfGame"));
                        else role.sendRole();
                    }
                    else player.sendMessage(ChatColor.DARK_PURPLE + "[Xinada] " + ChatColor.GRAY + Language.getXinadaString("wait"));
                }
                else player.sendMessage(ChatColor.DARK_PURPLE + "[Xinada] " + ChatColor.GRAY + Language.getXinadaString("gameNotStarted"));
                return true;
            }
            if (label.equalsIgnoreCase("next")) { // /next
                if (inGame()) {
                    if (game.inRound()) game.getRound().end(0); // Skip round
                    else player.sendMessage(ChatColor.DARK_PURPLE + "[Xinada] " + ChatColor.GRAY + Language.getXinadaString("wait"));
                }
                else player.sendMessage(ChatColor.DARK_PURPLE + "[Xinada] " + ChatColor.GRAY + Language.getXinadaString("gameNotStarted"));
                return true;
            }
            if (label.equalsIgnoreCase("end")) { // /end
                if (inGame()) {
                    if (game.inRound()) {
                        game.getRound().setID(plugin.getConfig().getInt("game.rounds"));
                        game.endRound(true);
                    }
                    else player.sendMessage(ChatColor.DARK_PURPLE + "[Xinada] " + ChatColor.GRAY + Language.getXinadaString("wait"));
                }
                else player.sendMessage(ChatColor.DARK_PURPLE + "[Xinada] " + ChatColor.GRAY + Language.getXinadaString("gameNotStarted"));
                return true;
            }
        }
        else { // If command is executed from console
            sender.sendMessage(ChatColor.GRAY + "Commands only work from players, for this plugin.");
            return true;
        }
        return false;
    }
}
