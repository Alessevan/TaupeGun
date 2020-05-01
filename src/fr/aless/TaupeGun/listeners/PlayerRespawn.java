package fr.aless.TaupeGun.listeners;

import fr.aless.TaupeGun.game.PlayerTaupe;
import fr.aless.TaupeGun.game.TaupeGunManager;
import fr.aless.TaupeGun.utils.Message;
import net.minecraft.server.v1_15_R1.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_15_R1.SoundCategory;
import net.minecraft.server.v1_15_R1.SoundEffects;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Random;

class PlayerRespawn {

    private final Listening listening;

    PlayerRespawn(final Listening listening) {
        this.listening = listening;
    }

    void handle(final PlayerRespawnEvent e) {
        this.listening.getMain().getServer().getScheduler().runTaskLater(this.listening.getMain(), () -> {
            final PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupe(e.getPlayer());
            if (playerTaupe == null || this.listening.getMain().getTaupeGunManager().getTimer() > this.listening.getMain().getTaupeGunManager().taupeTime) {
                if (playerTaupe != null) {
                    PlayerTaupe.getPlayerTaupeList().remove(playerTaupe);
                }
                if (!this.listening.getMain().getTaupeGunManager().getState().equals(TaupeGunManager.State.WAITING))
                    e.getPlayer().setGameMode(GameMode.SPECTATOR);
                e.getPlayer().teleport(this.listening.getMain().getFileManager().getFile("data").getLocation("spawn"));
            } else if (this.listening.getMain().getTaupeGunManager().getTimer() < this.listening.getMain().getTaupeGunManager().taupeTime) {
                for (final Player pls : this.listening.getMain().getServer().getOnlinePlayers()) {
                    final PacketPlayOutNamedSoundEffect sound = new PacketPlayOutNamedSoundEffect(SoundEffects.ENTITY_EVOKER_PREPARE_WOLOLO, SoundCategory.BLOCKS, pls.getLocation().getX(), pls.getLocation().getY(), pls.getLocation().getZ(), 5, 1);
                    ((CraftPlayer) pls).getHandle().playerConnection.sendPacket(sound);
                }
                for (final Player pls : this.listening.getMain().getServer().getOnlinePlayers()) {
                    final PacketPlayOutNamedSoundEffect sound = new PacketPlayOutNamedSoundEffect(SoundEffects.ENTITY_EVOKER_PREPARE_WOLOLO, SoundCategory.BLOCKS, pls.getLocation().getX(), pls.getLocation().getY(), pls.getLocation().getZ(), 5, 1);
                    ((CraftPlayer) pls).getHandle().playerConnection.sendPacket(sound);
                }
                for (final Player pls : this.listening.getMain().getServer().getOnlinePlayers()) {
                    final PacketPlayOutNamedSoundEffect sound = new PacketPlayOutNamedSoundEffect(SoundEffects.ENTITY_EVOKER_PREPARE_WOLOLO, SoundCategory.BLOCKS, pls.getLocation().getX(), pls.getLocation().getY(), pls.getLocation().getZ(), 5, 1);
                    ((CraftPlayer) pls).getHandle().playerConnection.sendPacket(sound);
                }
                for (final Player pls : this.listening.getMain().getServer().getOnlinePlayers()) {
                    final PacketPlayOutNamedSoundEffect sound = new PacketPlayOutNamedSoundEffect(SoundEffects.ENTITY_EVOKER_PREPARE_WOLOLO, SoundCategory.BLOCKS, pls.getLocation().getX(), pls.getLocation().getY(), pls.getLocation().getZ(), 5, 1);
                    ((CraftPlayer) pls).getHandle().playerConnection.sendPacket(sound);
                }
                Message.create(this.listening.getMain().getFileManager().getPrefixMessage("revive").replace("%player%", e.getPlayer().getDisplayName())).broadcast();
                final Location location = ((Player) playerTaupe.getTeam().getPlayers().parallelStream().filter(mate -> !mate.equals(playerTaupe.getPlayer())).toArray()[new Random().nextInt(playerTaupe.getTeam().getPlayers().parallelStream().filter(mate -> !mate.equals(playerTaupe.getPlayer())).toArray().length)]).getLocation().clone();
                e.getPlayer().teleport(location);
                playerTaupe.getTeam().show(playerTaupe.getPlayer());
            }
        }, 2L);
    }

}
