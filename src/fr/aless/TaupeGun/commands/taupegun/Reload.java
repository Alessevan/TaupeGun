package fr.aless.TaupeGun.commands.taupegun;

import fr.aless.TaupeGun.game.TaupeGunManager;
import fr.aless.TaupeGun.utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

class Reload {

    private final TaupeGunExecutor executor;

    Reload(final TaupeGunExecutor executor) {
        this.executor = executor;
    }

    boolean handle(final CommandSender sender) {
        if (!sender.hasPermission("taupegun.reload")) {
            Message.create(this.executor.getMain().getFileManager().getPrefixError("permission")).sendMessage(sender);
            return false;
        }
        if (!this.executor.getMain().getTaupeGunManager().getState().equals(TaupeGunManager.State.WAITING)) {
            Message.create(this.executor.getMain().getFileManager().getPrefixError("commands.reload.alreadyStart")).sendMessage(sender);
            return false;
        }

        Message.create(this.executor.getMain().getFileManager().getPrefixMessage("commands.reload.progress")).sendMessage(sender);
        try {
            this.executor.getMain().getFileManager().reload();
            this.executor.getMain().getTaupeGunManager().init();
        } catch (IOException | InvalidConfigurationException e) {
            Message.create("&c&lTaupe Gun &4&l» &cError while reloading files.").sendMessage(sender);
            return false;
        }
        Message.create(this.executor.getMain().getFileManager().getPrefixMessage("commands.reload.end")).sendMessage(sender);
        return true;
    }
}
