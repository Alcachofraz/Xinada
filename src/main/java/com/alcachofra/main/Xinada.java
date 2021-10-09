package com.alcachofra.main;

import com.alcachofra.utils.Config;
import com.alcachofra.utils.Language;
import com.alcachofra.utils.Utils;
import com.alcachofra.events.*;
import com.alcachofra.utils.WorldManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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

    public static int GAME = 0;
    public static int STRINGS = 1;
    public static int MAPS = 2;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        plugin = this;

        new Config.Builder()
                .setPlugin(this)
                .setPaths(
                        "game.yml",
                        "strings" + getConfig().getString("game.language") + ".yml",
                        "maps.yml"
                ).build();

        new WorldManager.Builder()
                .setPlugin(this)
                .setWorldName(getConfig().getString("world.name"))
                .build();

        new Language.Builder()
                .setFileConfiguration(Config.get(STRINGS))
                .build();

        Bukkit.getServer().createWorld(new WorldCreator("Xinada"));
        Objects.requireNonNull(Bukkit.getServer().getWorld("Xinada")).setDifficulty(Difficulty.PEACEFUL);

        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new BreakBlocks(), this);
        pm.registerEvents(new Chat(), this);
        pm.registerEvents(new Consumables(), this);
        pm.registerEvents(new Crouch(), this);
        pm.registerEvents(new Damage(), this);
        pm.registerEvents(new Drop(), this);
        pm.registerEvents(new Hunger(), this);
        pm.registerEvents(new Interact(), this);
        pm.registerEvents(new InteractEntity(), this);
        pm.registerEvents(new InventoryClick(), this);
        pm.registerEvents(new Join(), this);
        pm.registerEvents(new Leave(), this);
        pm.registerEvents(new Move(), this);
        pm.registerEvents(new Pickup(), this);
        pm.registerEvents(new PlaceBlocks(), this);
        pm.registerEvents(new Projectile(), this);
        pm.registerEvents(new ShootBow(), this);
        pm.registerEvents(new Splash(), this);

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
     * Check if a game has started.
     * @return true if in game, false otherwise.
     */
    public static boolean inGame() {
        return game != null;
    }

    /**
     * Get Xinada purple tag for chat messages.
     * @return Xinada Tag.
     */
    public static String getTag() {
        return ChatColor.DARK_PURPLE + "[Xinada] " + ChatColor.RESET;
    }

    /**
     * Start a new game. Creates a game instance and calls method start().
     * @return true if game was successfully created, false if not enough/too many players.
     */
    public boolean startGame() {
            HashSet<Player> players = Utils.getOnlinePlayers();
            if (RoleManager.DEBUG || (players.size() > 2 && players.size() <= 10)) {
                game = new Game(players, Config.get(GAME).getInt("game.rounds"));
                game.start(); // Start game
                return true;
            }
            return false;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) { // If command is executed by a Player

            Player player = (Player) sender;

            if (label.equalsIgnoreCase("start")) { // "/start"
                if (inGame()) player.sendMessage(getTag() + Language.getString("gameAlreadyStarted"));
                else if (!startGame()) Utils.messageGlobal(getTag() + Language.getString("notEnoughPlayers"));
                return true;
            }
            if (label.equalsIgnoreCase("role")) { // "/role"
                if (inGame()) {
                    if (game.inRound()) { // If a round has started...
                        Role role = game.getRound().getCurrentRole(player);
                        if (game.getPlayers().containsKey(player)) role.sendRole();
                        else player.sendMessage(getTag() + Language.getString("notPartOfGame"));
                    }
                    else player.sendMessage(getTag() + Language.getString("waitAndTryAgain"));
                }
                else player.sendMessage(getTag() + Language.getString("gameNotStarted"));
                return true;
            }
            if (label.equalsIgnoreCase("next")) { // "/next"
                if (inGame()) {
                    if (game.inRound()) game.endRound(Round.EndCause.FORCED_ROUND_END); // Skip round
                    else player.sendMessage(getTag() + Language.getString("waitAndTryAgain"));
                }
                else player.sendMessage(getTag() + Language.getString("gameNotStarted"));
                return true;
            }
            if (label.equalsIgnoreCase("end")) { // "/end"
                if (inGame()) {
                    if (game.inRound()) game.endRound(Round.EndCause.FORCED_GAME_END);
                    else player.sendMessage(getTag() + Language.getString("waitAndTryAgain"));
                }
                else player.sendMessage(getTag() + Language.getString("gameNotStarted"));
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
