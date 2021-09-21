package com.alcachofra.roles.good;

import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import org.bukkit.entity.Player;

public class Innocent extends Role {
    public Innocent(Player player) {
        super(
            player,
            Language.getRoleName("innocent"),
            Language.getRoleDescription("innocent"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        setPoints(2);
    }
}
