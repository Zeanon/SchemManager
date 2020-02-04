package de.zeanon.schemmanager.worldeditmode.utils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class WorldEditModeRequestUtils {

	private final @NotNull Map<UUID, String> deleteRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<UUID, String> deleteFolderRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<UUID, String> renameRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<UUID, String> renameFolderRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<UUID, String> overwriteRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<UUID, String> copyRequest = new ConcurrentHashMap<>();
	private final @NotNull Map<UUID, String> copyFolderRequest = new ConcurrentHashMap<>();


	public void addCopyRequest(final @NotNull UUID uuid, final String name) {
		WorldEditModeRequestUtils.copyRequest.put(uuid, name);
	}

	public void removeCopyRequest(final @NotNull UUID uuid) {
		WorldEditModeRequestUtils.copyRequest.remove(uuid);
	}

	public boolean checkCopyRequest(final @NotNull UUID uuid, final String name) {
		return WorldEditModeRequestUtils.copyRequest.containsKey(uuid)
			   && WorldEditModeRequestUtils.copyRequest.get(uuid).equalsIgnoreCase(name);
	}

	public void addCopyFolderRequest(final @NotNull UUID uuid, final String name) {
		WorldEditModeRequestUtils.copyFolderRequest.put(uuid, name);
	}

	public void removeCopyFolderRequest(final @NotNull UUID uuid) {
		WorldEditModeRequestUtils.copyFolderRequest.remove(uuid);
	}

	public boolean checkCopyFolderRequest(final @NotNull UUID uuid, final String name) {
		return WorldEditModeRequestUtils.copyFolderRequest.containsKey(uuid)
			   && WorldEditModeRequestUtils.copyFolderRequest.get(uuid).equalsIgnoreCase(name);
	}

	public void addDeleteRequest(final @NotNull UUID uuid, final String name) {
		WorldEditModeRequestUtils.deleteRequests.put(uuid, name);
	}

	public void removeDeleteRequest(final @NotNull UUID uuid) {
		WorldEditModeRequestUtils.deleteRequests.remove(uuid);
	}

	public boolean checkDeleteRequest(final @NotNull UUID uuid, final String name) {
		return WorldEditModeRequestUtils.deleteRequests.containsKey(uuid)
			   && WorldEditModeRequestUtils.deleteRequests.get(uuid).equalsIgnoreCase(name);
	}


	public void addDeleteFolderRequest(final @NotNull UUID uuid, final String name) {
		WorldEditModeRequestUtils.deleteFolderRequests.put(uuid, name);
	}

	public void removeDeleteFolderRequest(final @NotNull UUID uuid) {
		WorldEditModeRequestUtils.deleteFolderRequests.remove(uuid);
	}

	public boolean checkDeleteFolderRequest(final @NotNull UUID uuid, final String name) {
		return WorldEditModeRequestUtils.deleteFolderRequests.containsKey(uuid)
			   && WorldEditModeRequestUtils.deleteFolderRequests.get(uuid).equalsIgnoreCase(name);
	}


	public void addRenameRequest(final @NotNull UUID uuid, final String name) {
		WorldEditModeRequestUtils.renameRequests.put(uuid, name);
	}

	public void removeRenameRequest(final @NotNull UUID uuid) {
		WorldEditModeRequestUtils.renameRequests.remove(uuid);
	}

	public boolean checkRenameRequest(final @NotNull UUID uuid, final String name) {
		return WorldEditModeRequestUtils.renameRequests.containsKey(uuid)
			   && WorldEditModeRequestUtils.renameRequests.get(uuid).equalsIgnoreCase(name);
	}


	public void addRenameFolderRequest(final @NotNull UUID uuid, final String name) {
		WorldEditModeRequestUtils.renameFolderRequests.put(uuid, name);
	}

	public void removeRenameFolderRequest(final @NotNull UUID uuid) {
		WorldEditModeRequestUtils.renameFolderRequests.remove(uuid);
	}

	public boolean checkRenameFolderRequest(final @NotNull UUID uuid, final String name) {
		return WorldEditModeRequestUtils.renameFolderRequests.containsKey(uuid)
			   && WorldEditModeRequestUtils.renameFolderRequests.get(uuid).equalsIgnoreCase(name);
	}


	public void addOverwriteRequest(final @NotNull UUID uuid, final String name) {
		WorldEditModeRequestUtils.overwriteRequests.put(uuid, name);
	}

	public void removeOverWriteRequest(final @NotNull UUID uuid) {
		WorldEditModeRequestUtils.overwriteRequests.remove(uuid);
	}

	public boolean checkOverWriteRequest(final @NotNull UUID uuid, final String name) {
		return WorldEditModeRequestUtils.overwriteRequests.containsKey(uuid)
			   && WorldEditModeRequestUtils.overwriteRequests.get(uuid).equalsIgnoreCase(name);
	}
}