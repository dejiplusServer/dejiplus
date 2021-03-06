/**
 * dejiplus - Package: net.syamn.dejiplus Created: 2012/12/03 2:31:22
 */
package net.syamn.dejiplus;

import java.io.File;
import java.util.logging.Logger;

import net.syamn.dejiplus.feature.GeoIP;
import net.syamn.utils.file.FileStructure;

import org.bukkit.configuration.file.FileConfiguration;


/**
 * ConfigurationManager (ConfigurationManager.java)
 * 
 * @author syam(syamn)
 */
public class ConfigurationManager {
    /* Current config.yml File Version! */
    private final int latestVersion = 1;

    // Logger
    private static final Logger log = Dejiplus.log;
    private static final String logPrefix = Dejiplus.logPrefix;
    private static final String msgPrefix = Dejiplus.msgPrefix;
    private final Dejiplus plugin;

    // private YamlConfiguration conf;
    private FileConfiguration conf;
    private File pluginDir;

    // hookup plugin
    private boolean useVault;

    /**
     * Constructor
     */
    public ConfigurationManager(final Dejiplus plugin) {
        this.plugin = plugin;

        this.pluginDir = this.plugin.getDataFolder();
    }

    /**
     * Load config.yml
     */
    public void loadConfig(final boolean initialLoad) throws Exception {
        // create directories
        FileStructure.createDir(pluginDir);

        // get config.yml path
        File file = new File(pluginDir, "config.yml");
        if (!file.exists()) {
            FileStructure.extractResource("/config.yml", pluginDir, false, false, plugin);
            log.info("config.yml is not found! Created default config.yml!");
        }

        plugin.reloadConfig();
        conf = plugin.getConfig();

        checkver(conf.getInt("ConfigVersion", 1));

        // check vault
        /*
         * useVault = conf.getBoolean("UseVault", false); if (!initialLoad &&
         * useVault && (plugin.getVault() == null || plugin.getEconomy() ==
         * null)) { plugin.setupVault(); }
         */
        
        // setup geoIP
        if (getUseGeoIP()){
            new GeoIP(plugin).init();
        }
    }

    /**
     * Check configuration file version
     */
    private void checkver(final int ver) {
        // compare configuration file version
        if (ver < latestVersion) {
            // first, rename old configuration
            final String destName = "oldconfig-v" + ver + ".yml";
            String srcPath = new File(pluginDir, "config.yml").getPath();
            String destPath = new File(pluginDir, destName).getPath();
            try {
                FileStructure.copyTransfer(srcPath, destPath);
                log.info("Copied old config.yml to " + destName + "!");
            } catch (Exception ex) {
                log.warning("Failed to copy old config.yml!");
            }

            // force copy config.yml and languages
            FileStructure.extractResource("/config.yml", pluginDir, true, false, plugin);
            // Language.extractLanguageFile(true);

            plugin.reloadConfig();
            conf = plugin.getConfig();

            log.info("Deleted existing configuration file and generate a new one!");
        }
    }

    /* ***** Begin Configuration Getters *********************** */

    // General
    public boolean needsSilkEnchant(){
        return conf.getBoolean("NeedsSilkEnchant", false);
    }
   
    // Messages
    public String getMessageGeoIP(){
        return conf.getString("MessageGeoIP", "&7 %PLAYER% は %LOCATION% から接続しました！");
    }
    
    // GeoIP
    public boolean getUseGeoIP(){
        return conf.getBoolean("UseGeoIP", true);
    }
    public boolean getUseCityDB(){
        return conf.getBoolean("UseCityDB", false);
    }
    public boolean getUseSimpleFormatOnJoin(){
        return conf.getBoolean("UseSimpleFormatOnJoin", false);
    }
    public boolean getDownloadMissingDB(){
        return conf.getBoolean("DownloadMissingDB", true);
    }
    public String getCountryDBurl(){
        return conf.getString("CountryDB", "http://geolite.maxmind.com/download/geoip/database/GeoLiteCountry/GeoIP.dat.gz");
    }
    public String getCityDBurl(){
        return conf.getString("CityDB", "http://geolite.maxmind.com/download/geoip/database/GeoLiteCity.dat.gz");
    }
    
    // Debug
    public boolean isDebug() {
        return conf.getBoolean("Debug", false);
    }
}