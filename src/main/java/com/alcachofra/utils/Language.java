package com.alcachofra.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Random;


public class Language {
    private static FileConfiguration config;

    private static void init(FileConfiguration config) {
        Language.config = config;
    }

    /**
     * Get String in path "strings.<path>".
     * @param path Path.
     * @return String.
     */
    public static String getString(String path) {
        String format = config.getString("strings." + path);
        if (format == null) return "";
        return ChatColor.translateAlternateColorCodes('&', format);
    }

    public static String getRoleName(String path) {
        return config.getString("roles." + path + ".name");
    }

    public static String getRoleDescription(String path) {
        return config.getString("roles." + path + ".description");
    }

    public static String getRandomJoke() {
        Random rand = new Random();
        List<String> jokes = config.getStringList("jokes");
        return jokes.get(rand.nextInt(jokes.size()));
    }

    public static final class Builder {
        FileConfiguration config;

        /**
         * Set the Configuration File where Language will get its String from.
         * @param config The Plugin instance.
         * @return the Builder.
         */
        public Language.Builder setFileConfiguration(FileConfiguration config) {
            this.config = config;
            return this;
        }

        /**
         * Build FileManager. Calls FileManager.init() with set Plugin instance
         * and set File Configuration paths.
         * This method must be called before all FileManager static methods.
         */
        public void build() {
            if (config == null)
                throw new RuntimeException("Please, set the configuration file instance before initializing Language.");
            else
                Language.init(config);
        }
    }
}
