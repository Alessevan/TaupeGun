package fr.aless.TaupeGun.commands.taupegun;

import fr.aless.TaupeGun.game.TaupeGunManager;
import fr.aless.TaupeGun.utils.Message;
import org.bukkit.command.CommandSender;

class Stop {

    private final TaupeGunExecutor executor;

    Stop(final TaupeGunExecutor executor) {
        this.executor = executor;
    }

    boolean handle(final CommandSender commandSender) {
        if (!commandSender.hasPermission("taupegun.stop")) {
            Message.create(this.executor.getMain().getFileManager().getPrefixError("permission")).sendMessage(commandSender);
            return false;
        }
        if (!this.executor.getMain().getTaupeGunManager().getState().equals(TaupeGunManager.State.WAITING)) {
            this.executor.getMain().getTaupeGunManager().stop();
            Message.create(this.executor.getMain().getFileManager().getPrefixMessage("commands.stop").replace("%stopped%", commandSender.getName())).broadcast();
            return true;
        } else {
            Message.create(this.executor.getMain().getFileManager().getPrefixError("commands.stop.start")).broadcast();
            return false;
        }
    }

}
