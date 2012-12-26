/**
 * dejiplus - Package: net.syamn.dejiplus.command Created: 2012/12/03 2:49:50
 */
package net.syamn.dejiplus.command;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.syamn.dejiplus.Dejiplus;
import net.syamn.dejiplus.util.Actions;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;


/**
 * DejiplusCommandHandler (DejiplusCommandHandler.java)
 * 
 * @author syam(syamn)
 */
public class DejiplusCommandHandler implements TabExecutor {
    private final Dejiplus plugin;

    // command map
    private Map<String, BaseCommand> commands = new HashMap<String, BaseCommand>();

    /**
     * Constructor
     */
    public DejiplusCommandHandler(final Dejiplus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command command,
            String commandLabel, String[] args) {
        final String commandName = command.getName()
                .toLowerCase(Locale.ENGLISH);
        final BaseCommand cmd = commands.get(commandName);
        if (cmd == null) {
            Actions.message(sender, ChatColor.RED
                    + "This command not loaded properly!");
            return true;
        }

        // Run the command
        cmd.run(plugin, sender, commandLabel, args);

        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender,
            Command command, String commandLabel, String[] args) {
        final String commandName = command.getName()
                .toLowerCase(Locale.ENGLISH);
        final BaseCommand cmd = commands.get(commandName);
        if (cmd == null) {
            return null;
        }

        // check permission here
        if (sender != null && !cmd.permission(sender)) {
            return null;
        }

        // Get tab complete list
        return cmd.tabComplete(plugin, sender, commandLabel, args);
    }

    public void registerCommand(final BaseCommand bc) {
        if (bc.name != null) {
            commands.put(bc.name, bc);
        } else {
            Dejiplus.log.warning("Invalid command not registered! "
                    + bc.getClass().getName());
        }
    }
}