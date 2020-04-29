package fr.SPFF.TaupeGun.commands.taupegun;

import fr.SPFF.TaupeGun.game.Teams;
import fr.SPFF.TaupeGun.utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class AddPlayer {

    private final TaupeGunExecutor executor;

    AddPlayer(final TaupeGunExecutor executor) {
        this.executor = executor;
    }

    boolean handle(final CommandSender commandSender, final String[] args) {
        if (!commandSender.hasPermission("taupegun.addplayer")) {
            Message.create("&c&lTaupe Gun &4&l» &cVous n'avez pas la permission de faire cela.").sendMessage(commandSender);
            return false;
        }
        final java.util.List<Teams> teamsList = Teams.getTeams();
        for (final Teams team : teamsList) {
            if (team.getName().equalsIgnoreCase(args[2])) {
                for (final Player player : this.executor.getMain().getServer().getOnlinePlayers()) {
                    if (player.getName().equalsIgnoreCase(args[1])) {
                        if (Teams.getPlayerTeam(player).isPresent()) {
                            Message.create("&c&lTaupe Gun &4&l» &cCe joueur est déjà dans une team.").sendMessage(commandSender);
                            return false;
                        }
                        team.addPlayer(player);
                        Message.create("&3&lTaupe Gun &8&l» &7" + player.getName() + " ajouté à la team " + team.getName() + ".").sendMessage(commandSender);
                        return true;
                    }
                }
                Message.create("&c&lTaupe Gun &4&l» &cCe joueur n'est pas connecté.").sendMessage(commandSender);
                return false;
            }
        }
        Message.create("&c&lTaupe Gun &4&l» &cCette team n'existe pas.").sendMessage(commandSender);
        return false;
    }
}
