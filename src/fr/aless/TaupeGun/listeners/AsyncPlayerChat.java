package fr.aless.TaupeGun.listeners;

import fr.aless.TaupeGun.game.PlayerTaupe;
import fr.aless.TaupeGun.game.TaupeGunManager;
import fr.aless.TaupeGun.game.TeamsColor;
import fr.aless.TaupeGun.utils.Message;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

class AsyncPlayerChat {

    private final Listening listening;

    AsyncPlayerChat(final Listening listening) {
        this.listening = listening;
    }

    void handle(final AsyncPlayerChatEvent e) {
        if (!this.listening.getMain().getTaupeGunManager().getState().equals(TaupeGunManager.State.STARTED)) {
            e.setFormat(ChatColor.translateAlternateColorCodes('&', "&7" + e.getPlayer().getDisplayName() + " : &f") + e.getMessage().replace("&", ""));
        } else {
            e.setCancelled(true);
            final PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupe(e.getPlayer());
            if (e.getPlayer().getGameMode().equals(GameMode.SPECTATOR) || playerTaupe == null) {
                final Message message = Message.create("&8&o[Spectator] " + e.getPlayer().getDisplayName() + " : &7&o" + e.getMessage().replace("&", ""));
                for (final Player player : this.listening.getMain().getServer().getOnlinePlayers()) {
                    if (player.getGameMode().equals(GameMode.SPECTATOR))
                        message.sendMessage(player);
                }
                this.listening.getMain().getLogger().info("[Spectator] " + e.getPlayer().getDisplayName() + " : " + e.getMessage().replace("&", ""));
                return;
            } else {
                if (e.getMessage().startsWith("!")) {
                    e.setCancelled(false);
                    e.setFormat(ChatColor.translateAlternateColorCodes('&', (playerTaupe.isTaupe() && playerTaupe.isReveal() ? TeamsColor.getColor(playerTaupe.getTaupe().getColor().getValue()) : TeamsColor.getColor(playerTaupe.getTeam().getColor().getValue())) + (playerTaupe.isTaupe() && playerTaupe.isReveal() ? " [" + playerTaupe.getTaupe().getName() + "]" : "[" + playerTaupe.getTeam().getName() + "]") + " " + e.getPlayer().getDisplayName() + " : &f") + e.getMessage().substring(1).replace("&", ""));
                    return;
                }
                final Message message = Message.create(TeamsColor.getColor(playerTaupe.getTeam().getColor().getValue()) + "[Team] " + e.getPlayer().getDisplayName() + " : &f" + e.getMessage().replace("&", ""));
                for (final PlayerTaupe playerTaupes : PlayerTaupe.getPlayerTaupeList()) {
                    if (playerTaupe.getTeam().equals(playerTaupes.getTeam())) {
                        message.sendMessage(playerTaupes.getPlayer());
                    }
                }
                this.listening.getMain().getLogger().info("[Team] " + e.getPlayer().getDisplayName() + " : " + e.getMessage().replace("&", ""));
            }
        }
    }

}
