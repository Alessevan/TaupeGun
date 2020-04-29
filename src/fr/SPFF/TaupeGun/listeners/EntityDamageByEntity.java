package fr.SPFF.TaupeGun.listeners;

import fr.SPFF.TaupeGun.game.TaupeGunManager;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

class EntityDamageByEntity {

    private final Listening listening;

    EntityDamageByEntity(final Listening listening) {
        this.listening = listening;
    }

    void handle(final EntityDamageByEntityEvent e) {
        if (this.listening.getMain().getTaupeGunManager().getState().equals(TaupeGunManager.State.WAITING)) {
            e.setCancelled(true);
        }
        if (this.listening.getMain().getTaupeGunManager().getTimer() <= this.listening.getMain().getTaupeGunManager().taupeTime) {
            e.setCancelled(true);
        }
    }

}
