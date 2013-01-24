/**
 * dejiplus - Package: net.syamn.dejiplus.listener
 * Created: 2013/01/25 7:38:27
 */
package net.syamn.dejiplus.listener;

import net.syamn.dejiplus.ConfigurationManager;
import net.syamn.dejiplus.Dejiplus;

import org.bukkit.event.Listener;

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
}
