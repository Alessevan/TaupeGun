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
        switch (e.getBlock().getType()) {
            case IRON_ORE: {
                e.setDropItems(false);
                e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation().add(0.5, 0D, 0.5), new ItemStack(Material.IRON_INGOT));
                e.setExpToDrop(7);
                return;
            }
            case GOLD_ORE: {
                e.setDropItems(false);
                e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation().add(0.5, 0D, 0.5), new ItemStack(Material.GOLD_INGOT));
                e.setExpToDrop(10);
                return;
            }
        }
    }
}
