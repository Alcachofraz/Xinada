package com.alcachofra.roles.good;

import com.alcachofra.utils.Utils;
import com.alcachofra.main.Language;
import com.alcachofra.main.Role;
import com.alcachofra.main.Xinada;
import com.alcachofra.roles.bad.Accomplice;
import com.alcachofra.roles.bad.Murderer;
import com.alcachofra.roles.bad.Traitor;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Guru extends Role {
    public Guru(Player player) {
        super(
            player,
            Language.getRolesName("guru"),
            Language.getRolesDescription("guru"),
            1
        );
    }

    @Override
    public void award() {
        addPoint(); // Add 1 point
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Role wwh) {
        if (!isActivated() && !isDead() && !wwh.isDead()) {
            boolean murderer_activated;
            boolean accomplice_activated;
            boolean traitor_activated;

            murderer_activated = Xinada.getGame().getRound().getCurrentRole(Murderer.class).isActivated();

            // Check for Accomplice, and its activity:
            if (Xinada.getGame().getRound().getCurrentRole(Accomplice.class) == null) {
                accomplice_activated = false;
            }
            else accomplice_activated = Xinada.getGame().getRound().getCurrentRole(Accomplice.class).isActivated();

            // Check for Traitor, and its activity:
            if (Xinada.getGame().getRound().getCurrentRole(Traitor.class) == null) {
                traitor_activated = false;
            }
            else traitor_activated = Xinada.getGame().getRound().getCurrentRole(Traitor.class).isActivated();
            if (
                ((wwh instanceof Murderer) && murderer_activated) ||
                ((wwh instanceof Accomplice) && accomplice_activated) ||
                ((wwh instanceof Traitor) && traitor_activated)
            ) {
                wwh.getPlayer().setWalkSpeed(0); // Player whom was hit loses ability to walk
                wwh.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 250)); // Player whom was hit loses ability to jump
                wwh.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 250)); // Player whom was hit loses ability to see
                wwh.getPlayer().getInventory().setHeldItemSlot(0); // Put knife in hand

                Utils.sendPopup(
                    wwh.getPlayer(),
                    ChatColor.RED + Language.getRoleString("84"),
                    ChatColor.RED + Language.getRoleString("85")
                );

                wwh.setExposed(true);

                setPoints(1);

                getPlayer().sendMessage(ChatColor.GREEN + wwh.getPlayerName() + " " + Language.getRoleString("86") + " " + wwh.getRoleName() + ChatColor.RED + ". " + Language.getRoleString("87"));

                Utils.soundLocation(wwh.getPlayer().getLocation(), Sound.ENTITY_WOLF_HOWL);
            }
            else {
                if (wwh instanceof Immune) {
                    getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("2"));
                    wwh.getPlayer().sendMessage(ChatColor.GREEN + getPlayerName() + " " + Language.getRoleString("88") + ", " + ChatColor.GREEN + Language.getRoleString("1"));
                }
                getPlayer().sendMessage(ChatColor.RED + wwh.getPlayerName() + " " + Language.getRoleString("89"));
                wwh.getPlayer().sendMessage(ChatColor.GREEN + getPlayerName() + " " + Language.getRoleString("90"));
            }
            setActivated(true); // Guru can't use his power again
        }
    }
}
