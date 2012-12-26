/**
 * dejiplus - Package: net.syamn.dejiplus.command Created: 2012/12/03 2:47:08
 */
package net.syamn.dejiplus.command;

import net.syamn.dejiplus.Perms;
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

    @Override
    public void execute() throws CommandException {
        Util.message(sender, "&bYay, this is test command!");
    }

    @Override
    public boolean permission(CommandSender sender) {
        return Perms.ADMIN.has(sender);
    }
}
