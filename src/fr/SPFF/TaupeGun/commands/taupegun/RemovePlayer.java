package fr.SPFF.TaupeGun.commands.taupegun;

import fr.SPFF.TaupeGun.game.Teams;
import fr.SPFF.TaupeGun.utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class RemovePlayer {

    private final TaupeGunExecutor executor;

    RemovePlayer(final TaupeGunExecutor executor) {
        this.executor = executor;
    }

    boolean handle(final CommandSender commandSender, final String[] args) {
        if (!commandSender.hasPermission("taupegun.removeplayer")) {
            Message.create("&c&lTaupe Gun &4&l» &cVous n'avez pas la permission de faire cela.").sendMessage(commandSender);
            return false;
        }
        for (final Player player : this.executor.getMain().getServer().getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(args[1])) {
                if (!Teams.getPlayerTeam(player).isPresent()) {
                    Message.create("&c&lTaupe Gun &4&l» &cCe joueur n'est dans auncune team.").sendMessage(commandSender);
                    return false;
                }
                Teams.getPlayerTeam(player).get().removePlayer(player);
                Message.create("&3&lTaupe Gun &8&l» &7" + player.getName() + " n'est plus dans sa team.").sendMessage(commandSender);
                return true;
            }
        }
        Message.create("&c&lTaupe Gun &4&l» &cCe joueur n'est pas connecté.").sendMessage(commandSender);
        return false;
    }
}
