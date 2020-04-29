package fr.SPFF.TaupeGun.commands.taupegun;

import fr.SPFF.TaupeGun.utils.Message;
import org.bukkit.command.CommandSender;

class Start {

    private final TaupeGunExecutor executor;

    Start(final TaupeGunExecutor executor){
        this.executor = executor;
    }

    boolean handle(final CommandSender commandSender){
        if (!commandSender.hasPermission("taupegun.start")) {
            Message.create("&c&lTaupe Gun &4&l» &cVous n'avez pas la permission de faire cela.").sendMessage(commandSender);
            return false;
        }
        try {
            this.executor.getMain().getTaupeGunManager().chronoStart();
            Message.create("&3&lTaupe Gun &8&l» &7Démarrage de la partie grâce à " + commandSender.getName() + ".").broadcast();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
