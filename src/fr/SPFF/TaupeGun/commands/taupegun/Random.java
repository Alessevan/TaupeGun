package fr.SPFF.TaupeGun.commands.taupegun;

import fr.SPFF.TaupeGun.utils.Message;
import fr.SPFF.TaupeGun.utils.MiscUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

class Random {

    private final TaupeGunExecutor taupeGunExecutor;

    Random(final TaupeGunExecutor taupeGunExecutor) {
        this.taupeGunExecutor = taupeGunExecutor;
    }

    boolean handle(final CommandSender sender, final String[] args) {
        if (args.length == 1) {
            return false;
        }
        if (!StringUtils.isNumeric(args[1]))
            return false;
        final Message message = Message.create("");
        int i = 0;
        int size = Integer.parseInt(args[1]);
        for (final Player player : MiscUtils.shufflePlayers(new ArrayList<>(this.taupeGunExecutor.getMain().getServer().getOnlinePlayers()))) {
            if (i % size == 0 && i != 0) {
                message.addLine(player.getDisplayName());
            } else {
                message.append(", ").append(player.getDisplayName());
            }
            i++;
        }
        message.broadcast();
        return true;
    }
}
