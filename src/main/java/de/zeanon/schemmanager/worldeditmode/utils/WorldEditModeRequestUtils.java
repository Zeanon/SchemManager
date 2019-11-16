package de.zeanon.schemmanager.worldeditmode.utils;

import java.util.HashMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
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


	public static void addCopyRequest(@NotNull final Player p, final String name) {
		copyRequest.put(p.getUniqueId().toString(), name);
	}

	public static void removeCopyRequest(@NotNull final Player p) {
		copyRequest.remove(p.getUniqueId().toString());
	}

	public static boolean checkCopyRequest(@NotNull final Player p, final String name) {
		return copyRequest.containsKey(p.getUniqueId().toString())
			   && copyRequest.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}

	public static void addCopyFolderRequest(@NotNull final Player p, final String name) {
		copyFolderRequest.put(p.getUniqueId().toString(), name);
	}

	public static void removeCopyFolderRequest(@NotNull final Player p) {
		copyFolderRequest.remove(p.getUniqueId().toString());
	}

	public static boolean checkCopyFolderRequest(@NotNull final Player p, final String name) {
		return copyFolderRequest.containsKey(p.getUniqueId().toString())
			   && copyFolderRequest.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}

	public static void addDeleteRequest(@NotNull final Player p, final String name) {
		deleteRequests.put(p.getUniqueId().toString(), name);
	}

	public static void removeDeleteRequest(@NotNull final Player p) {
		deleteRequests.remove(p.getUniqueId().toString());
	}

	public static boolean checkDeleteRequest(@NotNull final Player p, final String name) {
		return deleteRequests.containsKey(p.getUniqueId().toString())
			   && deleteRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}


	public static void addDeleteFolderRequest(@NotNull final Player p, final String name) {
		deleteFolderRequests.put(p.getUniqueId().toString(), name);
	}

	public static void removeDeleteFolderRequest(@NotNull final Player p) {
		deleteFolderRequests.remove(p.getUniqueId().toString());
	}

	public static boolean checkDeleteFolderRequest(@NotNull final Player p, final String name) {
		return deleteFolderRequests.containsKey(p.getUniqueId().toString())
			   && deleteFolderRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}


	public static void addRenameRequest(@NotNull final Player p, final String name) {
		renameRequests.put(p.getUniqueId().toString(), name);
	}

	public static void removeRenameRequest(@NotNull final Player p) {
		renameRequests.remove(p.getUniqueId().toString());
	}

	public static boolean checkRenameRequest(@NotNull final Player p, final String name) {
		return renameRequests.containsKey(p.getUniqueId().toString())
			   && renameRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}


	public static void addRenameFolderRequest(@NotNull final Player p, final String name) {
		renameFolderRequests.put(p.getUniqueId().toString(), name);
	}

	public static void removeRenameFolderRequest(@NotNull final Player p) {
		renameFolderRequests.remove(p.getUniqueId().toString());
	}

	public static boolean checkRenameFolderRequest(@NotNull final Player p, final String name) {
		return renameFolderRequests.containsKey(p.getUniqueId().toString())
			   && renameFolderRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}


	public static void addOverwriteRequest(@NotNull final Player p, final String name) {
		overwriteRequests.put(p.getUniqueId().toString(), name);
	}

	public static void removeOverWriteRequest(@NotNull final Player p) {
		overwriteRequests.remove(p.getUniqueId().toString());
	}

	public static boolean checkOverWriteRequest(@NotNull final Player p, final String name) {
		return overwriteRequests.containsKey(p.getUniqueId().toString())
			   && overwriteRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}
}