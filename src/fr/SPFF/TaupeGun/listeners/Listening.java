package fr.SPFF.TaupeGun.listeners;

import fr.SPFF.TaupeGun.plugin.TaupeGunPlugin;
import org.bukkit.event.Listener;

public class Listening implements Listener {

    private final TaupeGunPlugin main;

    public Listening() {
        this.main = TaupeGunPlugin.getInstance();
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }
}
