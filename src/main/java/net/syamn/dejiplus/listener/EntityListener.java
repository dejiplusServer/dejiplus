/**
 * dejiplus - Package: net.syamn.dejiplus.listener
 * Created: 2013/01/25 7:38:27
 */
package net.syamn.dejiplus.listener;

import java.util.List;

import net.syamn.dejiplus.ConfigurationManager;
import net.syamn.dejiplus.Dejiplus;
import net.syamn.utils.LogUtil;
import net.syamn.utils.StrUtil;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * EntityListener (EntityListener.java)
 * @author syam(syamn)
 */
public class EntityListener implements Listener{
    private final Dejiplus plugin;
    private final ConfigurationManager config;

    public EntityListener(final Dejiplus plugin){
        this.plugin = plugin;
        this.config = plugin.getConfigs();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDeath(final EntityDeathEvent event){
        if (!isCheckEntity(event.getEntity()) || event.getDroppedExp() <= 0){
            return;
        }

        final Entity ent = event.getEntity();
        final List<Entity> ents = ent.getNearbyEntities(0.75D, 1.5D, 0.75D);

        int i = 0;
        for (final Entity e : ents){
            if (isCheckEntity(e)) i++;
        }

        if (i >= 3){
            event.setDroppedExp(0);
            event.getDrops().clear();
            LogUtil.warning("ExpTrap detected! " + StrUtil.getLocationString(ent.getLocation()));
        }
    }

    private boolean isCheckEntity(final Entity ent){
        if (ent == null) return false;
        switch (ent.getType()){
            case ZOMBIE:
            case SKELETON:
            case SPIDER:
            case BLAZE:
                return true;
            default:
                return false;
        }
    }
}
