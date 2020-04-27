package fr.SPFF.TaupeGun.listeners;

import fr.SPFF.TaupeGun.game.PlayerTaupe;
import fr.SPFF.TaupeGun.game.TaupeGunManager;
import fr.SPFF.TaupeGun.game.Teams;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChat {

    private final Listening listening;

    AsyncPlayerChat(final Listening listening) {
        this.listening = listening;
    }

    void handle(final AsyncPlayerChatEvent e){
        if(this.listening.getMain().getTaupeGunManager().getState().equals(TaupeGunManager.State.WAITING)){
            e.setFormat(ChatColor.translateAlternateColorCodes('&', "&7" + e.getPlayer().getDisplayName() + " : &f") + e.getMessage());
        }
        else {
            e.setCancelled(true);
            if(e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
                for(final Player player : this.listening.getMain().getServer().getOnlinePlayers()){
                    if(player.getGameMode().equals(GameMode.SPECTATOR))
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&o[Spectateur] " + e.getPlayer().getDisplayName() + " : &7&o") + e.getMessage());
                }
                this.listening.getMain().getLogger().info("[Spectateur] " + e.getPlayer().getDisplayName() + " : " + e.getMessage());
                return;
            }
            else {
                if(e.getMessage().startsWith("!")){
                    e.setCancelled(false);
                    final PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupe(e.getPlayer());
                    e.setFormat(ChatColor.translateAlternateColorCodes('&', Teams.getColor(playerTaupe.getTeam().getValue()) + "[Équipe] " + e.getPlayer().getDisplayName() + " : &f") + e.getMessage());
                    return;
                }
                final PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupe(e.getPlayer());
                for(final PlayerTaupe playerTaupes : PlayerTaupe.getPlayerTaupeList()){
                    if(playerTaupe.getTeam().equals(playerTaupes.getTeam())){
                        playerTaupes.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Teams.getColor(playerTaupe.getTeam().getValue()) + "[Équipe] " + e.getPlayer().getDisplayName() + " : &f") + e.getMessage());
                    }
                }
                this.listening.getMain().getLogger().info("[Équipe] " + e.getPlayer().getDisplayName() + " : " + e.getMessage());
            }
        }
    }

}
