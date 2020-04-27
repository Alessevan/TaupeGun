package fr.SPFF.TaupeGun.listeners;

import fr.SPFF.TaupeGun.game.TaupeGunManager;
import org.bukkit.event.entity.EntityDamageEvent;

class EntityDamage {

    private final Listening listening;

    EntityDamage(final Listening listening) {
        this.listening = listening;
    }

    void handle(final EntityDamageEvent e){
        if(this.listening.getMain().getTaupeGunManager().getState().equals(TaupeGunManager.State.WAITING)){
            e.setCancelled(true);
        }
        if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL) && this.listening.getMain().getTaupeGunManager().getTimer() <= 10 * 10){
            e.setCancelled(true);
        }
    }
}
