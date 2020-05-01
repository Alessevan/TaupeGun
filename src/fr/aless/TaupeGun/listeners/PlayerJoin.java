package fr.aless.TaupeGun.listeners;

import fr.aless.TaupeGun.game.PlayerTaupe;
import fr.aless.TaupeGun.game.TaupeGunManager;
import fr.aless.TaupeGun.utils.Message;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerJoinEvent;

class PlayerJoin {

    private final Listening listening;

    PlayerJoin(final Listening listening) {
        this.listening = listening;
    }

    void handle(final PlayerJoinEvent e) {
        if (this.listening.getMain().getTaupeGunManager().getState().equals(TaupeGunManager.State.STARTED)) {
            e.setJoinMessage("");
            PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupe(e.getPlayer());
            if (playerTaupe != null) {
                if ((this.listening.getMain().getTaupeGunManager().getTimer() < this.listening.getMain().getTaupeGunManager().taupeTime || (playerTaupe.getTeam().getPlayers().size() >= 1))) {
                    e.setJoinMessage(this.listening.getMain().getFileManager().getPrefixMessage("join").replace("%player%", e.getPlayer().getName()));
                    playerTaupe.setPlayer(e.getPlayer());
                    if (!playerTaupe.isTaupe())
                        playerTaupe.getTeam().getPlayers().add(playerTaupe.getPlayer());
                    playerTaupe.getTeam().show(playerTaupe.getPlayer());
                    if (playerTaupe.isTaupe()) {
                        playerTaupe.getTaupe().addPlayer(playerTaupe.getPlayer());
                        if (playerTaupe.isReveal()) {
                            playerTaupe.getTaupe().show(playerTaupe.getPlayer());
                        } else {
                            playerTaupe.getTeam().show(playerTaupe.getPlayer());
                        }
                    }
                    return;
                }
            }
            e.getPlayer().getInventory().clear();
            e.setJoinMessage("");
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            e.getPlayer().teleport((Location) listening.getMain().getFileManager().getFile("data").get("spawn"));
            Message.create(this.listening.getMain().getFileManager().getPrefixError("alreadyStart")).sendMessage(e.getPlayer());
            return;
        }
        this.listening.getMain().getTaupeGunManager().getPlayerConsumer().accept(e.getPlayer());
        e.setJoinMessage(this.listening.getMain().getFileManager().getPrefixMessage("join").replace("%player%", e.getPlayer().getName()));
        if (listening.getMain().getFileManager().getFile("data").get("spawn") == null) {
            if (e.getPlayer().isOp()) {
                Message.create(this.listening.getMain().getFileManager().getPrefixError("spawn")).sendMessage(e.getPlayer());
            }
            return;
        }
        e.getPlayer().teleport((Location) listening.getMain().getFileManager().getFile("data").get("spawn"));
    }
}
