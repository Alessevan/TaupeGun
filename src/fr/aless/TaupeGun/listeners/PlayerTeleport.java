package fr.aless.TaupeGun.listeners;

import org.bukkit.GameRule;
import org.bukkit.event.player.PlayerTeleportEvent;

class PlayerTeleport {

    private final Listening listening;

    PlayerTeleport(final Listening listening) {
        this.listening = listening;
    }

    void handle(final PlayerTeleportEvent e) {
        if (e.getTo() == null)
            return;
        if (e.getTo().getWorld() == null)
            return;
        e.getTo().getWorld().setGameRule(GameRule.NATURAL_REGENERATION, false);
        e.getTo().getWorld().setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        e.getTo().getWorld().getWorldBorder().setSize(this.listening.getMain().getWorld().getWorldBorder().getSize());
    }
}
