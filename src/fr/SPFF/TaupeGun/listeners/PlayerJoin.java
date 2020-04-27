package fr.SPFF.TaupeGun.listeners;

import fr.SPFF.TaupeGun.game.TaupeGunManager;
import fr.SPFF.TaupeGun.utils.Message;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerJoinEvent;

class PlayerJoin {

    private final Listening listening;

    PlayerJoin(final Listening listening) {
        this.listening = listening;
    }

    void handle(final PlayerJoinEvent e){
        if(!this.listening.getMain().getTaupeGunManager().getState().equals(TaupeGunManager.State.WAITING)){
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            e.getPlayer().teleport((Location) listening.getMain().getFileManager().getFile("data").get("spawn"));
            Message.create("&c&lTaupe Gun &4&l» &cLa partie a déjà commencé. Vous êtes spectateur.").sendMessage(e.getPlayer());
            return;
        }
        e.getPlayer().setGameMode(GameMode.ADVENTURE);
        if(listening.getMain().getFileManager().getFile("data").get("spawn") == null){
            if(e.getPlayer().isOp()){
                Message.create("&c&lTaupe Gun &4&l» &cLe spawn n'est pas défini !").sendMessage(e.getPlayer());
            }
            return;
        }
        e.getPlayer().teleport((Location) listening.getMain().getFileManager().getFile("data").get("spawn"));
    }
}
