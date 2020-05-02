package fr.aless.TaupeGun.listeners;

import fr.aless.TaupeGun.game.PlayerTaupe;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

class BlockBreak {

    private final Listening listening;

    BlockBreak(final Listening listening) {
        this.listening = listening;
    }

    void handle(final BlockBreakEvent e) {
        if (PlayerTaupe.getPlayerTaupe(e.getPlayer()) == null) {
            return;
        }
        if (this.listening.getMain().getFileManager().getFile("config").get("config.actions.break") != null) {
            for (final String block : this.listening.getMain().getFileManager().getFile("config").getConfigurationSection("config.actions.break").getKeys(false)) {
                if (Material.getMaterial(block.toUpperCase()) != null) {
                    if (e.getBlock().getType().equals(Material.getMaterial(block.toUpperCase()))) {
                        final int xp = this.listening.getMain().getFileManager().getFile("config").getInt("config.actions.break." + block + ".xp");
                        e.setExpToDrop(xp);
                        final Material material = Material.valueOf(this.listening.getMain().getFileManager().getFile("config").getString("config.actions.break." + block + ".drop"));
                        final int amount = this.listening.getMain().getFileManager().getFile("config").getInt("config.actions.break." + block + ".amount");
                        e.setDropItems(false);
                        e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation().add(0.5, 0.5, 0.5), new ItemStack(material, amount));
                        return;
                    }
                }
            }
        }
    }
}
