/**
 * dejiplus - Package: net.syamn.dejiplus.command Created: 2012/12/03 2:39:51
 */
package net.syamn.dejiplus.command;

import java.util.ArrayList;
import java.util.List;

import net.syamn.dejiplus.ConfigurationManager;
import net.syamn.dejiplus.Dejiplus;
import net.syamn.utils.Util;

import net.syamn.utils.exception.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


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
            Util.message(sender, "&cThis command not loaded properly!");
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
            Util.message(sender, "&cコンソールからは実行できません！");
            return true;
        }
        if (sender instanceof Player) {
            player = (Player) sender;
            senderName = player.getName();
            isPlayer = true;
        }

        // Check permission
        if (!permission(sender)) {
            Util.message(sender, "&c権限がありません！");
            return true;
        }

        // Exec
        try {
            execute();
        } catch (CommandException ex) {
            Throwable error = ex;
            while (error instanceof Exception) {
                Util.message(sender, error.getMessage());
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
        Util.message(sender, "&c/" + this.command + " " + name + " " + usage);
    }
}