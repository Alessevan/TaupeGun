package fr.SPFF.TaupeGun.listeners;

import fr.SPFF.TaupeGun.game.PlayerTaupe;
import fr.SPFF.TaupeGun.game.TaupeGunManager;
import fr.SPFF.TaupeGun.utils.Message;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;

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
                    e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', "&3&lTaupe Gun &8&l» &7" + e.getPlayer().getDisplayName() + " a rejoint la partie."));
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
            Message.create("&c&lTaupe Gun &4&l» &cLa partie a déjà commencé. Vous êtes spectateur.").sendMessage(e.getPlayer());
            return;
        }
        e.getPlayer().setTotalExperience(0);
        e.getPlayer().setFoodLevel(20);
        e.getPlayer().setHealth(20);
        e.getPlayer().getInventory().clear();
        e.getPlayer().getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        for (PotionEffect potionEffectType : e.getPlayer().getActivePotionEffects()) {
            e.getPlayer().removePotionEffect(potionEffectType.getType());
        }
        e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', "&3&lTaupe Gun &8&l» &7" + e.getPlayer().getDisplayName() + " a rejoint la partie."));
        e.getPlayer().setGameMode(GameMode.ADVENTURE);
        if (listening.getMain().getFileManager().getFile("data").get("spawn") == null) {
            if (e.getPlayer().isOp()) {
                Message.create("&c&lTaupe Gun &4&l» &cLe spawn n'est pas défini !").sendMessage(e.getPlayer());
            }
            return;
        }
        e.getPlayer().teleport((Location) listening.getMain().getFileManager().getFile("data").get("spawn"));
    }
}
