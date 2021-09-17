package com.alcachofra.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;

public final class FileManager {
    private static final ArrayList<FileConfiguration> configs = new ArrayList<>();

    /**
     * Initialise FileManager with Plugin instance and the File Configuration
     * paths. Loads the Configuration Files
     * Starts list "configs" (list of this Plugin's Configuration Files) that'll
     * be used by many static methods.
     * @param plugin Plugin instance.
     */
    private static void init(Plugin plugin, String ...paths) {
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

    public static final class Builder {
        Plugin plugin;
        String[] paths;

        /**
         * Set the Plugin instance.
         * @param plugin The Plugin instance.
         * @return the Builder.
         */
        public FileManager.Builder setPlugin(Plugin plugin) {
            this.plugin = plugin;
            return this;
        }

        /**
         * Set the Plugin instance.
         * @param paths The Plugin instance.
         * @return the Builder.
         */
        public FileManager.Builder setPaths(String ...paths) {
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
                FileManager.init(plugin, paths);
        }
    }
}
