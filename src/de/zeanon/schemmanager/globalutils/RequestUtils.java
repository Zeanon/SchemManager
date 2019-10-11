package de.zeanon.schemmanager.globalutils;

import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * a Global WorldEditVersionRequestUtils Class
 */
public class RequestUtils {

    private static final ArrayList<String> disableRequests = new ArrayList<>();
    private static final ArrayList<String> updateRequests = new ArrayList<>();


    static void addDisableRequest(final Player p) {
        disableRequests.add(p.getUniqueId().toString());
    }

    public static void removeDisableRequest(final Player p) {
        disableRequests.remove(p.getUniqueId().toString());
    }

    static boolean checkDisableRequest(final Player p) {
        return disableRequests.contains(p.getUniqueId().toString());
    }


    static void addUpdateRequest(final Player p) {
        updateRequests.add(p.getUniqueId().toString());
    }

    public static void removeUpdateRequest(final Player p) {
        updateRequests.remove(p.getUniqueId().toString());
    }

    static boolean checkUpdateRequest(final Player p) {
        return updateRequests.contains(p.getUniqueId().toString());
    }
}