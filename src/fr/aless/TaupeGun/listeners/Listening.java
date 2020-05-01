package fr.aless.TaupeGun.listeners;

import fr.aless.TaupeGun.plugin.TaupeGunPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Listening implements Listener {

    private final TaupeGunPlugin main;

    public Listening() {
        this.main = TaupeGunPlugin.getInstance();
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        new PlayerJoin(this).handle(e);
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        new PlayerQuit(this).handle(e);
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent e) {
        new EntityDamage(this).handle(e);
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent e) {
        new PlayerDeath(this).handle(e);
    }

    @EventHandler
    public void onPlayerRespawn(final PlayerRespawnEvent e) {
        new PlayerRespawn(this).handle(e);
    }

    @EventHandler
    public void onAsyncPlayerChat(final AsyncPlayerChatEvent e) {
        new AsyncPlayerChat(this).handle(e);
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent e) {
        new BlockBreak(this).handle(e);
    }

    @EventHandler
    public void onFoodLevelChange(final FoodLevelChangeEvent e) {
        new FoodLevelChange(this).handle(e);
    }

    public TaupeGunPlugin getMain() {
        return this.main;
    }
}
