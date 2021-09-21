package com.alcachofra.main;

import com.alcachofra.utils.Language;
import com.alcachofra.utils.Utils;
import com.alcachofra.utils.WorldManager;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Game {
    private final HashMap<Player, Integer> players = new HashMap<>();
    private final int rounds;
    private int roundID = 0;
    private Round round;
    private boolean inRound = false;
    private boolean inGame = false;

    /**
     * Constructor for Game. Takes a set of Players and the number of rounds to play.
     * @param players Set of Players.
     * @param rounds Number of Rounds to play in this Game.
     */
    public Game(Set<Player> players, int rounds) {
        for (Player player : players) {
            this.players.put(player, 0);
        }
        this.rounds = rounds;
    }

    /**
     * Start game.
     */
    public void start() {
        inGame = true;
        nextRound();
    }


    /**
     * End Game and output player results.
     * @param outputResults If true, results won't be output.
     */
    public void end(boolean outputResults) {
        if (outputResults) outputWinner();

        Utils.soundGlobal(Sound.BLOCK_NOTE_BLOCK_PLING);
        Utils.messageGlobal(Xinada.getTag() + Language.getString("gameEnded"));

        players.keySet().forEach(WorldManager::lobby);
        inGame = false;
    }

    /**
     * End round. Calls Round.end(), updates Tab List and takes 1 of 4 actions:
     * In case the round ended due to issue of the command "/end", Game also ends.
     * In case there not enough players (because someone left), Game also ends.
     * In case of this being the last round, Game also ends.
     * Otherwise, a countdown of 5 seconds starts and the next rounds begins.
     * @param cause Cause of Round end.
     */
    public void endRound(Round.EndCause cause) {
        inRound = false;
        round.end(cause);
        updateTabList();

        new BukkitRunnable() {
            public void run() {
                if (cause == Round.EndCause.FORCED_GAME_END) {
                    end(false);
                }
                else if (players.keySet().size() < 3) {
                    Utils.messageGlobal(Xinada.getTag() + Language.getString("notEnoughPlayers"));
                    end(false);
                }
                else if (roundID >= rounds) {
                    end(true);
                }
                else {
                    nextRound();
                }
            }
        }.runTaskLater(Xinada.getPlugin(), 20*3); // 20 ticks = 1 second
    }

    /**
     * Create next Round instance, and start it. Then updates Tab List.
     * If Round couldn't be instantiated, Exception is thrown and Game
     * ends.
     */
    private void nextRound() {
        RoundCountdown countdown = new RoundCountdown(
                5,
                1,
                1,
                roundID + 1,
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            round = new Round(++roundID, players.keySet());
                            round.start();
                            updateTabList();
                            inRound = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            end(false);
                        }
                    }
                }
        );
        countdown.setMessage(
                Xinada.getTag() + Language.getString("roundStartingIn")
        ).start();
    }

    /**
     * Check if the Game has started.
     * @return True if the Game has started, false otherwise.
     */
    public boolean inGame() {
        return this.inGame;
    }

    /**
     * Check if a Round has started.
     * @return True if a Round has started, false otherwise.
     */
    public boolean inRound() {
        return this.inRound;
    }

    /**
     * Get current Round.
     * @return Current Round. Null if no Round has started.
     */
    public Round getRound() {
        return round;
    }

    /**
     * Get Players and their points.
     * @return Map of Players and their respective points for this Game.
     */
    public HashMap<Player, Integer> getPlayers() {
        return players;
    }

    /**
     * Add points to a Player.
     * @param player Player to add points to.
     * @param points Points to add.
     */
    public void addPoints(Player player, int points) {
        players.put(player, players.get(player) + points);
    }

    /**
     * Remove a player from this game
     * @param p Player to remove
     */
    public void removePlayer(Player p) {
        players.remove(p);
    }

    /**
     * Update Players Tab List.
     * If a player is alive, his name will be green. Otherwise, it'll be red.
     * To the right of the player's name will be his game points.
     */
    public void updateTabList() {
        if (players.isEmpty()) return;
        players.forEach((player, points) -> {
            Role r = round.getCurrentRole(player);
            if (inRound()) {
                if (r.isDead()) {
                    r.getPlayer().setPlayerListName(ChatColor.GRAY + " > " + ChatColor.RED + r.getPlayer().getName() + ChatColor.GRAY + ": " + ChatColor.GOLD + points + " ");
                } else {
                    r.getPlayer().setPlayerListName(ChatColor.GRAY + " > " + ChatColor.GREEN + r.getPlayer().getName() + ChatColor.GRAY + ": " + ChatColor.GOLD + points + " ");
                }
            }
            else r.getPlayer().setPlayerListName(ChatColor.GRAY + " > " + ChatColor.GREEN + r.getPlayer().getName() + ChatColor.GRAY + ": " + ChatColor.GOLD + points + " ");
        });
    }

    /**
     * Output the game winner and his score. If there's a tie, all winners will be
     * output.
     */
    private void outputWinner() {
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
                    ChatColor.WHITE + Language.getString("winner") + ": ",
                    ChatColor.GRAY + winners.get(0).getName() + " (" + ChatColor.GOLD + most + ChatColor.GRAY + ")"
            );
            Utils.messageGlobal("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "----------------------------------");
            Utils.messageGlobal(
                    ChatColor.GRAY + " > " +
                            ChatColor.WHITE + Language.getString("winner") + ": " +
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
                    ChatColor.WHITE + Language.getString("draw") + ": ",
                    ChatColor.GRAY + s.toString() + "(" + ChatColor.GOLD + most + ChatColor.GRAY + ")"
            );
            Utils.messageGlobal("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "----------------------------------");
            Utils.messageGlobal(
                    ChatColor.GRAY + " > " +
                            ChatColor.WHITE + Language.getString("draw") + ": " +
                            ChatColor.GRAY + s.toString() + " (" +
                            ChatColor.GOLD + most + ChatColor.GRAY + ")"
            );
        }
        Utils.messageGlobal("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "----------------------------------");
    }
}
