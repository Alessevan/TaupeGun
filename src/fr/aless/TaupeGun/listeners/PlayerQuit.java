package fr.aless.TaupeGun.listeners;

import fr.aless.TaupeGun.game.PlayerTaupe;
import fr.aless.TaupeGun.game.TaupeGunManager;
import fr.aless.TaupeGun.game.Teams;
import fr.aless.TaupeGun.utils.Message;
import org.bukkit.event.player.PlayerQuitEvent;

class PlayerQuit {

    private final Listening listening;

    PlayerQuit(final Listening listening) {
        this.listening = listening;
    }

    void handle(final PlayerQuitEvent e) {
        if (this.listening.getMain().getTaupeGunManager().getState().equals(TaupeGunManager.State.WAITING)) {
            if (Teams.getPlayerTeam(e.getPlayer()).isPresent()) {
                Teams.getPlayerTeam(e.getPlayer()).get().getPlayers().remove(e.getPlayer());
            }
            e.setQuitMessage(this.listening.getMain().getFileManager().getPrefixMessage("left").replace("%player%", e.getPlayer().getDisplayName()));
        } else {
            final PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupe(e.getPlayer());
            if (playerTaupe == null) {
                e.setQuitMessage("");
                return;
            } else {
                playerTaupe.getTeam().removePlayer(playerTaupe.getPlayer());
                if (playerTaupe.isTaupe()) {
                    playerTaupe.getTaupe().removePlayer(playerTaupe.getPlayer());
                    if (playerTaupe.getTaupe().getPlayers().size() == 0) {
                        Message.create(this.listening.getMain().getFileManager().getPrefixWarn("warn.team")).broadcast();
                        playerTaupe.getTaupe().destroy();
                        PlayerTaupe.getPlayerTaupeList().remove(playerTaupe);
                        playerTaupe.setTeam(null);
                        playerTaupe.setTaupe(null);
                    }
                }
                e.setQuitMessage(this.listening.getMain().getFileManager().getPrefixMessage("left").replace("%player%", e.getPlayer().getDisplayName()));
                if (playerTaupe.getTeam().getPlayers().size() == 0) {
                    Message.create(this.listening.getMain().getFileManager().getPrefixWarn("warn.team")).broadcast();
                    playerTaupe.getTeam().destroy();
                    PlayerTaupe.getPlayerTaupeList().remove(playerTaupe);
                    playerTaupe.setTeam(null);
                    playerTaupe.setTaupe(null);
                }
            }
        }
    }
}
