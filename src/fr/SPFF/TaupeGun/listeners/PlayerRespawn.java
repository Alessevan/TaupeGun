package fr.SPFF.TaupeGun.listeners;

import fr.SPFF.TaupeGun.game.PlayerTaupe;
import net.minecraft.server.v1_15_R1.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_15_R1.SoundCategory;
import net.minecraft.server.v1_15_R1.SoundEffects;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;

class PlayerRespawn {

    private final Listening listening;

    PlayerRespawn(final Listening listening){
        this.listening = listening;
    }

    void handle(final PlayerRespawnEvent e){
        final PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupe(e.getPlayer());
        if(PlayerTaupe.getPlayerTaupe(e.getPlayer()) == null) {
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            e.getPlayer().teleport(this.listening.getMain().getFileManager().getFile("data").getLocation("spawn"));
        }
        else {
            for(final Player pls : this.listening.getMain().getServer().getOnlinePlayers()){
                final PacketPlayOutNamedSoundEffect sound = new PacketPlayOutNamedSoundEffect(SoundEffects.ENTITY_WITHER_SPAWN, SoundCategory.BLOCKS, pls.getLocation().getX(), pls.getLocation().getY(), pls.getLocation().getZ(), 1, 1);
                ((CraftPlayer) pls).getHandle().playerConnection.sendPacket(sound);
            }
            final Location location = PlayerTaupe.getPlayerTaupeList().parallelStream().filter(playerTaupe1 -> playerTaupe1.getTeam().equals(playerTaupe.getTeam())).findAny().get().getPlayer().getLocation().clone();
            e.getPlayer().teleport(location);
        }
    }

}
