/**
 * dejiplus - Package: syam.dejiplus.command Created: 2012/12/03 2:47:08
 */
package syam.dejiplus.command;

import org.bukkit.command.CommandSender;

import syam.dejiplus.Perms;
import syam.dejiplus.exception.CommandException;
import syam.dejiplus.util.Actions;

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
        Actions.message(sender, "&bYay, this is test command!");
    }

    @Override
    public boolean permission(CommandSender sender) {
        return Perms.ADMIN.has(sender);
    }
}
