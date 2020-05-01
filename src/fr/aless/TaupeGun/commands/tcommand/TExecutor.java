package fr.aless.TaupeGun.commands.tcommand;

import fr.aless.TaupeGun.game.PlayerTaupe;
import fr.aless.TaupeGun.game.TaupeGunManager;
import fr.aless.TaupeGun.plugin.TaupeGunPlugin;
import fr.aless.TaupeGun.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TExecutor implements CommandExecutor {

    private final TaupeGunPlugin main;

    public TExecutor() {
        this.main = TaupeGunPlugin.getInstance();
        this.main.getCommand("t").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        final Player player = (Player) sender;
        final PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupe(player);
        if (!this.main.getTaupeGunManager().getState().equals(TaupeGunManager.State.STARTED)) {
            Message.create(this.main.getFileManager().getPrefixError("start")).sendMessage(player);
            return false;
        }
        if (playerTaupe == null) {
            Message.create(this.main.getFileManager().getPrefixError("inGame")).sendMessage(player);
            return false;
        }
        if (!playerTaupe.isTaupe()) {
            Message.create(this.main.getFileManager().getPrefixError("taupe")).sendMessage(player);
            return false;
        }
        final StringBuilder stringBuilder = new StringBuilder();
        for (final String part : args) {
            stringBuilder.append(part).append((part.equals(args[args.length - 1]) ? "" : " "));
        }
        if (stringBuilder.length() == 0) {
            Message.create(this.main.getFileManager().getPrefixError("write")).sendMessage(player);
            return false;
        }
        final Message message = Message.create("&c&l" + playerTaupe.getTaupe().getName() + " [Taupe] &c" + player.getDisplayName() + " : &f" + stringBuilder.toString());
        for (final Player players : playerTaupe.getTaupe().getPlayers()) {
            message.sendMessage(players);
        }
        this.main.getLogger().info("[Taupe] " + player.getDisplayName() + " : " + stringBuilder.toString());
        return true;
    }
}
