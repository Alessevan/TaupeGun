package fr.SPFF.TaupeGun.commands;

import fr.SPFF.TaupeGun.plugin.TaupeGunPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Executor implements CommandExecutor {

    private final TaupeGunPlugin main;

    public Executor() {
        this.main = TaupeGunPlugin.getInstance();
        this.main.getCommand("taupegun").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}
