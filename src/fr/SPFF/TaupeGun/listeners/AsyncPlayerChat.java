package fr.SPFF.TaupeGun.listeners;

import fr.SPFF.TaupeGun.game.PlayerTaupe;
import fr.SPFF.TaupeGun.game.TaupeGunManager;
import fr.SPFF.TaupeGun.game.Teams;
import fr.SPFF.TaupeGun.utils.Message;
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
            e.setFormat(ChatColor.translateAlternateColorCodes('&', "&7" + e.getPlayer().getDisplayName() + " : &f") + e.getMessage().replace("&", ""));
        }
        else {
            e.setCancelled(true);
            if(e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
                final Message message = Message.create("&8&o[Spectateur] " + e.getPlayer().getDisplayName() + " : &7&o" + e.getMessage().replace("&", ""));
                for(final Player player : this.listening.getMain().getServer().getOnlinePlayers()){
                    if(player.getGameMode().equals(GameMode.SPECTATOR))
                        message.sendMessage(player);
                }
                this.listening.getMain().getLogger().info("[Spectateur] " + e.getPlayer().getDisplayName() + " : " + e.getMessage().replace("&", ""));
                return;
            }
            else {
                if(e.getMessage().startsWith("!")){
                    e.setCancelled(false);
                    final PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupe(e.getPlayer());
                    e.setFormat(ChatColor.translateAlternateColorCodes('&', Teams.getPlayerTeam(playerTaupe).getColor().getValue() + "[Équipe] " + e.getPlayer().getDisplayName() + " : &f") + e.getMessage().replace("&", ""));
                    return;
                }
                final PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupe(e.getPlayer());
                final Message message = Message.create(Teams.getPlayerTeam(playerTaupe).getColor().getValue() + "[Équipe] " + e.getPlayer().getDisplayName() + " : &f" + e.getMessage().replace("&", ""));
                for(final PlayerTaupe playerTaupes : PlayerTaupe.getPlayerTaupeList()){
                    if(playerTaupe.getTeam().equals(playerTaupes.getTeam())){
                        message.sendMessage(playerTaupes.getPlayer());
                    }
                }
                this.listening.getMain().getLogger().info("[Équipe] " + e.getPlayer().getDisplayName() + " : " + e.getMessage().replace("&", ""));
            }
        }
    }

}
