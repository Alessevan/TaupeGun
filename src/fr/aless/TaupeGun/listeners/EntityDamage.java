package fr.aless.TaupeGun.listeners;

import fr.aless.TaupeGun.game.TaupeGunManager;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

class EntityDamage {

    private final Listening listening;

    EntityDamage(final Listening listening) {
        this.listening = listening;
    }

    void handle(final EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        if (!this.listening.getMain().getTaupeGunManager().getState().equals(TaupeGunManager.State.STARTED)) {
            e.setCancelled(true);
        }
        if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL) && this.listening.getMain().getTaupeGunManager().getTimer() <= 10 * 15) {
            e.setCancelled(true);
        }
    }
}
