package de.zeanon.schemmanager.worldeditversion.utils;


import java.util.HashMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorldEditVersionRequestUtils {

	private static HashMap<String, String> deleteRequests = new HashMap<>();
	private static HashMap<String, String> deleteFolderRequests = new HashMap<>();
	private static HashMap<String, String> renameRequests = new HashMap<>();
	private static HashMap<String, String> renameFolderRequests = new HashMap<>();
	private static HashMap<String, String> overwriteRequests = new HashMap<>();


	public static void addDeleteRequest(final Player p, final String name) {
		deleteRequests.put(p.getUniqueId().toString(), name);
	}

	public static void removeDeleteRequest(final Player p) {
		deleteRequests.remove(p.getUniqueId().toString());
	}

	public static boolean checkDeleteRequest(final Player p, final String name) {
		if (deleteRequests.containsKey(p.getUniqueId().toString())) {
			return deleteRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
		}
		return false;
	}


	public static void addDeleteFolderRequest(final Player p, final String name) {
		deleteFolderRequests.put(p.getUniqueId().toString(), name);
	}

	public static void removeDeleteFolderRequest(final Player p) {
		deleteFolderRequests.remove(p.getUniqueId().toString());
	}

	public static boolean checkDeleteFolderRequest(final Player p, final String name) {
		if (deleteFolderRequests.containsKey(p.getUniqueId().toString())) {
			return deleteFolderRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
		} else {
			return false;
		}
	}


	public static void addRenameRequest(final Player p, final String name) {
		renameRequests.put(p.getUniqueId().toString(), name);
	}

	public static void removeRenameRequest(final Player p) {
		renameRequests.remove(p.getUniqueId().toString());
	}

	public static boolean checkRenameRequest(final Player p, final String name) {
		if (renameRequests.containsKey(p.getUniqueId().toString())) {
			return renameRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
		} else {
			return false;
		}
	}


	public static void addRenameFolderRequest(final Player p, final String name) {
		renameFolderRequests.put(p.getUniqueId().toString(), name);
	}

	public static void removeRenameFolderRequest(final Player p) {
		renameFolderRequests.remove(p.getUniqueId().toString());
	}

	public static boolean checkRenameFolderRequest(final Player p, final String name) {
		if (renameFolderRequests.containsKey(p.getUniqueId().toString())) {
			return renameFolderRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
		} else {
			return false;
		}
	}


	public static void addOverwriteRequest(final Player p, final String name) {
		overwriteRequests.put(p.getUniqueId().toString(), name);
	}

	public static void removeOverWriteRequest(final Player p) {
		overwriteRequests.remove(p.getUniqueId().toString());
	}

	public static boolean checkOverWriteRequest(final Player p, final String name) {
		if (overwriteRequests.containsKey(p.getUniqueId().toString())) {
			return overwriteRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
		} else {
			return false;
		}
	}
}