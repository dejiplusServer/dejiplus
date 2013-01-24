/**
 * dejiplus - Package: net.syamn.dejiplus Created: 2012/12/03 2:24:51
 */
package net.syamn.dejiplus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.syamn.dejiplus.command.BaseCommand;
import net.syamn.dejiplus.command.CommandDejiplus;
import net.syamn.dejiplus.command.DejiplusCommandHandler;
import net.syamn.dejiplus.listener.BlockListener;
import net.syamn.dejiplus.listener.EntityListener;
import net.syamn.dejiplus.listener.PlayerListener;
import net.syamn.utils.LogUtil;
import net.syamn.utils.Metrics;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * Dejiplus (Dejiplus.java)
 * 
 * @author syam(syamn)
 */
public class Dejiplus extends JavaPlugin {
    // ** Logger **
    public final static Logger log = Logger.getLogger("Minecraft");
    public final static String logPrefix = "[dejiplus] ";
    public final static String msgPrefix = "&b[dejiplus] &f";

    // ** Listener **
    // ServerListener serverListener = new ServerListener(this);

    // ** Commands **
    private DejiplusCommandHandler cmdHandler;
    //private List<BaseCommand> commands = new ArrayList<BaseCommand>();

    // ** Private Classes **
    private ConfigurationManager config;
    // private TaskManager taskManager;

    // ** Static **
    // private static Database database;

    // ** Instance **
    private static Dejiplus instance;

    // ** Hookup Plugins **
    // private static Vault vault = null;
    // private static Economy economy = null;

    /**
     * プラグイン起動処理
     */
    @Override
    public void onEnable() {
        instance = this;
        LogUtil.init(this);

        PluginManager pm = getServer().getPluginManager();
        config = new ConfigurationManager(this);

        // loadconfig
        try {
            config.loadConfig(true);
        } catch (Exception ex) {
            log.warning(logPrefix
                    + "an error occured while trying to load the config file.");
            ex.printStackTrace();
        }

        /*
         * if (config.getUseVault()) { config.setUseVault(setupVault()); }
         */

        // プラグインを無効にした場合進まないようにする
        if (!pm.isPluginEnabled(this)) {
            return;
        }

        // Regist Listeners
        pm.registerEvents(new BlockListener(this), this);
        pm.registerEvents(new PlayerListener(this), this);
        pm.registerEvents(new EntityListener(this), this);

        // コマンド登録
        cmdHandler = new DejiplusCommandHandler(this);
        registerCommands();

        // database
        // database = new Database(this);
        // database.createStructure();

        // manager
        // taskManager = new TaskManager(this);
        // manager = new AdvertiseManager(this);

        // task start
        // taskManager.setSchedule(true, null);

        // メッセージ表示
        PluginDescriptionFile pdfFile = this.getDescription();
        log.info("[" + pdfFile.getName() + "] version " + pdfFile.getVersion()
                + " is enabled!");

        setupMetrics(); // mcstats
    }

    /**
     * プラグイン停止処理
     */
    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);

        // メッセージ表示
        PluginDescriptionFile pdfFile = this.getDescription();
        log.info("[" + pdfFile.getName() + "] version " + pdfFile.getVersion()
                + " is disabled!");
    }

    /**
     * コマンドを登録
     */
    private void registerCommands() {
        List<BaseCommand> cmds = new ArrayList<BaseCommand>();

        cmds.add(new CommandDejiplus());

        for (final BaseCommand cmd : cmds) {
            cmdHandler.registerCommand(cmd);
        }
    }

    /**
     * データベースを返す
     * 
     * @return Database
     */
    /*
     * public static Database getDatabases(){ return database; }
     */

    /**
     * Metricsセットアップ
     */
    private void setupMetrics() {
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException ex) {
            log.warning(logPrefix + "cant send metrics data!");
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command,
            final String label, final String[] args) {
        return cmdHandler.onCommand(sender, command, label, args);
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender,
            final Command command, final String alias, final String[] args) {
        return cmdHandler.onTabComplete(sender, command, alias, args);
    }

    /**
     * デバッグログ
     * 
     * @param msg
     */
    public void debug(final String msg) {
        if (config.isDebug()) {
            log.info(logPrefix + "[DEBUG]" + msg);
        }
    }

    /* getter */
    /**
     * 設定マネージャを返す
     * 
     * @return ConfigurationManager
     */
    public ConfigurationManager getConfigs() {
        return config;
    }

    /**
     * インスタンスを返す
     * 
     * @return Dejiplusインスタンス
     */
    public static Dejiplus getInstance() {
        return instance;
    }
}