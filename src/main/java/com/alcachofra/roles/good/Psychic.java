package com.alcachofra.roles.good;

import com.alcachofra.utils.Utils;
import com.alcachofra.utils.Language;
import com.alcachofra.main.Role;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Psychic extends Role {
    private Location psychic_location = null; // Location saved by Psychic

    public Psychic(Player player) {
        super(
            player,
            Language.getRoleName("psychic"),
            Language.getRoleDescription("psychic"),
            Side.GOOD
        );
    }

    @Override
    public void award() {
        setPoints(2);
    }

    public Location getPsychicLocation() {
        return psychic_location;
    }

    public void setPsychicLocation(Location psychic_location) {
        this.psychic_location = psychic_location;
    }

    @Override
    public void initialise() {
        Utils.addItem(getPlayer(), Material.SPIDER_EYE, 8, 1);
        super.initialise();
    }

    @Override
    public void reset() {
        Utils.addItem(getPlayer(), Material.SPIDER_EYE, 8, 1);
        psychic_location = null;
        super.reset();
    }

    @Override
    public void onInteract(PlayerInteractEvent event, Action action) {
        super.onInteract(event, action);
        if (!isDead()) {
            if (getPlayer().getInventory().getItemInMainHand().getType().equals(Material.SPIDER_EYE) ||
                    getPlayer().getInventory().getItemInOffHand().getType().equals(Material.SPIDER_EYE)) {
                if (!isActivated()) {
                    if (action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.LEFT_CLICK_AIR)) {
                        setPsychicLocation(getPlayer().getLocation());
                        getPlayer().sendMessage(Language.getString("locationSaved"));
                        Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG);
                    }
                    if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) {
                        if (getPsychicLocation() == null) {
                            getPlayer().sendMessage(Language.getString("mustSaveLocation"));
                            return;
                        }
                        getPlayer().teleport(getPsychicLocation());
                        setActivated(true);

                        getPlayer().sendMessage(Language.getString("teleported"));
                        Utils.soundLocation(getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT);

                        // Remove eye from Psychic
                        Utils.removeItem(getPlayer(), Material.SPIDER_EYE);
                    }
                }
            }
        }
    }
}
