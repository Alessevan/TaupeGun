package fr.SPFF.TaupeGun.commands.taupegun;

import fr.SPFF.TaupeGun.game.Teams;
import fr.SPFF.TaupeGun.plugin.TaupeGunPlugin;
import fr.SPFF.TaupeGun.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TaupeGunExecutor implements CommandExecutor, TabCompleter {

    private final TaupeGunPlugin main;

    public TaupeGunExecutor() {
        this.main = TaupeGunPlugin.getInstance();
        this.main.getCommand("taupegun").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            try {
                if (args.length == 0)
                    return new Reload(this).handle(sender);
                if (args[0].equalsIgnoreCase("addplayer"))
                    return new AddPlayer(this).handle(sender, args);
                if (args[0].equalsIgnoreCase("list"))
                    return new fr.SPFF.TaupeGun.commands.taupegun.List(this).handle(sender);
                if (args[0].equalsIgnoreCase("name"))
                    return new Name(this).handle(sender, args);
                if (args[0].equalsIgnoreCase("random"))
                    return new Random(this).handle(sender, args);
                if (args[0].equalsIgnoreCase("reload"))
                    return new Reload(this).handle(sender);
                if (args[0].equalsIgnoreCase("removeplayer"))
                    return new RemovePlayer(this).handle(sender, args);
                if (args[0].equalsIgnoreCase("start"))
                    return new Start(this).handle(sender);
                if (args[0].equalsIgnoreCase("stop"))
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
                if (args[0].equalsIgnoreCase("addplayer"))
                    return new AddPlayer(this).handle(player, args);
                if (args[0].equalsIgnoreCase("list"))
                    return new fr.SPFF.TaupeGun.commands.taupegun.List(this).handle(player);
                if (args[0].equalsIgnoreCase("name"))
                    return new Name(this).handle(player, args);
                if (args[0].equalsIgnoreCase("random"))
                    return new Random(this).handle(player, args);
                if (args[0].equalsIgnoreCase("reload"))
                    return new Reload(this).handle(player);
                if (args[0].equalsIgnoreCase("removeplayer"))
                    return new RemovePlayer(this).handle(player, args);
                if (args[0].equalsIgnoreCase("spawn"))
                    return new Spawn(this).handle(player);
                if (args[0].equalsIgnoreCase("start"))
                    return new Start(this).handle(player);
                if (args[0].equalsIgnoreCase("stop"))
                    return new Stop(this).handle(player);
            } catch (ArrayIndexOutOfBoundsException e) {
                Message.create("&c&lTaupe Gun &4&l» &cTrop peu d'argument.").sendMessage(player);
                return false;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 1) {
            final String[] completions0 = {"addplayer", "list", "name", "random", "reload", "removeplayer", "spawn", "start", "stop"};
            final List<String> completions = new ArrayList<>();
            for (final String alias : completions0) {
                if (commandSender.hasPermission("taupegun." + alias) && alias.toLowerCase().startsWith(args[0].toLowerCase()))
                    completions.add(alias);
            }
            return completions;
        }
        if (args.length == 2) {
            if (commandSender.hasPermission("taupegun." + args[0])) {
                List<String> completions = new ArrayList<>();
                if (args[0].equalsIgnoreCase("addplayer") || args[0].equalsIgnoreCase("removeplayer")) {
                    for (final Player player : this.main.getServer().getOnlinePlayers()) {
                        if (player.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                            completions.add(player.getName());
                    }
                } else if (args[0].equalsIgnoreCase("name")) {
                    for (final Teams teams : Teams.getTeams()) {
                        if (teams.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                            completions.add(teams.getName());
                    }
                }
                return completions;
            }
        }
        if (args.length == 3) {
            if (commandSender.hasPermission("taupegun." + args[0])) {
                List<String> completions = new ArrayList<>();
                if (args[0].equalsIgnoreCase("addplayer")) {
                    for (final Teams teams : Teams.getTeams()) {
                        if (teams.getName().toLowerCase().startsWith(args[2].toLowerCase()))
                            completions.add(teams.getName());
                    }
                }
                return completions;
            }
        }
        return null;
    }

    public TaupeGunPlugin getMain() {
        return main;
    }
}
