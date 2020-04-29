package fr.SPFF.TaupeGun.commands.taupegun;

import fr.SPFF.TaupeGun.utils.Message;
import org.bukkit.command.CommandSender;

class Stop {

    private final TaupeGunExecutor executor;

    Stop(final TaupeGunExecutor executor){
        this.executor = executor;
    }

    boolean handle(final CommandSender commandSender){
        if(!commandSender.hasPermission("taupegun.stop")){
            Message.create("&c&lTaupe Gun &4&l» &cVous n'avez pas la permission de faire cela.").sendMessage(commandSender);
            return false;
        }
        this.executor.getMain().getTaupeGunManager().stop();
        Message.create("&3&lTaupe Gun &8&l» &7Arrêt de la partie à cause de " + commandSender.getName() + ".").broadcast();
        return true;
    }

}
