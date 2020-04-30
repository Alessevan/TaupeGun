package fr.SPFF.TaupeGun.listeners;

import fr.SPFF.TaupeGun.game.PlayerTaupe;
import fr.SPFF.TaupeGun.game.TaupeGunManager;
import fr.SPFF.TaupeGun.game.Teams;
import fr.SPFF.TaupeGun.utils.Message;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerQuitEvent;

class PlayerQuit {

    private final Listening listening;

    PlayerQuit(final Listening listening){
        this.listening = listening;
    }

    void handle(final PlayerQuitEvent e){
        if (this.listening.getMain().getTaupeGunManager().getState().equals(TaupeGunManager.State.WAITING)) {
            if (Teams.getPlayerTeam(e.getPlayer()).isPresent()) {
                Teams.getPlayerTeam(e.getPlayer()).get().getPlayers().remove(e.getPlayer());
            }
            e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', "&3&lTaupe Gun &8&l» &7" + e.getPlayer().getDisplayName() + " a quitté la partie"));
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
                        Message.create("&c&lTaupe Gun &4&l» &cUne équipe est morte !").broadcast();
                        playerTaupe.getTaupe().destroy();
                        PlayerTaupe.getPlayerTaupeList().remove(playerTaupe);
                        playerTaupe.setTeam(null);
                        playerTaupe.setTaupe(null);
                    }
                }
                e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', "&3&lTaupe Gun &8&l» &7" + e.getPlayer().getDisplayName() + " s'est déconnecté."));
                if (playerTaupe.getTeam().getPlayers().size() == 0) {
                    Message.create("&c&lTaupe Gun &4&l» &cUne équipe est morte !").broadcast();
                    playerTaupe.getTeam().destroy();
                    PlayerTaupe.getPlayerTaupeList().remove(playerTaupe);
                    playerTaupe.setTeam(null);
                    playerTaupe.setTaupe(null);
                }
            }
        }
    }
}
