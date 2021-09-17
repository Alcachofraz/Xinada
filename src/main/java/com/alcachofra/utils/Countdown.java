package com.alcachofra.utils;

import com.alcachofra.main.Language;
import com.alcachofra.main.Xinada;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Countdown {

    private String firstMessage;
    private String message;

    private final BukkitRunnable runnable;
    private final int seconds;
    private final int period;
    private final int delay;

    private BukkitTask countdown;

    /**
     * Countdown constructor.
     * @param seconds How many seconds to countdown.
     * @param period Countdown period (in seconds).
     * @param delay Initial Countdown delay (in seconds).
     * @param runnable run() will be called when countdown comes to an end.
     */
    public Countdown(int seconds, int period, int delay, BukkitRunnable runnable) {
        this.seconds = seconds;
        this.period = period;
        this.delay = delay;
        this.runnable = runnable;
    }

    /**
     * Set first message. This message is output upon start() call.
     * If firstMessage isn't set, nothing is output upon start() call.
     * @param firstMessage First Message.
     */
    public Countdown setFirstMessage(String firstMessage) {
        this.firstMessage = firstMessage;
        return this;
    }

    /**
     * Set message. This message will be output every second. First "%s"
     * will be replaced with remaining seconds. Second "%s" will be
     * replaced with "minutes" or "seconds".
     * @param message Message.
     */
    public Countdown setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * Start this countdown.
     */
    public void start() {
        if (firstMessage != null) Utils.messageGlobal(firstMessage);
        countdown = new BukkitRunnable() {
            private long secsRemaining = seconds;
            public void run() {
                if (secsRemaining > 0) {
                    if (secsRemaining > 60) {
                        if (secsRemaining % 60 == 0) {
                            if (message != null) {
                                Utils.messageGlobal(String.format(message, secsRemaining/60, Language.getRoundString("minutes")));
                                Utils.soundGlobal(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
                            }
                        }
                    }
                    else if (secsRemaining == 60 || secsRemaining == 30 || secsRemaining == 10 || secsRemaining <=5) {
                        if (message != null) {
                            Utils.messageGlobal(String.format(message, secsRemaining, Language.getRoundString("seconds")));
                            Utils.soundGlobal(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
                        }
                    }
                    secsRemaining--;
                }
                else {
                    runnable.run();
                    this.cancel();
                }
            }
        }.runTaskTimer(Xinada.getPlugin(), delay*20, period*20); // 20 ticks = 1 second
    }

    public void cancel() {
        countdown.cancel();
    }
}
