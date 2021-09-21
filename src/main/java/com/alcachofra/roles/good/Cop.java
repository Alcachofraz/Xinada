package com.alcachofra.roles.good;

import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import org.bukkit.entity.Player;

public class Cop extends Role {
    public Cop(Player player) {
        super(
            player,
            Language.getRoleName("cop"),
            Language.getRoleDescription("cop"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        setPoints(2);
    }

    @Override
    public void initialise() {
        setCanPickUp(false);
        addBow();
        super.initialise();
    }

    @Override
    public void reset() {
        setCanPickUp(false);
        addBow();
        super.reset();
    }
}
