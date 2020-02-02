package de.zeanon.schemmanager.worldeditmode.utils;

import java.util.HashMap;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class WorldEditModeRequestUtils {

	private final @NotNull HashMap<String, String> deleteRequests = new HashMap<>();
	private final @NotNull HashMap<String, String> deleteFolderRequests = new HashMap<>();
	private final @NotNull HashMap<String, String> renameRequests = new HashMap<>();
	private final @NotNull HashMap<String, String> renameFolderRequests = new HashMap<>();
	private final @NotNull HashMap<String, String> overwriteRequests = new HashMap<>();
	private final @NotNull HashMap<String, String> copyRequest = new HashMap<>();
	private final @NotNull HashMap<String, String> copyFolderRequest = new HashMap<>();


	public void addCopyRequest(final @NotNull Player p, final String name) {
		WorldEditModeRequestUtils.copyRequest.put(p.getUniqueId().toString(), name);
	}

	public void removeCopyRequest(final @NotNull Player p) {
		WorldEditModeRequestUtils.copyRequest.remove(p.getUniqueId().toString());
	}

	public boolean checkCopyRequest(final @NotNull Player p, final String name) {
		return WorldEditModeRequestUtils.copyRequest.containsKey(p.getUniqueId().toString())
			   && WorldEditModeRequestUtils.copyRequest.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}

	public void addCopyFolderRequest(final @NotNull Player p, final String name) {
		WorldEditModeRequestUtils.copyFolderRequest.put(p.getUniqueId().toString(), name);
	}

	public void removeCopyFolderRequest(final @NotNull Player p) {
		WorldEditModeRequestUtils.copyFolderRequest.remove(p.getUniqueId().toString());
	}

	public boolean checkCopyFolderRequest(final @NotNull Player p, final String name) {
		return WorldEditModeRequestUtils.copyFolderRequest.containsKey(p.getUniqueId().toString())
			   && WorldEditModeRequestUtils.copyFolderRequest.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}

	public void addDeleteRequest(final @NotNull Player p, final String name) {
		WorldEditModeRequestUtils.deleteRequests.put(p.getUniqueId().toString(), name);
	}

	public void removeDeleteRequest(final @NotNull Player p) {
		WorldEditModeRequestUtils.deleteRequests.remove(p.getUniqueId().toString());
	}

	public boolean checkDeleteRequest(final @NotNull Player p, final String name) {
		return WorldEditModeRequestUtils.deleteRequests.containsKey(p.getUniqueId().toString())
			   && WorldEditModeRequestUtils.deleteRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}


	public void addDeleteFolderRequest(final @NotNull Player p, final String name) {
		WorldEditModeRequestUtils.deleteFolderRequests.put(p.getUniqueId().toString(), name);
	}

	public void removeDeleteFolderRequest(final @NotNull Player p) {
		WorldEditModeRequestUtils.deleteFolderRequests.remove(p.getUniqueId().toString());
	}

	public boolean checkDeleteFolderRequest(final @NotNull Player p, final String name) {
		return WorldEditModeRequestUtils.deleteFolderRequests.containsKey(p.getUniqueId().toString())
			   && WorldEditModeRequestUtils.deleteFolderRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}


	public void addRenameRequest(final @NotNull Player p, final String name) {
		WorldEditModeRequestUtils.renameRequests.put(p.getUniqueId().toString(), name);
	}

	public void removeRenameRequest(final @NotNull Player p) {
		WorldEditModeRequestUtils.renameRequests.remove(p.getUniqueId().toString());
	}

	public boolean checkRenameRequest(final @NotNull Player p, final String name) {
		return WorldEditModeRequestUtils.renameRequests.containsKey(p.getUniqueId().toString())
			   && WorldEditModeRequestUtils.renameRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}


	public void addRenameFolderRequest(final @NotNull Player p, final String name) {
		WorldEditModeRequestUtils.renameFolderRequests.put(p.getUniqueId().toString(), name);
	}

	public void removeRenameFolderRequest(final @NotNull Player p) {
		WorldEditModeRequestUtils.renameFolderRequests.remove(p.getUniqueId().toString());
	}

	public boolean checkRenameFolderRequest(final @NotNull Player p, final String name) {
		return WorldEditModeRequestUtils.renameFolderRequests.containsKey(p.getUniqueId().toString())
			   && WorldEditModeRequestUtils.renameFolderRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}


	public void addOverwriteRequest(final @NotNull Player p, final String name) {
		WorldEditModeRequestUtils.overwriteRequests.put(p.getUniqueId().toString(), name);
	}

	public void removeOverWriteRequest(final @NotNull Player p) {
		WorldEditModeRequestUtils.overwriteRequests.remove(p.getUniqueId().toString());
	}

	public boolean checkOverWriteRequest(final @NotNull Player p, final String name) {
		return WorldEditModeRequestUtils.overwriteRequests.containsKey(p.getUniqueId().toString())
			   && WorldEditModeRequestUtils.overwriteRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
	}
}