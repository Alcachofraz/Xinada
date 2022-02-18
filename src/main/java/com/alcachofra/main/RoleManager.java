package com.alcachofra.main;

import com.alcachofra.roles.bad.*;
import com.alcachofra.roles.good.*;
import com.alcachofra.roles.neutral.*;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public final class RoleManager {

    /**
     * Possible Role draws. All available Roles.
     */
    public enum RoleDraw {
        ACCOMPLICE(Accomplice.class),
        CANNIBAL(Cannibal.class),
        CUPID(Cupid.class),
        DEVIL(Devil.class),
        FLUTIST(Flutist.class),
        MONSTER(Monster.class),
        MURDERER(Murderer.class),
        PYROTECHNIC(Pyrotechnic.class),
        TERRORIST(Terrorist.class),
        TRAITOR(Traitor.class),
        TRICKSTER(Trickster.class),

        ANALYST(Analyst.class),
        ATHLETE(Athlete.class),
        CLOWN(Clown.class),
        COP(Cop.class),
        DOCTOR(Doctor.class),
        ELECTRICIAN(Electrician.class),
        ENGINEER(Engineer.class),
        FISHERMAN(Fisherman.class),
        GURU(Guru.class),
        HUNTER(Hunter.class),
        ILLUSIONIST(Illusionist.class),
        IMMUNE(Immune.class),
        INNOCENT(Innocent.class),
        JESUS(Jesus.class),
        MAGICIAN(Magician.class),
        NEGOTIATOR(Negotiator.class),
        NINJA(Ninja.class),
        PHANTOM(Phantom.class),
        PIRATE(Pirate.class),
        PRIEST(Priest.class),
        PROMOTER(Promoter.class),
        PSYCHIC(Psychic.class),
        SHEEP(Sheep.class),
        SHEPARD(Shepard.class),
        SURVIVOR(Survivor.class),
        WITCH(Witch.class),

        GRAVEDIGGER(Gravedigger.class),
        THIEF(Thief.class);

        public final Class<? extends Role> role;

        RoleDraw(Class<? extends Role> role) {
            this.role = role;
        }

        public Constructor<? extends Role> getConstructor() {
            try {
                return role.getConstructor(Player.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Draw Roles. Almost every Role has the same chance of being drawn. But some don't. There are some restrictions.
     * <p>
     * There's an algorithm to it:
     * <p>
     * <b>Innocent:</b> The Innocent is most probable Role (1/4)
     * <p>
     * <b>Pirate:</b> There always have to be 2 Pirates. That means, in a 3 player game, a Pirate will never be drawn.
     * If a Pirate is drawn, the next draw will forcefully be a Pirate. If there is no next draw, then
     * the previous draw gets replaced by a Pirate.
     * <p>
     * <b>Sheep and Shepard:</b> There always have to be both. The algorithm is similar to that of the Pirate's.
     *
     * @param players Collection of Players to participate.
     * @return Map of Players and their Roles for the current round.
     * @throws IllegalAccessException When an Instance of a Role can't be accessed.
     * @throws InvocationTargetException When a Role Constructor can't be invoked.
     * @throws InstantiationException When an Instance of a Role can't be created.
     */
    public static Map<Player, Role> draw(Collection<Player> players) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        List<RoleDraw> pool = new ArrayList<>(EnumSet.allOf(RoleDraw.class)); // List of possible Roles.
        List<RoleDraw> roleDraws = new ArrayList<>(); // List of Drawn Roles for this round.
        Map<Player, Role> roles = new HashMap<>(); // Map of Players and their Roles for this round.

        final int ROLES_NUM = pool.size();

        // Add Cop and Murderer (required):
        roleDraws.add(RoleDraw.COP);
        roleDraws.add(RoleDraw.MURDERER);

        // Add Innocents to pool (chance of Innocent is 1/4):
        for (int i = 0; i < (ROLES_NUM / 4) - 1; i++) {
            pool.add(RoleDraw.INNOCENT);
        }

        // Shuffle pool:
        Collections.shuffle(pool);

        /*
        List "pool" now holds ROLES_NUM/4 Innocents and 1 of each other Role (except for Murderer and Cop).
        List "roleDraws" now holds Murderer and Cop, because they are required Roles for the game.
        */

        // Draw Roles from pool to roleDraws:
        for (int i = 0; roleDraws.size() < players.size(); i++) { // While size of roleDraws is less than number of Players
            RoleDraw role = pool.get(i); // Role drawn

            switch (role) {
                case COP:
                case MURDERER:
                    break;
                case PIRATE:
                    if (roleDraws.size() + 1 < players.size()) {// If not last draw
                        roleDraws.add(role);
                        roleDraws.add(role);
                    }
                    break;
                case SHEEP:
                case SHEPARD:
                    if (roleDraws.size() + 1 < players.size()) { // If last draw
                        roleDraws.add(RoleDraw.SHEEP);
                        roleDraws.add(RoleDraw.SHEPARD);
                    }
                    break;
                default:
                    roleDraws.add(role);
                    break;
            }
        }

        // Shuffle list of Roles
        Collections.shuffle(roleDraws);

        // Create Roles for players:
        Iterator<RoleDraw> iterator = roleDraws.iterator();
        for (Player player : players) {
            Constructor<? extends Role> constructor = iterator.next().getConstructor();
            roles.put(player, Objects.requireNonNull(constructor).newInstance(player));
        }

        return roles;
    }
}