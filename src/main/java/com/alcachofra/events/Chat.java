package com.alcachofra.events;

import com.alcachofra.main.Xinada;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Chat implements Listener {

    public Chat() {}

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (Xinada.inGame() && Xinada.getGame().inRound()) {
            Xinada.getGame().getRound().getCurrentRole(event.getPlayer()).onChat(event);
        }
    }
}
