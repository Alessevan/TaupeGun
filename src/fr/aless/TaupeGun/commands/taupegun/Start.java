package fr.aless.TaupeGun.commands.taupegun;

import fr.aless.TaupeGun.game.TaupeGunManager;
import fr.aless.TaupeGun.utils.Message;
import org.bukkit.command.CommandSender;

class Start {

    private final TaupeGunExecutor executor;

    Start(final TaupeGunExecutor executor) {
        this.executor = executor;
    }

    boolean handle(final CommandSender commandSender) {
        if (!commandSender.hasPermission("taupegun.start")) {
            Message.create(this.executor.getMain().getFileManager().getPrefixError("permission")).sendMessage(commandSender);
            return false;
        }
        try {
            if (this.executor.getMain().getTaupeGunManager().getState().equals(TaupeGunManager.State.WAITING)) {
                this.executor.getMain().getTaupeGunManager().chronoStart();
                Message.create(this.executor.getMain().getFileManager().getPrefixMessage("commands.start").replace("%started%", commandSender.getName())).broadcast();
                return true;
            } else {
                Message.create(this.executor.getMain().getFileManager().getPrefixError("commands.start.alreadyStart")).sendMessage(commandSender);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
