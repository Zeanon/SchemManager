package de.zeanon.schemmanager.worldeditmode.utils;

import java.util.HashMap;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class WorldEditModeRequestUtils {

	@NotNull
	private static final HashMap<String, String> deleteRequests = new HashMap<>();
	@NotNull
	private static final HashMap<String, String> deleteFolderRequests = new HashMap<>();
	@NotNull
	private static final HashMap<String, String> renameRequests = new HashMap<>();
	@NotNull
	private static final HashMap<String, String> renameFolderRequests = new HashMap<>();
	@NotNull
	private static final HashMap<String, String> overwriteRequests = new HashMap<>();
	@NotNull
	private static final HashMap<String, String> copyRequest = new HashMap<>();
	@NotNull
	private static final HashMap<String, String> copyFolderRequest = new HashMap<>();


	public static void addCopyRequest(final @NotNull Player p, final String name) {
		copyRequest.put(p.getUniqueId().toString(), name);
	}

	public static void removeCopyRequest(final @NotNull Player p) {
		copyRequest.remove(p.getUniqueId().toString());
	}

	public static boolean checkCopyRequest(final @NotNull Player p, final String name) {
		return copyRequest.containsKey(p.getUniqueId().toString())
			   && copyRequest.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}

	public static void addCopyFolderRequest(final @NotNull Player p, final String name) {
		copyFolderRequest.put(p.getUniqueId().toString(), name);
	}

	public static void removeCopyFolderRequest(final @NotNull Player p) {
		copyFolderRequest.remove(p.getUniqueId().toString());
	}

	public static boolean checkCopyFolderRequest(final @NotNull Player p, final String name) {
		return copyFolderRequest.containsKey(p.getUniqueId().toString())
			   && copyFolderRequest.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}

	public static void addDeleteRequest(final @NotNull Player p, final String name) {
		deleteRequests.put(p.getUniqueId().toString(), name);
	}

	public static void removeDeleteRequest(final @NotNull Player p) {
		deleteRequests.remove(p.getUniqueId().toString());
	}

	public static boolean checkDeleteRequest(final @NotNull Player p, final String name) {
		return deleteRequests.containsKey(p.getUniqueId().toString())
			   && deleteRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}


	public static void addDeleteFolderRequest(final @NotNull Player p, final String name) {
		deleteFolderRequests.put(p.getUniqueId().toString(), name);
	}

	public static void removeDeleteFolderRequest(final @NotNull Player p) {
		deleteFolderRequests.remove(p.getUniqueId().toString());
	}

	public static boolean checkDeleteFolderRequest(final @NotNull Player p, final String name) {
		return deleteFolderRequests.containsKey(p.getUniqueId().toString())
			   && deleteFolderRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}


	public static void addRenameRequest(final @NotNull Player p, final String name) {
		renameRequests.put(p.getUniqueId().toString(), name);
	}

	public static void removeRenameRequest(final @NotNull Player p) {
		renameRequests.remove(p.getUniqueId().toString());
	}

	public static boolean checkRenameRequest(final @NotNull Player p, final String name) {
		return renameRequests.containsKey(p.getUniqueId().toString())
			   && renameRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}


	public static void addRenameFolderRequest(final @NotNull Player p, final String name) {
		renameFolderRequests.put(p.getUniqueId().toString(), name);
	}

	public static void removeRenameFolderRequest(final @NotNull Player p) {
		renameFolderRequests.remove(p.getUniqueId().toString());
	}

	public static boolean checkRenameFolderRequest(final @NotNull Player p, final String name) {
		return renameFolderRequests.containsKey(p.getUniqueId().toString())
			   && renameFolderRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}


	public static void addOverwriteRequest(final @NotNull Player p, final String name) {
		overwriteRequests.put(p.getUniqueId().toString(), name);
	}

	public static void removeOverWriteRequest(final @NotNull Player p) {
		overwriteRequests.remove(p.getUniqueId().toString());
	}

	public static boolean checkOverWriteRequest(final @NotNull Player p, final String name) {
		return overwriteRequests.containsKey(p.getUniqueId().toString())
			   && overwriteRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}
}