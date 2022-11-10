package de.jasper.bsi.events;

import de.jasper.bsi.BSIMain;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BSIEvents implements Listener {
    public static File file = new File("plugins/bsi", "inventories.yml");
    public static YamlConfiguration items = YamlConfiguration.loadConfiguration(file);
    public static final HashMap<Location, Inventory> bookMap = new HashMap<>();

    @EventHandler
    public void onClickBookshelf(PlayerInteractEvent e) {
        Action a = e.getAction();
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();

        if (b != null) {
            Location blockLoc = e.getClickedBlock().getLocation();
            if (b.getType() == Material.BOOKSHELF && Objects.equals(e.getHand(), EquipmentSlot.HAND) && a.equals(Action.RIGHT_CLICK_BLOCK) && !p.isSneaking()) {
                openInv(p, blockLoc);
            }
        }
    }

    public void openInv(Player p, Location loc) {
        p.openInventory(Objects.requireNonNull(BSIMain.getInv(loc)));
    }

   @EventHandler
   public static void onBreak(BlockBreakEvent e){
        Block b = e.getBlock();
        Player p = e.getPlayer();
        Location blockLoc = b.getLocation();
        Inventory inventory = BSIMain.getInv(blockLoc);

        if(b.getType() == Material.BOOKSHELF) {
            inventory.setItem(0, new ItemStack(Material.BOOK));
            inventory.setItem(1, null);
            inventory.setItem(2, null);
            inventory.setItem(3, new ItemStack(Material.BOOK));
            inventory.setItem(4, null);
            inventory.setItem(5, new ItemStack(Material.BOOK));
            inventory.setItem(6, null);
            inventory.setItem(7, new ItemStack(Material.BOOK));
            inventory.setItem(8, new ItemStack(Material.BOOK));
            BSIEvents.bookMap.put(blockLoc, inventory);
        }
   }

    @EventHandler
    public void cancelPlace(BlockPlaceEvent e) {
        if (e.getBlockAgainst().getType() == Material.BOOKSHELF && !e.getPlayer().isSneaking())
            e.setBuild(false);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals("§cBookshelf §fInventory")) {
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getType().equals(Material.BOOK)) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
