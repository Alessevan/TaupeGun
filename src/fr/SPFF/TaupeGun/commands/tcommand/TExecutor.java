package fr.SPFF.TaupeGun.commands.tcommand;

import fr.SPFF.TaupeGun.game.PlayerTaupe;
import fr.SPFF.TaupeGun.game.TaupeGunManager;
import fr.SPFF.TaupeGun.plugin.TaupeGunPlugin;
import fr.SPFF.TaupeGun.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TExecutor implements CommandExecutor {

    private final TaupeGunPlugin main;

    public TExecutor(){
        this.main = TaupeGunPlugin.getInstance();
        this.main.getCommand("t").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        final Player player = (Player) sender;
        final PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupe(player);

        if(!this.main.getTaupeGunManager().getState().equals(TaupeGunManager.State.WAITING)){
            Message.create("&c&lTaupe Gun &4&l» &cLa partie n'a pas commencé.").sendMessage(player);
            return false;
        }
        if(!playerTaupe.isTaupe()){
            Message.create("&c&lTaupe Gun &4&l» &cVous n'êtes pas une taupe.").sendMessage(player);
            return false;
        }
        final StringBuilder stringBuilder = new StringBuilder();
        for(final String part : args){
            stringBuilder.append(part).append((part.equals(args[args.length - 1]) ? "" : " "));
        }
        if(stringBuilder.length() == 0){
            Message.create("&c&lTaupe Gun &4&l» &cVous devez écrire un message.").sendMessage(player);
            return false;
        }
        final Message message = Message.create("&c&l[Taupe] &c" + player.getDisplayName() + " : &f" + stringBuilder.toString());
        for(final Player players : playerTaupe.getTeam().getPlayers()){
            message.sendMessage(players);
        }
        this.main.getLogger().info("[Taupe] " + player.getDisplayName() + " : " + stringBuilder.toString());
        return true;
    }
}
