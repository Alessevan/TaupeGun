package fr.aless.TaupeGun.commands.taupegun;

import fr.aless.TaupeGun.game.TaupeGunManager;
import fr.aless.TaupeGun.game.Teams;
import fr.aless.TaupeGun.utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class AddPlayer {

    private final TaupeGunExecutor executor;

    AddPlayer(final TaupeGunExecutor executor) {
        this.executor = executor;
    }

    boolean handle(final CommandSender commandSender, final String[] args) {
        if (!commandSender.hasPermission("taupegun.addplayer")) {
            Message.create(this.executor.getMain().getFileManager().getPrefixError("permission")).sendMessage(commandSender);
            return false;
        }
        if (!this.executor.getMain().getTaupeGunManager().getState().equals(TaupeGunManager.State.WAITING)) {
            Message.create(this.executor.getMain().getFileManager().getPrefixError("commands.add.alreadyStart")).sendMessage(commandSender);
            return false;
        }
        final java.util.List<Teams> teamsList = Teams.getTeams();
        for (final Teams team : teamsList) {
            if (team.getName().equalsIgnoreCase(args[2])) {
                for (final Player player : this.executor.getMain().getServer().getOnlinePlayers()) {
                    if (player.getName().equalsIgnoreCase(args[1])) {
                        if (Teams.getPlayerTeam(player).isPresent()) {
                            Message.create(this.executor.getMain().getFileManager().getPrefixError("commands.add.inTeam")).sendMessage(commandSender);
                            return false;
                        }
                        team.addPlayer(player);
                        Message.create(this.executor.getMain().getFileManager().getPrefixMessage("commands.add").replace("%player%", player.getDisplayName()).replace("%team%", team.getName())).sendMessage(commandSender);
                        return true;
                    }
                }
                Message.create(this.executor.getMain().getFileManager().getPrefixError("commands.add.connected")).sendMessage(commandSender);
                return false;
            }
        }
        Message.create(this.executor.getMain().getFileManager().getPrefixError("commands.add.team")).sendMessage(commandSender);
        return false;
    }
}
