package com.alcachofra.utils;

import com.alcachofra.main.Xinada;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class WorldManager {

    private static World world;

    /**
     * Initialise WorldManager with Plugin instance and the Name of the World.
     * World is set to peaceful and clean() is called.
     * Starts attribute "world" that'll be used by many static methods.
     * @param plugin Plugin instance.
     */
    private static void init(Plugin plugin, String worldName) {
        world = plugin.getServer().getWorld(worldName);
        if (world == null)
            throw new RuntimeException("No such world with name \"" + worldName + "\".");
        else {
            world.setDifficulty(Difficulty.PEACEFUL);
            clean();
        }
    }

    /**
     * Get World.
     * @return World.
     */
    public static World getWorld() {
        return world;
    }

    /**
     * Teleport player to Lobby.
     * @param player Player to teleport.
     */
    public static void lobby(Player player) {
        player.teleport(
                new Location(
                        player.getWorld(),
                        Config.get(Xinada.WORLD).getDouble("lobby.x"),
                        Config.get(Xinada.WORLD).getDouble("lobby.y"),
                        Config.get(Xinada.WORLD).getDouble("lobby.z")
                )
        );
    }

    /**
     * Clean World. This method will remove all item entities.
     */
    public static void clean() {
        for (Entity e : world.getEntities())
            if (e instanceof Item) e.remove();
    }

    public static final class Builder {
        Plugin plugin;
        String worldName;

        /**
         * Set the Plugin instance.
         * @param plugin The Plugin instance.
         * @return the Builder.
         */
        public Builder setPlugin(Plugin plugin) {
            this.plugin = plugin;
            return this;
        }

        /**
         * Set the name of the World WorldManager will operate with. Defaults to "Xinada".
         * @param worldName Name of the world.
         * @return the Builder.
         */
        public Builder setWorldName(String worldName) {
            this.worldName = worldName;
            return this;
        }

        /**
         * Build WorldManager. Calls WorldManager.init() with set Plugin instance.
         * This method must be called before all WorldManager static methods.
         */
        public void build() {
            if (plugin == null)
                throw new RuntimeException("Please, set the plugin instance before initializing WorldManager.");
            else
                WorldManager.init(plugin, worldName == null ? "Xinada" : worldName);
        }
    }
}
