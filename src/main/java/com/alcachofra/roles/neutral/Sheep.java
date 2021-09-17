package com.alcachofra.roles.neutral;

import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Sheep extends Role {
    public Sheep(Player player) {
        super(
            player,
            Language.getRolesName("sheep"),
            Language.getRolesDescription("sheep"),
            0
        );
    }

    @Override
    public void initialise() {
        Utils.addItem(getPlayer(), Material.WHITE_WOOL, 8, 1);
        super.initialise();
    }

    @Override
    public void reset() {
        Utils.addItem(getPlayer(), Material.WHITE_WOOL, 8, 1);
        super.reset();
    }

    @Override
    public void award() {
        // No points (neutral)
    }
}
