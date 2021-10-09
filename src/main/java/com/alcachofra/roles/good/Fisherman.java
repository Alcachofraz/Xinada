package com.alcachofra.roles.good;

import com.alcachofra.main.Xinada;
import com.alcachofra.utils.Config;
import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Fisherman extends Role {

    public Fisherman(Player player) {
        super(
            player,
            Language.getRoleName("fisherman"),
            Language.getRoleDescription("fisherman"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        setPoints(2);
    }

    @Override
    public void initialise() {
        Utils.addItem(getPlayer(), Material.FISHING_ROD, 8, 1);
        super.initialise();
    }

    @Override
    public void reset() {
        Utils.addItem(getPlayer(), Material.FISHING_ROD, 8, 1);
        super.reset();
    }

    @Override
    public void onFish(ProjectileHitEvent event, Role fished) {
        if (!isDead() && !fished.isDead()) { // If neither of them are dead...
            if (fished instanceof Immune) {
                getPlayer().sendMessage(String.format(Language.getString("triedToFishYou"), fished.getPlayer().getName()) + ", " + Language.getString("butImmune"));
                fished.getPlayer().sendMessage(String.format(Language.getString("isImmune"), getPlayer().getName()));
            }
            else {
                FishHook hook = (FishHook) event.getEntity();

                Vector v = fished.getPlayer().getLocation().toVector()
                        .subtract(hook.getLocation().toVector())
                        .setY(0)
                        .normalize()
                        .multiply(Config.get(Xinada.GAME).getInt("fisherKnockback"))
                        .setY(0.2);

                fished.getPlayer().setVelocity(v);
                hook.remove();

                ItemStack rod;
                if (!getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) rod = getPlayer().getInventory().getItemInMainHand();
                else rod = getPlayer().getInventory().getItemInOffHand();

                rod.setDurability((short) (rod.getDurability() + Material.FISHING_ROD.getMaxDurability() / Config.get(Xinada.GAME).getInt("fisherTimes")));
                if (rod.getDurability() > Material.FISHING_ROD.getMaxDurability()) {
                    Utils.soundIndividual(getPlayer(), Sound.ENTITY_ITEM_BREAK);
                    Utils.removeItem(getPlayer(), Material.FISHING_ROD);
                }
            }
        }
    }
}
