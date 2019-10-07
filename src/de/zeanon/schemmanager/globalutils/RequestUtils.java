package de.zeanon.schemmanager.globalutils;

import de.zeanon.schemmanager.SchemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * a Global WorldEditVersionRequestUtils Class
 */
public class RequestUtils {

    private static final ArrayList<String> disableRequests = new ArrayList<>();
    private static final ArrayList<String> updateRequests = new ArrayList<>();


    static void addDisableRequest(Player p) {
        disableRequests.add(p.getUniqueId().toString());
    }

    public static void removeDisableRequest(Player p) {
        disableRequests.remove(p.getUniqueId().toString());
    }

    static boolean checkDisableRequest(Player p) {
        return disableRequests.contains(p.getUniqueId().toString());
    }


    static void addUpdateRequest(Player p) {
        updateRequests.add(p.getUniqueId().toString());
    }

    public static void removeUpdateRequest(Player p) {
        updateRequests.remove(p.getUniqueId().toString());
    }

    static boolean checkUpdateRequest(Player p) {
        return updateRequests.contains(p.getUniqueId().toString());
    }


    public static void disable() {
        Bukkit.getPluginManager().disablePlugin(SchemManager.getInstance());
    }
}
