/**
 * dejiplus - Package: net.syamn.dejiplus.listener
 * Created: 2012/12/26 23:14:23
 */
package net.syamn.dejiplus.listener;

import net.syamn.dejiplus.ConfigurationManager;
import net.syamn.dejiplus.Dejiplus;
import net.syamn.dejiplus.Perms;
import net.syamn.utils.LogUtil;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * BlockListener (BlockListener.java)
 * @author syam(syamn)
 */
public class BlockListener implements Listener{
    private final Dejiplus plugin;
    private final ConfigurationManager config;

    public BlockListener(final Dejiplus plugin){
        this.plugin = plugin;
        this.config = plugin.getConfigs();
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBreakSpawner(final BlockBreakEvent event){
        final Block block = event.getBlock();
        final Player player = event.getPlayer();
        final ItemStack item = player.getItemInHand();
        
        if (!Perms.SPAWNER_DROP.has(player) || !Material.MOB_SPAWNER.equals(block.getType()) || item == null || item.getType().equals(Material.AIR)){
            return;
        }
        
        switch (item.getType()){
            case IRON_PICKAXE:
            case GOLD_PICKAXE:
            case DIAMOND_PICKAXE:
                if (config.needsSilkEnchant() && !item.containsEnchantment(Enchantment.SILK_TOUCH)){
                    return;
                }
                break;
            default: return;
        }
        
        final EntityType type = ((CreatureSpawner) block.getState()).getSpawnedType();
        ItemStack spawner = new ItemStack(block.getType(), 1, type.getTypeId(), block.getData());
        //spawner.setDurability(type.getTypeId());
        block.getWorld().dropItemNaturally(block.getLocation(), spawner);
        event.setExpToDrop(0); // disabled exp drops
        
        if (config.isDebug()){
            LogUtil.info(event.getPlayer().getName() + " broke " + type.name() + "[" + type.getTypeId() + "] Spawner!");
        }
    }
}
