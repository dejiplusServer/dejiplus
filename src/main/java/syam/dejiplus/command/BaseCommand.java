/**
 * dejiplus - Package: syam.dejiplus.command Created: 2012/12/03 2:39:51
 */
package syam.dejiplus.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import syam.dejiplus.ConfigurationManager;
import syam.dejiplus.Dejiplus;
import syam.dejiplus.exception.CommandException;
import syam.dejiplus.util.Actions;

/**
 * BaseCommand (BaseCommand.java)
 * 
 * @author syam(syamn)
 */
public abstract class BaseCommand {
    // Set this class
    protected Dejiplus plugin;
    protected ConfigurationManager config;
    protected CommandSender sender;
    protected String command;

    // Needs init
    protected List<String> args = new ArrayList<String>();
    protected String senderName;
    protected Player player;
    protected boolean isPlayer;

    // Set this class if banning (needs init)
    protected String target = "";
    protected String targetIP = "";

    // Set extend class constructor (Command property)
    protected String name;
    protected int argLength = 0;
    protected String usage;
    protected boolean bePlayer = false;

    public boolean run(final Dejiplus plugin, final CommandSender sender,
            final String cmd, final String[] preArgs) {
        if (name == null) {
            Actions.message(sender, "&cThis command not loaded properly!");
            return true;
        }

        // init command
        init();

        this.plugin = plugin;
        this.config = plugin.getConfigs();
        this.sender = sender;
        this.command = cmd;

        // Sort args
        for (String arg : preArgs)
            args.add(arg);

        // Check args size
        if (argLength > args.size()) {
            sendUsage();
            return true;
        }

        // Check sender is player
        if (bePlayer && !(sender instanceof Player)) {
            Actions.message(sender, "&cコンソールからは実行できません！");
            return true;
        }
        if (sender instanceof Player) {
            player = (Player) sender;
            senderName = player.getName();
            isPlayer = true;
        }

        // Check permission
        if (!permission(sender)) {
            Actions.message(sender, "&c権限がありません！");
            return true;
        }

        // Exec
        try {
            execute();
        } catch (CommandException ex) {
            Throwable error = ex;
            while (error instanceof Exception) {
                Actions.message(sender, error.getMessage());
                error = error.getCause();
            }
        }

        return true;
    }

    /**
     * Initialize command
     */
    private void init() {
        this.args.clear();
        this.player = null;
        this.isPlayer = false;
        this.senderName = "Console";
    }

    /**
     * Execute command
     */
    public abstract void execute() throws CommandException;

    /**
     * TabComplete
     */
    protected List<String> tabComplete(final Dejiplus plugin,
            final CommandSender sender, final String cmd, final String[] preArgs) {
        return null;
    }

    /**
     * Check sender has command permission
     * 
     * @return true if sender has permission
     */
    public abstract boolean permission(final CommandSender sender);

    /**
     * Send command usage
     */
    public void sendUsage() {
        Actions.message(sender, "&c/" + this.command + " " + name + " " + usage);
    }
}