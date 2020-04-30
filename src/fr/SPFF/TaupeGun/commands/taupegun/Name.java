package fr.SPFF.TaupeGun.commands.taupegun;

import fr.SPFF.TaupeGun.game.Teams;
import fr.SPFF.TaupeGun.utils.Message;
import org.bukkit.command.CommandSender;

class Name {

    private final TaupeGunExecutor executor;

    Name(final TaupeGunExecutor executor) {
        this.executor = executor;
    }

    boolean handle(final CommandSender commandSender, final String[] args) {
        if (!commandSender.hasPermission("taupegun.name")) {
            Message.create("&c&lTaupe Gun &4&l» &cVous n'avez pas la permission de faire cela.").sendMessage(commandSender);
            return false;
        }
        final java.util.List<Teams> teamsList = Teams.getTeams();
        for (final Teams team : teamsList) {
            if (team.getName().equalsIgnoreCase(args[1])) {
                team.setName(args[2]);
                Message.create("&3&lTaupe Gun &8&l» &7Le nom de la team a été changé.").sendMessage(commandSender);
                return true;
            }
        }
        Message.create("&c&lTaupe Gun &4&l» &cCette team n'existe pas.").sendMessage(commandSender);
        return false;
    }

}
