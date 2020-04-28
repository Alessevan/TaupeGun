package fr.SPFF.TaupeGun.listeners;

import fr.SPFF.TaupeGun.game.PlayerTaupe;
import fr.SPFF.TaupeGun.game.TaupeGunManager;
import fr.SPFF.TaupeGun.game.Teams;
import org.bukkit.event.player.PlayerQuitEvent;

class PlayerQuit {

    private final Listening listening;

    PlayerQuit(final Listening listening){
        this.listening = listening;
    }

    void handle(final PlayerQuitEvent e){
        if(this.listening.getMain().getTaupeGunManager().getState().equals(TaupeGunManager.State.WAITING)){
            e.setQuitMessage("&3&lTaupe Gun &8&l» &7" + e.getQuitMessage() + " a quitté la partie");
        }
        else {
            final PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupe(e.getPlayer());
            if (playerTaupe == null) {
                e.setQuitMessage("");
                return;
            } else {
                playerTaupe.getTeam().removePlayer(playerTaupe.getPlayer());
                if (this.listening.getMain().getTaupeGunManager().getTimer() < 10 * 60 * 30) {
                    e.setQuitMessage("&3&lTaupe Gun &8&l» &7" + e.getPlayer().getDisplayName() + " s'est déconnecté mais il peut revenir avant la diffusion de l'ordre de mission des taupes.");
                    return;
                }
                e.setQuitMessage("&3&lTaupe Gun &8&l» &7" + e.getPlayer().getDisplayName() + " s'est déconnecté et ne peut plus se reconnecter");
                if(playerTaupe.getTeam().getPlayers().size() == 0){
                    Teams.getTeams().remove(playerTaupe.getTeam());
                }
                PlayerTaupe.getPlayerTaupeList().remove(playerTaupe);
                playerTaupe.setTeam(null);
                playerTaupe.setTaupe(null);
            }
        }
    }
}
