package fr.aless.TaupeGun.listeners;

import fr.aless.TaupeGun.game.TaupeGunManager;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

class EntityDamageByEntity {

    private final Listening listening;

    EntityDamageByEntity(final Listening listening) {
        this.listening = listening;
    }

    void handle(final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        if (!this.listening.getMain().getTaupeGunManager().getState().equals(TaupeGunManager.State.STARTED)) {
            e.setCancelled(true);
        }
        if (this.listening.getMain().getTaupeGunManager().getTimer() <= this.listening.getMain().getTaupeGunManager().taupeTime) {
            e.setCancelled(true);
        }
    }

}
