package fr.aless.TaupeGun.listeners;

import fr.aless.TaupeGun.game.TaupeGunManager;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;

class FoodLevelChange {

    private final Listening listening;

    FoodLevelChange(final Listening listening) {
        this.listening = listening;
    }

    void handle(final FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            if (!this.listening.getMain().getTaupeGunManager().getState().equals(TaupeGunManager.State.STARTED)) {
                e.setCancelled(true);
            }
        }
    }
}
