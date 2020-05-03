package fr.aless.TaupeGun.plugin;

import fr.aless.TaupeGun.commands.claim.ClaimExecutor;
import fr.aless.TaupeGun.commands.reveal.RevealExecutor;
import fr.aless.TaupeGun.commands.taupegun.TaupeGunExecutor;
import fr.aless.TaupeGun.commands.tcommand.TExecutor;
import fr.aless.TaupeGun.game.TaupeGunManager;
import fr.aless.TaupeGun.listeners.Listening;
import fr.aless.TaupeGun.utils.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import java.io.IOException;
import java.util.logging.Level;

public class TaupeGunPlugin extends JavaPlugin {

    private static TaupeGunPlugin instance;

    private TaupeGunManager taupeGunManager;

    private FileManager fileManager;

    private World world;

    private Scoreboard scoreboard;

    public static TaupeGunPlugin getInstance() {
        return TaupeGunPlugin.instance;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        TaupeGunPlugin.instance = this;
        try {
            this.fileManager = new FileManager("data", "config");
        } catch (IOException | InvalidConfigurationException e) {
            this.getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', "§c§lTaupe Gun §4§l» §cError while retrieve creating files ."), e);
        }
        if (this.getServer().getWorlds().parallelStream().noneMatch(world -> world.getName().equalsIgnoreCase(this.fileManager.getFile("config").getString("config.world")))) {
            this.getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', "§c§lTaupe Gun §4§l» §c§lUnknown world in config.yml"));
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.world = this.getServer().getWorlds().parallelStream().filter(world -> world.getName().equalsIgnoreCase(this.fileManager.getFile("config").getString("config.world"))).findFirst().get();
        this.getLogger().info("\n" +
                "___________                              ________              \n" +
                "\\__    ___/____   __ ________   ____    /  _____/ __ __  ____  \n" +
                "  |    |  \\__  \\ |  |  \\____ \\_/ __ \\  /   \\  ___|  |  \\/    \\ \n" +
                "  |    |   / __ \\|  |  /  |_> >  ___/  \\    \\_\\  \\  |  /   |  \\\n" +
                "  |____|  (____  /____/|   __/ \\___  >  \\______  /____/|___|  /\n" +
                "               \\/      |__|        \\/          \\/           \\/ \n");

        new TaupeGunExecutor();
        new TExecutor();
        new ClaimExecutor();
        new RevealExecutor();
        new Listening();
        this.scoreboard = this.getServer().getScoreboardManager().getMainScoreboard();
        this.taupeGunManager = new TaupeGunManager();
        this.taupeGunManager.init();
    }

    public FileManager getFileManager() {
        return this.fileManager;
    }

    public TaupeGunManager getTaupeGunManager() {
        return this.taupeGunManager;
    }

    public World getWorld() {
        return this.world;
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }
}
