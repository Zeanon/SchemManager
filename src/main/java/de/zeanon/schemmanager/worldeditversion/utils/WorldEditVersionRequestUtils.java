package de.zeanon.schemmanager.worldeditversion.utils;

import java.util.HashMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorldEditVersionRequestUtils {

	private static final HashMap<String, String> deleteRequests = new HashMap<>();
	private static final HashMap<String, String> deleteFolderRequests = new HashMap<>();
	private static final HashMap<String, String> renameRequests = new HashMap<>();
	private static final HashMap<String, String> renameFolderRequests = new HashMap<>();
	private static final HashMap<String, String> overwriteRequests = new HashMap<>();
	private static final HashMap<String, String> copyRequest = new HashMap<>();
	private static final HashMap<String, String> copyFolderRequest = new HashMap<>();


	public static void addCopyRequest(final Player p, final String name) {
		copyRequest.put(p.getUniqueId().toString(), name);
	}

	public static void removeCopyRequest(final Player p) {
		copyRequest.remove(p.getUniqueId().toString());
	}

	public static boolean checkCopyRequest(final Player p, final String name) {
		return copyRequest.containsKey(p.getUniqueId().toString()) && copyRequest.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}

	public static void addCopyFolderRequest(final Player p, final String name) {
		copyFolderRequest.put(p.getUniqueId().toString(), name);
	}

	public static void removeCopyFolderRequest(final Player p) {
		copyFolderRequest.remove(p.getUniqueId().toString());
	}

	public static boolean checkCopyFolderRequest(final Player p, final String name) {
		return copyFolderRequest.containsKey(p.getUniqueId().toString()) && copyFolderRequest.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}

	public static void addDeleteRequest(final Player p, final String name) {
		deleteRequests.put(p.getUniqueId().toString(), name);
	}

	public static void removeDeleteRequest(final Player p) {
		deleteRequests.remove(p.getUniqueId().toString());
	}

	public static boolean checkDeleteRequest(final Player p, final String name) {
		return deleteRequests.containsKey(p.getUniqueId().toString()) && deleteRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}


	public static void addDeleteFolderRequest(final Player p, final String name) {
		deleteFolderRequests.put(p.getUniqueId().toString(), name);
	}

	public static void removeDeleteFolderRequest(final Player p) {
		deleteFolderRequests.remove(p.getUniqueId().toString());
	}

	public static boolean checkDeleteFolderRequest(final Player p, final String name) {
		return deleteFolderRequests.containsKey(p.getUniqueId().toString()) && deleteFolderRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}


	public static void addRenameRequest(final Player p, final String name) {
		renameRequests.put(p.getUniqueId().toString(), name);
	}

	public static void removeRenameRequest(final Player p) {
		renameRequests.remove(p.getUniqueId().toString());
	}

	public static boolean checkRenameRequest(final Player p, final String name) {
		return renameRequests.containsKey(p.getUniqueId().toString()) && renameRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}


	public static void addRenameFolderRequest(final Player p, final String name) {
		renameFolderRequests.put(p.getUniqueId().toString(), name);
	}

	public static void removeRenameFolderRequest(final Player p) {
		renameFolderRequests.remove(p.getUniqueId().toString());
	}

	public static boolean checkRenameFolderRequest(final Player p, final String name) {
		return renameFolderRequests.containsKey(p.getUniqueId().toString()) && renameFolderRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}


	public static void addOverwriteRequest(final Player p, final String name) {
		overwriteRequests.put(p.getUniqueId().toString(), name);
	}

	public static void removeOverWriteRequest(final Player p) {
		overwriteRequests.remove(p.getUniqueId().toString());
	}

	public static boolean checkOverWriteRequest(final Player p, final String name) {
		return overwriteRequests.containsKey(p.getUniqueId().toString()) && overwriteRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}
}