package com.alcachofra.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;

public final class Config {

    private static final ArrayList<FileConfiguration> configs = new ArrayList<>();
    private static Plugin plugin;

    /**
     * Initialise Config with Plugin instance and the File Configuration
     * paths. Loads the Configuration Files. <br> <br>
     * Starts a list of this Plugin's Configuration Files (paths). The order
     * of the paths passed in this constructor is important, since the static
     * method get() has an index parameter. The first path is treated as '0'.
     * @param plugin Plugin instance.
     */
    private static void init(Plugin plugin, String ...paths) {
        Config.plugin = plugin;

        addConfig(paths);
    }

    public static void addConfig(String ...paths) {
        File aux;

        for (String path : paths) {
            aux = new File(plugin.getDataFolder(), path);
            if (!aux.exists()) plugin.saveResource(path, false);
            configs.add(YamlConfiguration.loadConfiguration(aux));
        }
    }

    /**
     * Get this Plugin's Configuration Files.
     * @return List of Configuration Files.
     */
    public static ArrayList<FileConfiguration> getConfigs() {
        return configs;
    }

    /**
     * Get a Configuration File. Order of paths passed in FileManager
     * Constructor is important, and is used here.
     * @param index Index of File Configuration wanted (refer to constructor).
     * @return The Configuration File.
     */
    public static FileConfiguration get(int index) {
        return configs.get(index);
    }

    public static final class Builder {
        Plugin plugin;
        String[] paths;

        /**
         * Set the Plugin instance.
         * @param plugin The Plugin instance.
         * @return the Builder.
         */
        public Config.Builder setPlugin(Plugin plugin) {
            this.plugin = plugin;
            return this;
        }

        /**
         * Set the Plugin instance.
         * @param paths The Plugin instance.
         * @return the Builder.
         */
        public Config.Builder setPaths(String ...paths) {
            this.paths = paths;
            return this;
        }

        /**
         * Build FileManager. Calls FileManager.init() with set Plugin instance
         * and set File Configuration paths.
         * This method must be called before all FileManager static methods.
         */
        public void build() {
            if (plugin == null)
                throw new RuntimeException("Please, set the plugin instance before initializing FileManager.");
            else if (paths == null)
                throw new RuntimeException("Please, set the configuration files path before initializing FileManager.");
            else
                Config.init(plugin, paths);
        }
    }
}
