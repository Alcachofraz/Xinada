package com.alcachofra.roles.good;

import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import org.bukkit.entity.Player;

public class Immune extends Role {
    public Immune(Player player) {
        super(
            player,
            Language.getRolesName("immune"),
            Language.getRolesDescription("immune"),
            1
        );
    }
}
