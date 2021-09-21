package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Messages implements Listener {

    public Messages() {}

    public void onMessage(AsyncPlayerChatEvent event) {
        if (Xinada.inGame() && Xinada.getGame().inRound()) {
            Xinada.getGame().getRound().getCurrentRole(event.getPlayer()).onMessage(event);
        }
    }
}
