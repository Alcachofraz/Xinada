package com.alcachofra.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Random;


public class Language {
    private static String key;
    private static Map<String, FileConfiguration> configs;

    private static void init(Map<String, FileConfiguration> configs) {
        Language.configs = configs;
        Language.key = configs.keySet().iterator().next();
    }

    /**
     * Set key language.
     * @param key If the file you want to use is "stringsPT.yml", then key
     *            should be "PT".
     * @return If language key is invalid, returns false.
     */
    public static boolean setKey(String key) {
        if (configs.containsKey(key)) {
            Language.key = key;
            return true;
        }
        return false;
    }

    /**
     * Get String in path "strings.<path>".
     * @param path Path.
     * @return String.
     */
    public static String getString(String path) {
        String format = configs.get(key).getString("strings." + path);
        if (format == null) return "";
        return ChatColor.translateAlternateColorCodes('&', format);
    }

    public static String getRoleName(String path) {
        return configs.get(key).getString("roles." + path + ".name");
    }

    public static String getRoleDescription(String path) {
        return configs.get(key).getString("roles." + path + ".description");
    }

    public static String getRandomJoke() {
        Random rand = new Random();
        List<String> jokes = configs.get(key).getStringList("jokes");
        return jokes.get(rand.nextInt(jokes.size()));
    }

    public static final class Builder {
        Map<String, FileConfiguration> configs;

        /**
         * Set the Configuration Files where Language will get its String from.
         * @param configs Configuration Files.
         * @return the Builder.
         */
        public Language.Builder setConfigurationFiles(Map<String, FileConfiguration> configs) {
            this.configs = configs;
            return this;
        }

        /**
         * Build FileManager. Calls FileManager.init() with set Plugin instance
         * and set File Configuration paths.
         * This method must be called before all FileManager static methods.
         */
        public void build() {
            if (configs == null || configs.isEmpty())
                throw new RuntimeException("Please, set the configuration file instance before initializing Language.");
            else
                Language.init(configs);
        }
    }
}
