/**
 * dejiplus - Package: net.syamn.dejiplus.command Created: 2012/12/03 2:47:08
 */
package net.syamn.dejiplus.command;

import java.util.ArrayList;
import java.util.List;

import net.syamn.dejiplus.Perms;
import net.syamn.utils.LogUtil;
import net.syamn.utils.StrUtil;
import net.syamn.utils.Util;
import net.syamn.utils.exception.CommandException;

import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * CommandDejiplus (CommandDejiplus.java)
 * 
 * @author syam(syamn)
 */
public class CommandDejiplus extends BaseCommand {
    public CommandDejiplus() {
        bePlayer = false;
        name = "dejiplus";
        argLength = 0;
        usage = "<- dejiplus information";
    }

    private void sendHelp(){
        
    }
    
    @Override
    public void execute() throws CommandException {
        // reload
        if (args.get(0).equalsIgnoreCase("reload")) {
            if (!Perms.RELOAD.has(sender)){
                Util.message(sender, "&cPermission Denied!");
                return;
            }
            try {
                plugin.getConfigs().loadConfig(false);
            } catch (Exception ex) {
                LogUtil.warning("an error occured while trying to load the config file.");
                ex.printStackTrace();
                return;
            }
            Util.message(sender, "&aConfiguration reloaded!");
            return;
        }
        
        // item
        if (args.size() >= 2 && args.get(0).equalsIgnoreCase("item") && player != null) {
            if (!Perms.EDIT_ITEM.has(sender)){
                Util.message(sender, "&cPermission Denied!");
                return;
            }
            
            ItemStack item = player.getItemInHand();
            if (item == null) {
                Util.message(player, "&cアイテムを持っていません");
                return;
            }
            ItemMeta meta = item.getItemMeta();
            
            args.remove(0); // remove /<cmd> item
            final String action = args.remove(0);
            
            if (action.equalsIgnoreCase("clear")){
                meta.setLore(null);
            }else{
                if (args.size() <= 0){
                    Util.message(sender, "&cパラメータが足りません！"); return;
                }
                
                final String str = Util.coloring(StrUtil.join(args, " "));
                if (action.equalsIgnoreCase("name")) {
                    meta.setDisplayName(str);
                } else if (action.equalsIgnoreCase("set")) {
                    List<String> lores = new ArrayList<String>();
                    lores.add(str);
                    meta.setLore(lores);
                } else if (action.equalsIgnoreCase("add")) {
                    List<String> lores = meta.getLore();
                    lores.add(str);
                    meta.setLore(lores);
                } else {
                    Util.message(player, "&cUnknown action! (name/set/add/clear)");
                    return;
                }
            }
            
            item.setItemMeta(meta);
            player.setItemInHand(item);
            Util.message(player, "&aSuccess!");
            return;
        }
        
        sendHelp();
    }

    @Override
    public boolean permission(CommandSender sender) {
        return true;
    }
}
