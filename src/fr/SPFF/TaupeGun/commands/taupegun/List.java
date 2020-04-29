package fr.SPFF.TaupeGun.commands.taupegun;

import fr.SPFF.TaupeGun.game.Teams;
import fr.SPFF.TaupeGun.game.TeamsColor;
import fr.SPFF.TaupeGun.utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class List {

    private final TaupeGunExecutor executor;

    List(final TaupeGunExecutor executor) {
        this.executor = executor;
    }

    boolean handle(final CommandSender commandSender) {
        if (!commandSender.hasPermission("taupegun.list")) {
            Message.create("&c&lTaupe Gun &4&l» &cVous n'avez pas la permission de faire cela.").sendMessage(commandSender);
            return false;
        }
        final java.util.List<Teams> teamsList = Teams.getTeams();
        final Message message = Message.create("&3&lTaupe Gun &8&l» &7Liste des teams : ");
        for (final Teams team : teamsList) {
            message.addLine(TeamsColor.getColor(team.getColor().getValue()) + team.getName() + "&7 : ");
            for (final Player player : team.getPlayers()) {
                message.addLine(" &7- " + player.getDisplayName());
            }
        }
        message.sendMessage(commandSender);
        return true;
    }
}
