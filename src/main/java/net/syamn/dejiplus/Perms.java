/**
 * dejiplus - Package: net.syamn.dejiplus Created: 2012/12/03 2:36:49
 */
package net.syamn.dejiplus;

import org.bukkit.permissions.Permissible;

/**
 * Perms (Perms.java)
 * 
 * @author syam(syamn)
 */
public enum Perms {
    /* 権限ノード */
    // User Permission
    SPAWNER_DROP("user.dropspawner"),
    
    // GeoIP
    GEOIP_SEND("geoip.send"),
    GEOIP_HIDE("geoip.hide"),
    
    // Admin Permission
    RELOAD("admin.reload"),
    EDIT_ITEM("admin.edititem"),
    ;

    // ノードヘッダー
    final String HEADER = "dejiplus.";
    private String node;

    /**
     * コンストラクタ
     * 
     * @param node
     *            権限ノード
     */
    Perms(final String node) {
        this.node = HEADER + node;
    }

    /**
     * 指定したプレイヤーが権限を持っているか
     * 
     * @param player
     *            Permissible. Player, CommandSender etc
     * @return boolean
     */
    public boolean has(final Permissible perm) {
        if (perm == null)
            return false;
        return perm.hasPermission(node); // only support SuperPerms
    }
}
