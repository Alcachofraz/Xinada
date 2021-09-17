package com.alcachofra.utils;

import com.alcachofra.main.Xinada;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.HashSet;
import java.util.Objects;

public final class Utils {

    /**
     * Get a list of all online Players.
     * @return List of Players.
     */
    public static HashSet<Player> getOnlinePlayers() {
        return new HashSet<>(Bukkit.getOnlinePlayers());
    }

    /**
     * Send a message in chat to all online Players.
     * @param message Message to send.
     */
    public static void messageGlobal(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    /**
     * Play a Sound for all online Players.
     * @param sound Sound to play.
     */
    public static void soundGlobal(Sound sound) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, 0.8f, 0);
        }
    }

    /**
     * Play a Sound for one Player.
     * @param player Player to play the Sound for.
     * @param sound Sound to play.
     */
    public static void soundIndividual(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 0.8f, 0);
    }

    /**
     * Play a Sound in a certain Location.
     * @param location Location to play the Sound at.
     * @param sound Sound to play.
     */
    public static void soundLocation(Location location, Sound sound) {
        Objects.requireNonNull(location.getWorld()).playSound(location, sound, 0.7f, 0);
    }

    /**
     * Send Popup to a certain player with two lines, that last 4 seconds.
     * @param player Player to send Popup to.
     * @param string First line (larger font).
     * @param subString Second line (smaller font).
     */
    public static void sendPopup(Player player, String string, String subString) {
        player.sendTitle(string, subString, 10, 60, 10);
    }

    /**
     * Send Popup to all online players with two lines, that lasts 4 seconds.
     * @param string First line (larger font).
     * @param subString Second line (smaller font).
     */
    public static void sendPopupGlobal(String string, String subString) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.sendTitle(string, subString, 10, 60, 10);
        }
    }

    /**
     * Remove a certain Item from a Player's inventory.
     * @param player Player.
     * @param material Item to be removed.
     */
    public static void removeItem(Player player, Material material) {
        for (int i = 0; i < player.getInventory().getSize(); i++){
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && item.getType().equals(material)) {
                player.getInventory().setItem(i, null);
                player.updateInventory();
            }
        }
    }

    /**
     * Add a certain Item to a Player's inventory.
     * @param player Player.
     * @param material Item to be added.
     * @param slot Slot in which to add the Item (Hot bar: 0-8).
     * @param amount Amount of the Material to add (if Item is stacked).
     */
    public static void addItem(Player player, Material material, int slot, int amount) {
        player.getInventory().setItem(slot, new ItemStack(material, amount));
    }


    public static void fillInventory(Player player, Material material, int amount) {
        for (int i = 0; i < 36; i++) {
            player.getInventory().setItem(i, new ItemStack(material, amount));
        }
        player.getInventory().setItemInOffHand(new ItemStack(material, amount));
    }

    /**
     * Clean a Player. The term "clean" stands for clearing effects,
     * resetting hunger level, restoring health and regaining the ability
     * to see, move and jump.
     * @param player Player to clean.
     */
    public static void cleanPlayer(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) { // Remove all potion effects
            player.removePotionEffect(effect.getType());
        }
        player.setWalkSpeed(0.2f); // Player gains the ability to walk
        player.setFlySpeed(0.2f);
        player.setHealth(20); // Player's health is restored
        player.setFoodLevel(20); // Player's hunger is restored
        player.getInventory().clear(); // Player's inventory is cleared
        player.closeInventory();
        if (player.getGameMode() == GameMode.SPECTATOR) player.setSpectatorTarget(null);
        player.setGameMode(GameMode.SURVIVAL);
    }

    /**
     * Drop an Item at a certain Location.
     * @param item Item to drop. An ItemStack must be instantiated and
     *             passed to this method.
     * @param location Location where item will be dropped at.
     */
    public static void dropItem(ItemStack item, Location location) {
        Objects.requireNonNull(location.getWorld()).dropItem(location, item); // Drop item
    }

    /**
     * Set Player invisible to all other Players in the Server.
     * @param invisible The to be invisible Player.
     */
    public static void setInvisible(Player invisible) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            Objects.requireNonNull(player.getPlayer()).hidePlayer(Xinada.getPlugin(), invisible);
        }
    }

    /**
     * Set Player visible to all other Players in the Server.
     * @param invisible The to be visible Player.
     */
    public static void setVisible(Player invisible) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            Objects.requireNonNull(player.getPlayer()).showPlayer(Xinada.getPlugin(), invisible);
        }
    }
}
