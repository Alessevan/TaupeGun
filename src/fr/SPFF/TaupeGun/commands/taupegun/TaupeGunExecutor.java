package fr.SPFF.TaupeGun.commands.taupegun;

import fr.SPFF.TaupeGun.plugin.TaupeGunPlugin;
import fr.SPFF.TaupeGun.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TaupeGunExecutor implements CommandExecutor {

    private final TaupeGunPlugin main;

    public TaupeGunExecutor() {
        this.main = TaupeGunPlugin.getInstance();
        this.main.getCommand("taupegun").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            try {
                if(args.length == 0)
                    return new Reload(this).handle(sender);
                if (args[0].equalsIgnoreCase("reload"))
                    return new Reload(this).handle(sender);
                if(args[0].equalsIgnoreCase("start"))
                    return new Start(this).handle(sender);
                if(args[0].equalsIgnoreCase("stop"))
                    return new Stop(this).handle(sender);
                else {
                    return new Reload(this).handle(sender);
                }
            }
            catch (ArrayIndexOutOfBoundsException e){
                Message.create("&c&lTaupe Gun &4&l» &cTrop peu d'argument.").sendMessage(sender);
                return false;
            }
        }
        else {
            final Player player = (Player) sender;
            try {
                if(args[0].equalsIgnoreCase("reload"))
                    return new Reload(this).handle(player);
                if(args[0].equalsIgnoreCase("spawn"))
                    return new Spawn(this).handle(player);
                if(args[0].equalsIgnoreCase("start"))
                    return new Start(this).handle(player);
                if(args[0].equalsIgnoreCase("stop"))
                    return new Stop(this).handle(player);
            }
            catch (ArrayIndexOutOfBoundsException e){
                Message.create("&c&lTaupe Gun &4&l» &cTrop peu d'argument.").sendMessage(player);
                return false;
            }
        }
        return true;
    }

    public TaupeGunPlugin getMain() {
        return main;
    }
}
