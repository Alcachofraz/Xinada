package com.alcachofra.roles.good;

import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import org.bukkit.entity.Player;

public class Cop extends Role {
    public Cop(Player player) {
        super(
            player,
            Language.getRolesName("cop"),
            Language.getRolesDescription("cop"),
            1
        );
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
