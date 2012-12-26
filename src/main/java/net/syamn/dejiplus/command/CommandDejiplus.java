/**
 * dejiplus - Package: net.syamn.dejiplus.command Created: 2012/12/03 2:47:08
 */
package net.syamn.dejiplus.command;

import net.syamn.dejiplus.Perms;
import net.syamn.utils.LogUtil;
import net.syamn.utils.Util;
import net.syamn.utils.exception.CommandException;

import org.bukkit.command.CommandSender;

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
        
        sendHelp();
    }

    @Override
    public boolean permission(CommandSender sender) {
        return true;
    }
}
