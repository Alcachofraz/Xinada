package com.alcachofra.roles.good;

import com.alcachofra.utils.Config;
import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Objects;

public class Hunter extends Role {
    public ArrayList<Location> traps = new ArrayList<>(); // Location of all set traps
    private int trap_num = 0;

    public Hunter(Player player) {
        super(
            player,
            Language.getRoleName("hunter"),
            Language.getRoleDescription("hunter"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        setPoints(2);
    }

    public int getTrapNum() {
        return trap_num;
    }

    public void setTrapNum(int trap_num) {
        this.trap_num = trap_num;
    }

    @Override
    public void initialise() {
        Utils.addItem(getPlayer(), Material.COBWEB, 8, 3);
        super.initialise();
    }

    @Override
    public void reset() {
        Utils.addItem(getPlayer(), Material.COBWEB, 8, 3);
        setTrapNum(0);
        super.reset();
    }

    @Override
    public void clean() {
        for (Location l : traps) l.getBlock().setType(Material.AIR);
        super.clean();
    }

    @Override
    public void onInteract(PlayerInteractEvent event, Action action) {
        super.onInteract(event, action);
        if (!isDead()) {
            if (getPlayer().getInventory().getItemInMainHand().getType().equals(Material.COBWEB) ||
                    getPlayer().getInventory().getItemInOffHand().getType().equals(Material.COBWEB)) {
                if (action.equals(Action.RIGHT_CLICK_BLOCK) && (getTrapNum() < Config.get(Xinada.GAME).getInt("trapNum"))) {
                    Location loc = Objects.requireNonNull(event.getClickedBlock()).getLocation();
                    loc.setY(loc.getY() + 1); // Trap location is above selected block

                    if (loc.getBlock().getType().equals(Material.AIR)) { // Validate trap location
                        getPlayer().getInventory().removeItem(new ItemStack(Material.COBWEB, 1)); // Subtract item in hand
                        loc.getBlock().setType(Material.COBWEB);

                        traps.add(loc);
                        setTrapNum(getTrapNum() + 1);

                        Utils.soundLocation(loc, Sound.BLOCK_ANVIL_LAND);
                    }
                    else getPlayer().sendMessage(Language.getString("trapNotAllowedHere"));
                }
            }
        }
    }
}
