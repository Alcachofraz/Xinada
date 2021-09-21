package com.alcachofra.roles.good;

import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Sheep extends Role {
    public Sheep(Player player) {
        super(
            player,
            Language.getRoleName("sheep"),
            Language.getRoleDescription("sheep"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        addPoint();
        if (isActivated()) addPoint();
    }

    @Override
    public void initialise() {
        Utils.addItem(getPlayer(), Material.WHITE_WOOL, 8, 1);
        setActivated(true);
        super.initialise();
    }

    @Override
    public void reset() {
        Utils.addItem(getPlayer(), Material.WHITE_WOOL, 8, 1);
        setActivated(true);
        super.reset();
    }
}
