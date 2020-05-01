package fr.aless.TaupeGun.commands.taupegun;

import fr.aless.TaupeGun.game.TaupeGunManager;
import fr.aless.TaupeGun.game.Teams;
import fr.aless.TaupeGun.utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class RemovePlayer {

    private final TaupeGunExecutor executor;

    RemovePlayer(final TaupeGunExecutor executor) {
        this.executor = executor;
    }

    boolean handle(final CommandSender commandSender, final String[] args) {
        if (!commandSender.hasPermission("taupegun.removeplayer")) {
            Message.create(this.executor.getMain().getFileManager().getPrefixError("permission")).sendMessage(commandSender);
            return false;
        }
        if (!this.executor.getMain().getTaupeGunManager().getState().equals(TaupeGunManager.State.WAITING)) {
            Message.create(this.executor.getMain().getFileManager().getPrefixError("commands.remove.alreadyStart")).sendMessage(commandSender);
            return false;
        }
        for (final Player player : this.executor.getMain().getServer().getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(args[1])) {
                if (!Teams.getPlayerTeam(player).isPresent()) {
                    Message.create(this.executor.getMain().getFileManager().getPrefixError("commands.remove.inTeam")).sendMessage(commandSender);
                    return false;
                }
                Teams.getPlayerTeam(player).get().removePlayer(player);
                Message.create(this.executor.getMain().getFileManager().getPrefixMessage("commands.remove").replace("%player%", player.getName())).sendMessage(commandSender);
                return true;
            }
        }
        Message.create(this.executor.getMain().getFileManager().getPrefixError("commands.remove.connected")).sendMessage(commandSender);
        return false;
    }
}
