package com.alcachofra.main;

public class Language {

    public static String getXinadaString(String path) {
        return Xinada.getStringsConfig().getString("strings.xinada." + path);
    }
    public static String getGameString(String path) {
        return Xinada.getStringsConfig().getString("strings.game." + path);
    }
    public static String getRoundString(String path) {
        return Xinada.getStringsConfig().getString("strings.round." + path);
    }
    public static String getRoleString(String path) {
        return Xinada.getStringsConfig().getString("strings.role." + path);
    }
    public static String getRolesName(String role) {
        return Xinada.getStringsConfig().getString("roles." + role + ".name");
    }
    public static String getRolesDescription(String role) {
        return Xinada.getStringsConfig().getString("roles." + role + ".description");
    }
}
