package fr.SPFF.TaupeGun.plugin;

import fr.SPFF.TaupeGun.commands.Executor;
import org.bukkit.plugin.java.JavaPlugin;

public class TaupeGunPlugin extends JavaPlugin {

    private static TaupeGunPlugin instance;

    @Override
    public void onEnable() {
        super.onEnable();
        TaupeGunPlugin.instance = this;
        new Executor();
    }

    public static TaupeGunPlugin getInstance() {
        return TaupeGunPlugin.instance;
    }
}
