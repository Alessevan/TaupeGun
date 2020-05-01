package fr.aless.TaupeGun.commands.taupegun;

import fr.aless.TaupeGun.game.Teams;
import fr.aless.TaupeGun.game.TeamsColor;
import fr.aless.TaupeGun.utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class List {

    private final TaupeGunExecutor executor;

    List(final TaupeGunExecutor executor) {
        this.executor = executor;
    }

    boolean handle(final CommandSender commandSender) {
        if (!commandSender.hasPermission("taupegun.list")) {
            Message.create(this.executor.getMain().getFileManager().getPrefixError("permission")).sendMessage(commandSender);
            return false;
        }
        final java.util.List<Teams> teamsList = Teams.getTeams();
        final Message message = Message.create(this.executor.getMain().getFileManager().getPrefixMessage("commands.list"));
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
