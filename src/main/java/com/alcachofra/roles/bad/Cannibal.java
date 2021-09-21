package com.alcachofra.roles.bad;

import com.alcachofra.main.Role;
import com.alcachofra.utils.Language;
import org.bukkit.entity.Player;

public class Cannibal extends Role {

    public Cannibal(Player player) {
        super(
            player,
            Language.getRoleName("cannibal"),
            Language.getRoleDescription("cannibal"),
            Side.BAD
        );
    }

    @Override
    public void award() {
        setPoints(2);
    }

    @Override
    public void initialise() {
        setCanPickUp(false);
        super.initialise();
    }

    @Override
    public void reset() {
        setCanPickUp(false);
        super.reset();
    }
}
