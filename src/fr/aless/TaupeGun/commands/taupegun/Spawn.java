package fr.aless.TaupeGun.commands.taupegun;

import fr.aless.TaupeGun.utils.Message;
import org.bukkit.entity.Player;

class Spawn {

    private final TaupeGunExecutor executor;

    Spawn(final TaupeGunExecutor executor) {
        this.executor = executor;
    }

    boolean handle(final Player player) {
        if (!player.hasPermission("taupegun.spawn")) {
            Message.create(this.executor.getMain().getFileManager().getPrefixError("permission")).sendMessage(player);
            return false;
        }
        Message.create(this.executor.getMain().getFileManager().getPrefixMessage("commands.spawn.progress")).sendMessage(player);
        this.executor.getMain().getFileManager().setLine("data", "spawn", player.getLocation());
        Message.create(this.executor.getMain().getFileManager().getPrefixMessage("commands.spawn.end")).sendMessage(player);
        return true;
    }
}
