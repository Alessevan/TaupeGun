package fr.SPFF.TaupeGun.listeners;

import fr.SPFF.TaupeGun.game.PlayerTaupe;
import fr.SPFF.TaupeGun.utils.Message;
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
        final PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupe(e.getPlayer());
        if(PlayerTaupe.getPlayerTaupe(e.getPlayer()) == null) {
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            e.getPlayer().teleport(this.listening.getMain().getFileManager().getFile("data").getLocation("spawn"));
        }
        else {

            this.listening.getMain().getServer().getScheduler().runTaskLater(this.listening.getMain(), () -> {

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
                Message.create("&3&lTaupe Gun &8&l» &7" + e.getPlayer().getDisplayName() + " est revenu d'entre les morts !").broadcast();
                final Location location = ((Player) playerTaupe.getTeam().getPlayers().parallelStream().filter(mate -> !mate.equals(playerTaupe.getPlayer())).toArray()[new Random().nextInt(playerTaupe.getTeam().getPlayers().parallelStream().filter(mate -> !mate.equals(playerTaupe.getPlayer())).toArray().length)]).getLocation().clone();
                e.getPlayer().teleport(location);

            }, 2L);
        }
    }

}
