package fr.SPFF.TaupeGun.commands.reveal;

import fr.SPFF.TaupeGun.game.PlayerTaupe;
import fr.SPFF.TaupeGun.game.TaupeGunManager;
import fr.SPFF.TaupeGun.plugin.TaupeGunPlugin;
import fr.SPFF.TaupeGun.utils.Message;
import net.minecraft.server.v1_15_R1.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_15_R1.SoundCategory;
import net.minecraft.server.v1_15_R1.SoundEffects;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RevealExecutor implements CommandExecutor {

    private final TaupeGunPlugin main;

    public RevealExecutor(){
        this.main = TaupeGunPlugin.getInstance();
        this.main.getCommand("reveal").setExecutor(this);
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
        if(playerTaupe.isReveal()){
            Message.create("&c&lTaupe Gun &4&l» &cVous vous êtes déjà révélé.").sendMessage(player);
            return false;
        }
        player.getLocation().getWorld().dropItem(player.getLocation(), new ItemStack(Material.GOLDEN_APPLE)).setPickupDelay(0);
        playerTaupe.setReveal(true);
        playerTaupe.getTeam().hide(playerTaupe.getPlayer());
        playerTaupe.getTaupe().show(playerTaupe.getPlayer());
        for(final Player pls : this.main.getServer().getOnlinePlayers()){
            final PacketPlayOutNamedSoundEffect sound = new PacketPlayOutNamedSoundEffect(SoundEffects.ENTITY_GHAST_SCREAM, SoundCategory.BLOCKS, pls.getLocation().getX(), pls.getLocation().getY(), pls.getLocation().getZ(), 1, 1);
            ((CraftPlayer) pls).getHandle().playerConnection.sendPacket(sound);
        }
        Message.create("&3&lTaupe Gun &8&l» &c")
                .append(player.getDisplayName())
                .append("&7 vient de révéler sa vraie nature !")
                .broadcast();
        return true;
    }
}
