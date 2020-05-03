package fr.aless.TaupeGun.listeners;

import fr.aless.TaupeGun.utils.Message;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

class PlayerCommandPreprocess {

    private final Listening listening;

    PlayerCommandPreprocess(final Listening listening){
        this.listening = listening;
    }

    void handle(final PlayerCommandPreprocessEvent e){
        if(!e.getMessage().toLowerCase().startsWith("taupegun") && !e.getMessage().toLowerCase().startsWith("tg")){
            e.setCancelled(true);
            Message.create(this.listening.getMain().getFileManager().getPrefixError("command")).sendMessage(e.getPlayer());
        }
    }
}
