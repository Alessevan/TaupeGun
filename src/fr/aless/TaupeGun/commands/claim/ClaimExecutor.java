package fr.aless.TaupeGun.commands.claim;

import fr.aless.TaupeGun.game.PlayerTaupe;
import fr.aless.TaupeGun.game.TaupeGunManager;
import fr.aless.TaupeGun.plugin.TaupeGunPlugin;
import fr.aless.TaupeGun.utils.Message;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class ClaimExecutor implements CommandExecutor {

    private final TaupeGunPlugin main;

    public ClaimExecutor() {
        this.main = TaupeGunPlugin.getInstance();
        this.main.getCommand("claim").setExecutor(this);
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
        if (playerTaupe.hasClaim()) {
            Message.create(this.main.getFileManager().getPrefixError("kit")).sendMessage(player);
            return false;
        }
        this.kitPlayer(player);
        playerTaupe.setClaim(true);
        Message.create(this.main.getFileManager().getPrefixMessage("taupes.kit"))
                .sendMessage(player);
        return true;
    }

    private void kitPlayer(final Player player) {
        int i = new Random().nextInt(4);
        switch (i) {
            case 0:
                player.getLocation().getWorld().dropItem(player.getLocation(), new ItemStack(Material.TNT, 3)).setPickupDelay(0);
                player.getLocation().getWorld().dropItem(player.getLocation(), new ItemStack(Material.FLINT_AND_STEEL, 1)).setPickupDelay(0);
                return;
            case 1:
                player.getLocation().getWorld().dropItem(player.getLocation(), new ItemStack(Material.BLAZE_SPAWN_EGG, 3)).setPickupDelay(0);
                final ItemStack fireAspect = new ItemStack(Material.ENCHANTED_BOOK, 1);
                final EnchantmentStorageMeta enchantmentStorageMetaFireAspect = (EnchantmentStorageMeta) fireAspect.getItemMeta();
                enchantmentStorageMetaFireAspect.addStoredEnchant(Enchantment.FIRE_ASPECT, 1, false);
                fireAspect.setItemMeta(enchantmentStorageMetaFireAspect);
                player.getLocation().getWorld().dropItem(player.getLocation(), fireAspect).setPickupDelay(0);
                return;
            case 2:
                ItemStack potion = new ItemStack(Material.POTION, 1);
                PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
                potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3 * 60, 0), true);
                potionMeta.setColor(Color.PURPLE);
                potionMeta.setDisplayName("§cPotion of strength I");
                potion.setItemMeta(potionMeta);
                player.getLocation().getWorld().dropItem(player.getLocation(), potion).setPickupDelay(0);
                potion = new ItemStack(Material.SPLASH_POTION, 1);
                potionMeta = (PotionMeta) potion.getItemMeta();
                potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 45, 0), true);
                potionMeta.setColor(Color.GREEN);
                potionMeta.setDisplayName("§aPotion of Poison I");
                potion.setItemMeta(potionMeta);
                player.getLocation().getWorld().dropItem(player.getLocation(), potion).setPickupDelay(0);
                return;
        }
        player.getLocation().getWorld().dropItem(player.getLocation(), new ItemStack(Material.BOW, 1)).setPickupDelay(0);
        player.getLocation().getWorld().dropItem(player.getLocation(), new ItemStack(Material.ARROW, 48)).setPickupDelay(0);
    }
}
