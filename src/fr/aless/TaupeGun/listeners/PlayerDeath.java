package fr.aless.TaupeGun.listeners;

import fr.aless.TaupeGun.game.PlayerTaupe;
import fr.aless.TaupeGun.utils.Message;
import net.minecraft.server.v1_15_R1.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_15_R1.SoundCategory;
import net.minecraft.server.v1_15_R1.SoundEffects;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

class PlayerDeath {

    private final Listening listening;

    PlayerDeath(final Listening listening) {
        this.listening = listening;
    }

    void handle(final PlayerDeathEvent e) {
        final PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupe(e.getEntity());
        e.setDeathMessage("");
        if (playerTaupe == null) return;
        if (e.getEntity().getKiller() != null)
            Message.create(this.listening.getMain().getFileManager().getPrefixWarn("killed.player").replace("%victim%", e.getEntity().getName()).replace("%killer%", e.getEntity().getKiller().getName())).broadcast();
        else
            Message.create(this.listening.getMain().getFileManager().getPrefixWarn("killed.mysteriously").replace("%victim%", e.getEntity().getName())).broadcast();
        for (final Player pls : this.listening.getMain().getServer().getOnlinePlayers()) {
            final PacketPlayOutNamedSoundEffect sound = new PacketPlayOutNamedSoundEffect(SoundEffects.ENTITY_WITHER_SPAWN, SoundCategory.BLOCKS, pls.getLocation().getX(), pls.getLocation().getY(), pls.getLocation().getZ(), 1, 1);
            ((CraftPlayer) pls).getHandle().playerConnection.sendPacket(sound);
        }
        playerTaupe.getTeam().removePlayer(playerTaupe.getPlayer());
        if (playerTaupe.isTaupe()) {
            playerTaupe.getTaupe().removePlayer(playerTaupe.getPlayer());
        }
        this.listening.getMain().getServer().getScheduler().scheduleSyncDelayedTask(this.listening.getMain(), () -> {
            e.getEntity().spigot().respawn();
        }, 2L);
        if (playerTaupe.getTeam().getPlayers().size() == 0 && !playerTaupe.isTaupe()) {
            Message.create(this.listening.getMain().getFileManager().getPrefixWarn("warn.team")).broadcast();
            playerTaupe.getTeam().destroy();
            PlayerTaupe.getPlayerTaupeList().remove(playerTaupe);
            playerTaupe.setTaupe(null);
            playerTaupe.setTeam(null);
            return;
        }
        if (playerTaupe.isTaupe() && playerTaupe.getTaupe().getPlayers().size() > 0) {
            playerTaupe.getTaupe().removePlayer(playerTaupe.getPlayer());
            PlayerTaupe.getPlayerTaupeList().remove(playerTaupe);
            playerTaupe.setTaupe(null);
            playerTaupe.setTeam(null);
            return;
        } else if (playerTaupe.isTaupe() && playerTaupe.getTaupe().getPlayers().size() == 0) {
            Message.create(this.listening.getMain().getFileManager().getPrefixWarn("warn.team")).broadcast();
            playerTaupe.getTaupe().removePlayer(playerTaupe.getPlayer());
            playerTaupe.getTaupe().destroy();
            PlayerTaupe.getPlayerTaupeList().remove(playerTaupe);
            playerTaupe.setTaupe(null);
            playerTaupe.setTeam(null);
            return;
        }
        if (this.listening.getMain().getTaupeGunManager().getTimer() > this.listening.getMain().getTaupeGunManager().taupeTime) {
            PlayerTaupe.getPlayerTaupeList().remove(playerTaupe);
            playerTaupe.setTaupe(null);
            playerTaupe.setTeam(null);

            return;
        }
        playerTaupe.getTeam().addPlayer(playerTaupe.getPlayer());
        if (playerTaupe.isTaupe()) {
            playerTaupe.getTaupe().addPlayer(playerTaupe.getPlayer());
        }
    }
}
