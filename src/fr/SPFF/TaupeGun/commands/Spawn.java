package fr.SPFF.TaupeGun.commands;

import fr.SPFF.TaupeGun.utils.Message;
import org.bukkit.entity.Player;

class Spawn {

    private final Executor executor;

    Spawn(final Executor executor) {
        this.executor = executor;
    }

    boolean handle(final Player player){
        if(!player.hasPermission("taupegun.spawn")){
            Message.create("&c&lTaupe Gun &4&l» &cVous n'avez pas la permission de faire cela.").sendMessage(player);
            return true;
        }
        Message.create("&3&lTaupe Gun &8&l» &7Sauvegarde du spawn.").sendMessage(player);
        this.executor.getMain().getFileManager().setLine("data", "spawn", player.getLocation());
        Message.create("&3&lTaupe Gun &8&l» &7Spawn sauvegardé.").sendMessage(player);
        return true;
    }
}
