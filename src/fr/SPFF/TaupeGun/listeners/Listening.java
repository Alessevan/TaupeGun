package fr.SPFF.TaupeGun.listeners;

import fr.SPFF.TaupeGun.plugin.TaupeGunPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Listening implements Listener {

    private final TaupeGunPlugin main;

    public Listening() {
        this.main = TaupeGunPlugin.getInstance();
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e){
        new PlayerJoin(this).handle(e);
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent e){
        new EntityDamage(this).handle(e);
    }

    @EventHandler
    public void onAsyncPlayerChat(final AsyncPlayerChatEvent e){
        new AsyncPlayerChat(this).handle(e);
    }

    public TaupeGunPlugin getMain() {
        return this.main;
    }
}
