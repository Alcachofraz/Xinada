package com.alcachofra.roles.good;

import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import org.bukkit.entity.Player;

public class Innocent extends Role {
    public Innocent(Player player) {
        super(
            player,
            Language.getRolesName("innocent"),
            Language.getRolesDescription("innocent"),
            1
        );
    }
}
