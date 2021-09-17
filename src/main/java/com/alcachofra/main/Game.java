package com.alcachofra.main;

import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Countdown;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Game {

    private final HashMap<Player, Integer> players = new HashMap<>();

    private boolean inRound = false;
    private Round round;

    private final Plugin plugin;


    /**
     * Unique constructor for Game. Takes a list of players that will participate.
     * @param players Players to participate.
     */
    public Game(Set<Player> players) {

        // Every player starts with 0 points :
        for (Player player : players) {
            this.players.put(player, 0);
        }

        plugin = Xinada.getPlugin();
        round = null;
    }

    /**
     * Get Participants and their game points.
     * @return Map containing Players and their points.
     */
    public HashMap<Player, Integer> getPlayers() {
        return players;
    }

    public void addPoints(Player player, int points) {
        players.put(player, players.get(player) + points);
    }

    /**
     * Get current round.
     * @return Current round instance or null if no round has started.
     */
    public Round getRound() {
        return round;
    }

    /**
     * Check if a round has started.
     * @return True if a round has started, false otherwise.
     */
    public boolean inRound() {
        return inRound;
    }

    /**
     * Set game in round.
     * @param inRound Whether this game is in a round or not.
     */
    public void setInRound(boolean inRound) {
        this.inRound = inRound;
    }

    /**
     * Start this game. This method will create a countdown of 5 seconds,
     * and then call startRound() for the first round.
     */
    public void start() {
        Countdown countdown = new Countdown(
                5,
                1,
                1,
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        startRound(1);
                    }
                }
        );
        countdown.setFirstMessage(
                ChatColor.DARK_PURPLE + "[Xinada] " +
                ChatColor.GRAY + Language.getGameString("gameStarting")
        ).setMessage(
                ChatColor.DARK_PURPLE + "[Xinada] " +
                ChatColor.GRAY + Language.getGameString("round") + " " +
                ChatColor.BOLD + ChatColor.DARK_PURPLE + "1" +
                ChatColor.RESET + ChatColor.GRAY + " " + Language.getGameString("starting") + " " +
                ChatColor.GOLD + "%s" +
                ChatColor.GRAY + "..."
        ).start();
    }

    /**
     * Start a new round. The new round should be identified with an integer.
     * @param n Identifier.
     */
    public void startRound(int n) {
        setInRound(true);
        round = new Round(n, players.keySet());
        round.start();
    }

    /**
     * End the current round. This method will update the Tab List, teleport all players to the lobby and "clean" them.
     * The term "clean" stands for inventory items, effects, health and hunger level.
     * At this point, if last round was reached, this game ends.
     * If not, a countdown of 5 seconds starts, and then a new round starts.
     * @param forced If round didn't end naturally. (probably because /next or /end was issued)
     */
    public void endRound(boolean forced) {
        if (Xinada.getGame().getRound().getCountdown() != null) Xinada.getGame().getRound().getCountdown().cancel();

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            Xinada.teleportLobby(p);
            Utils.cleanPlayer(p);
        }
        updateTabList();

        // If it's the last round:
        if (round.getID() >= plugin.getConfig().getInt("game.rounds")) {
            end(forced);
        }

        // If not enough players:
        else if (Xinada.getGame().getPlayers().size() < 3) {
            Utils.messageGlobal(ChatColor.DARK_PURPLE + "[Xinada] " + ChatColor.GRAY + Language.getXinadaString("notEnoughPlayers"));
            end(true);
        }

        // Else jump to next round in 5 seconds:
        else {
            Countdown countdown = new Countdown(
                    5,
                    1,
                    1,
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            startRound(round.getID() + 1);
                        }
                    }
            );
            countdown.setMessage(
                    ChatColor.DARK_PURPLE + "[Xinada] " +
                    ChatColor.GRAY + Language.getGameString("round") + " " +
                    ChatColor.BOLD + ChatColor.DARK_PURPLE + (round.getID() + 1) +
                    ChatColor.RESET + ChatColor.GRAY + " " + Language.getGameString("starting") + " " +
                    ChatColor.GOLD + "%s" +
                    ChatColor.GRAY + "..."
            ).start();
        }
    }

    /**
     * End this game. This method teleports all players to the lobby and "clean" them.
     * Then, calls Xinada.dropGame().
     * @param forced If round didn't end naturally. (probably because /next or /end was issued).
     *               If true, winner won't be output.
     */
    private void end(boolean forced) {
        if (!forced) printWinner();

        Utils.soundGlobal(Sound.BLOCK_NOTE_BLOCK_PLING);
        Utils.messageGlobal(ChatColor.DARK_PURPLE + "[Xinada] " + ChatColor.GRAY + Language.getGameString("gameEnded"));

        Xinada.dropGame();
    }

    /**
     * Remove a player from this game
     * @param p Player to remove
     */
    public void removePlayerFromGame(Player p) {
        players.remove(p);
    }

    /**
     * Output the game winner and his score. If there's a tie, all winners will be
     * output.
     */
    public void printWinner() {
        ArrayList<Player> winners = new ArrayList<>();
        int most = 0;

        // Find winners:
        for (Map.Entry<Player, Integer> entry : players.entrySet()) {
            if (entry.getValue() > most) {
                winners.clear();
                winners.add(entry.getKey());
                most = entry.getValue();
            }
            else if (entry.getValue() == most) winners.add(entry.getKey());
        }

        // Print winners:
        if (winners.size() == 1) { // If there's only 1 winner:
            Utils.sendPopupGlobal(
                ChatColor.WHITE + Language.getGameString("winner") + ": ",
                ChatColor.GRAY + winners.get(0).getName() + " (" + ChatColor.GOLD + most + ChatColor.GRAY + ")"
            );
            Utils.messageGlobal("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "----------------------------------");
            Utils.messageGlobal(
                ChatColor.GRAY + " > " +
                ChatColor.WHITE + Language.getGameString("winner") + ": " +
                ChatColor.GRAY + winners.get(0).getName() + " (" +
                ChatColor.GOLD + most + ChatColor.GRAY + ")"
            );
        }
        else {
            StringBuilder s = new StringBuilder();
            for (Player p : winners) {
                s.append(p.getName()).append(", ");
            }
            s.deleteCharAt(s.lastIndexOf(","));
            Utils.sendPopupGlobal(
                ChatColor.WHITE + Language.getGameString("draw") + ": ",
                ChatColor.GRAY + s.toString() + "(" + ChatColor.GOLD + most + ChatColor.GRAY + ")"
            );
            Utils.messageGlobal("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "----------------------------------");
            Utils.messageGlobal(
                ChatColor.GRAY + " > " +
                ChatColor.WHITE + Language.getGameString("draw") + ": " +
                ChatColor.GRAY + s.toString() + " (" +
                ChatColor.GOLD + most + ChatColor.GRAY + ")"
            );
        }
        Utils.messageGlobal("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "----------------------------------");
    }

    /**
     * Update Players Tab List.
     * If a player is alive, his name will be green. Otherwise, it'll be red.
     * To the right of the player's name will be his game points.
     */
    public void updateTabList() {
        if (players.isEmpty()) return;
        players.forEach((player, points) -> {
            Role r = Xinada.getGame().getRound().getCurrentRole(player);
            if (inRound()) {
                if (r.isDead()) {
                    r.getPlayer().setPlayerListName(ChatColor.GRAY + " > " + ChatColor.RED + r.getPlayerName() + ChatColor.GRAY + ": " + ChatColor.GOLD + points + " ");
                } else {
                    r.getPlayer().setPlayerListName(ChatColor.GRAY + " > " + ChatColor.GREEN + r.getPlayerName() + ChatColor.GRAY + ": " + ChatColor.GOLD + points + " ");
                }
            }
            else r.getPlayer().setPlayerListName(ChatColor.GRAY + " > " + ChatColor.GREEN + r.getPlayerName() + ChatColor.GRAY + ": " + ChatColor.GOLD + points + " ");
        });
    }
}
