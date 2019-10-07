package de.zeanon.schemmanager.worldeditversion.utils;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class WorldEditVersionRequestUtils {

    private static HashMap<String, String> deleteRequests = new HashMap<>();
    private static HashMap<String, String> deleteFolderRequests = new HashMap<>();
    private static HashMap<String, String> renameRequests = new HashMap<>();
    private static HashMap<String, String> renameFolderRequests = new HashMap<>();
    private static HashMap<String, String> overwriteRequests = new HashMap<>();


    public static void addDeleteRequest(Player p, String name) {
        deleteRequests.put(p.getUniqueId().toString(), name);
    }

    public static void removeDeleteRequest(Player p) {
        deleteRequests.remove(p.getUniqueId().toString());
    }

    public static boolean checkDeleteRequest(Player p, String name) {
        if (deleteRequests.containsKey(p.getUniqueId().toString())) {
            return deleteRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
        }
        return false;
    }


    public static void addDeleteFolderRequest(Player p, String name) {
        deleteFolderRequests.put(p.getUniqueId().toString(), name);
    }

    public static void removeDeleteFolderRequest(Player p) {
        deleteFolderRequests.remove(p.getUniqueId().toString());
    }

    public static boolean checkDeleteFolderRequest(Player p, String name) {
        if (deleteFolderRequests.containsKey(p.getUniqueId().toString())) {
            return deleteFolderRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
        } else {
            return false;
        }
    }


    public static void addRenameRequest(Player p, String name) {
        renameRequests.put(p.getUniqueId().toString(), name);
    }

    public static void removeRenameRequest(Player p) {
        renameRequests.remove(p.getUniqueId().toString());
    }

    public static boolean checkRenameRequest(Player p, String name) {
        if (renameRequests.containsKey(p.getUniqueId().toString())) {
            return renameRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
        } else {
            return false;
        }
    }


    public static void addRenameFolderRequest(Player p, String name) {
        renameFolderRequests.put(p.getUniqueId().toString(), name);
    }

    public static void removeRenameFolderRequest(Player p) {
        renameFolderRequests.remove(p.getUniqueId().toString());
    }

    public static boolean checkRenameFolderRequest(Player p, String name) {
        if (renameFolderRequests.containsKey(p.getUniqueId().toString())) {
            return renameFolderRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
        } else {
            return false;
        }
    }


    public static void addOverwriteRequest(Player p, String name) {
        overwriteRequests.put(p.getUniqueId().toString(), name);
    }

    public static void removeOverWriteRequest(Player p) {
        overwriteRequests.remove(p.getUniqueId().toString());
    }

    public static boolean checkOverWriteRequest(Player p, String name) {
        if (overwriteRequests.containsKey(p.getUniqueId().toString())) {
            return overwriteRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
        } else {
            return false;
        }
    }
}