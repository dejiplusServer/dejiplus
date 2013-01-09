/**
 * dejiplus - Package: net.syamn.dejiplus.feature
 * Created: 2013/01/10 6:18:11
 */
package net.syamn.dejiplus.feature;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.zip.GZIPInputStream;

import net.syamn.dejiplus.ConfigurationManager;
import net.syamn.dejiplus.Dejiplus;
import net.syamn.utils.LogUtil;

import org.bukkit.entity.Player;

import com.maxmind.geoip.Country;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;

/**
 * GeoIP (GeoIP.java)
 * @author syam(syamn)
 */
public class GeoIP {
    private static GeoIP instance = null;
    public static GeoIP getInstance(){
        return instance;
    }
    public static void dispose(){
        if (instance != null){
            instance.ls.close();
            instance.ls = null;
        }
        instance = null;
    }
    
    private Dejiplus plugin;
    private File database;
    private LookupService ls;
    
    /**
     * コンストラクタ
     * @param plugin
     */
    public GeoIP(final Dejiplus plugin){
        instance = this;
        this.plugin = plugin;
    }
    
    /**
     * 初期化
     */
    public void init(){
        final File parent = new File(plugin.getDataFolder(), "geoIp");
        if (!parent.exists()){
            parent.mkdirs();
        }
        
        final ConfigurationManager conf = plugin.getConfigs();
        final boolean isCity = conf.getUseCityDB();
        database = new File(parent, (isCity) ? "GeoIPCity.dat" : "GeoIPCountry.dat");
        
        if (!database.exists()){
            if (conf.getDownloadMissingDB()){
                // auto download db if missing
                if (isCity){
                    downloadDatabase(conf.getCityDBurl(), database);
                }else{
                    downloadDatabase(conf.getCountryDBurl(), database);
                }
            }else{
                LogUtil.warning("Could not find GeoIP database!");
                return;
            }
        }
        
        try{
            ls = new LookupService(database);
        }catch(IOException ex){
            LogUtil.warning("Could not read GeoIP database: " + ex.getMessage());
        }
    }
    
    /**
     * プレイヤーのGeoIPメッセージを返す
     */
    public String getGeoIpString(final InetAddress addr, final boolean simple){
        // Only two-char CountryCode
        if (simple){
            return ls.getCountry(addr).getCode();
        }
        // Country name / city name
        else if (plugin.getConfigs().getUseCityDB()){
            final StringBuilder sb = new StringBuilder();
            final Location loc = ls.getLocation(addr);
            if (loc == null) {
                final Country country = ls.getCountry(addr);
                return (country == null) ? null : country.getName();
            }
            
            sb.append(loc.countryName);
            
            final String region = regionName.regionNameByCode(loc.countryCode, loc.region);
            if (region != null){
                sb.append(", ").append(region);
            }
            
            if (loc.city != null){
                sb.append(", ").append(loc.city);
            }
            
            return sb.toString();
        }
        // Only country name
        else{
            return ls.getCountry(addr).getName();
        }
    }
    public String getGeoIpString(final Player player, final boolean simple){
        if (player == null || player.getAddress() == null || player.getAddress().getAddress() == null){
            return null;
        }
        return getGeoIpString(player.getAddress().getAddress(), simple);
    }
    public String getGeoIpString(final Player player){
        return getGeoIpString(player, false);
    }
    public String getGeoIpString(String addrStr){
        if (addrStr == null){
            return null;
        }
        if (addrStr.startsWith("/")){
            addrStr = addrStr.substring(1);
        }
        try {
            return getGeoIpString(InetAddress.getByName(addrStr), false);
        } catch (UnknownHostException ignore) {
            return null;
        }
    }
    
    
    /**
     * LookupServiceを返す
     * @return
     */
    public LookupService getLookupService(){
        return this.ls;
    }
    
    /**
     * GeoIPデータベースのダウンロードを行う
     * @param url
     * @param fileName
     */
    private static void downloadDatabase(final String url, final File file){
        if (url == null || url.isEmpty()){
            LogUtil.warning("Could not download GeoIP database! Url is empty!");
            return;
        }
        
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        
        InputStream input = null;
        OutputStream output = null;
        try{
            LogUtil.info("Downloading GeoIP databases...: " + file.getName());
            
            final URL dlUrl = new URL(url);
            final URLConnection conn = dlUrl.openConnection();
            conn.setConnectTimeout(10000); // 10 secs timeout
            
            input = conn.getInputStream();
            if (url.endsWith(".gz")){
                input = new GZIPInputStream(input);
            }
            
            output = new FileOutputStream(file);
            final byte[] buff = new byte[2048];
            int len = input.read(buff);
            while (len >= 0){
                output.write(buff, 0, len);
                len = input.read(buff);
            }
            
            input.close();
            output.close();
        }
        catch(MalformedURLException ex){
            LogUtil.warning("Download error (Malformed URL): " + ex.getMessage());
        }
        catch(IOException ex){
            LogUtil.warning("Download error (Connection failed): " + ex.getMessage());
        }
        finally{
            if (output != null){
                try{ output.close(); }catch(Exception ignore){}
            }
            if (input != null){
                try{ input.close(); }catch(Exception ignore){}
            }
        }
        
    }
}