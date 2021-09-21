package com.alcachofra.roles.good;

import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import org.bukkit.entity.Player;

public class Immune extends Role {
    public Immune(Player player) {
        super(
            player,
            Language.getRoleName("immune"),
            Language.getRoleDescription("immune"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        setPoints(2);
    }
}
