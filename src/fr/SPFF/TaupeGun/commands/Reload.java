package fr.SPFF.TaupeGun.commands;

import fr.SPFF.TaupeGun.utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

class Reload {

    private final Executor executor;

    Reload(final Executor executor) {
        this.executor = executor;
    }

    boolean handle(final CommandSender sender){
        if(!sender.hasPermission("taupegun.reload")){
            Message.create("&c&lTaupe Gun &4&l» &cVous n'avez pas la permission de faire cela.").sendMessage(sender);
            return false;
        }

        Message.create("&3&lTaupe Gun &8&l» &7Rechargement de la configuration en cours...").sendMessage(sender);
        try {
            this.executor.getMain().getFileManager().reload();
        } catch (IOException | InvalidConfigurationException e) {
            Message.create("&c&lTaupe Gun &4&l» &cErreur lors du rechargement.").sendMessage(sender);
            return false;
        }
        Message.create("&3&lTaupe Gun &8&l» &7Rechargement de la configuration terminé.").sendMessage(sender);
        return true;
    }
}
