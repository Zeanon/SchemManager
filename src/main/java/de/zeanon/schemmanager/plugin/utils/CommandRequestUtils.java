package de.zeanon.schemmanager.plugin.utils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class CommandRequestUtils {

	private final @NotNull Map<UUID, String> deleteRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<UUID, String> deleteFolderRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<UUID, String> renameRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<UUID, String> renameFolderRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<UUID, String> overwriteRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<UUID, String> copyRequest = new ConcurrentHashMap<>();
	private final @NotNull Map<UUID, String> copyFolderRequest = new ConcurrentHashMap<>();


	public void addCopyRequest(final @NotNull UUID uuid, final String name) {
		CommandRequestUtils.copyRequest.put(uuid, name);
	}

	public void removeCopyRequest(final @NotNull UUID uuid) {
		CommandRequestUtils.copyRequest.remove(uuid);
	}

	public boolean checkCopyRequest(final @NotNull UUID uuid, final String name) {
		return CommandRequestUtils.copyRequest.containsKey(uuid)
			   && CommandRequestUtils.copyRequest.get(uuid).equalsIgnoreCase(name);
	}

	public void addCopyFolderRequest(final @NotNull UUID uuid, final String name) {
		CommandRequestUtils.copyFolderRequest.put(uuid, name);
	}

	public void removeCopyFolderRequest(final @NotNull UUID uuid) {
		CommandRequestUtils.copyFolderRequest.remove(uuid);
	}

	public boolean checkCopyFolderRequest(final @NotNull UUID uuid, final String name) {
		return CommandRequestUtils.copyFolderRequest.containsKey(uuid)
			   && CommandRequestUtils.copyFolderRequest.get(uuid).equalsIgnoreCase(name);
	}

	public void addDeleteRequest(final @NotNull UUID uuid, final String name) {
		CommandRequestUtils.deleteRequests.put(uuid, name);
	}

	public void removeDeleteRequest(final @NotNull UUID uuid) {
		CommandRequestUtils.deleteRequests.remove(uuid);
	}

	public boolean checkDeleteRequest(final @NotNull UUID uuid, final String name) {
		return CommandRequestUtils.deleteRequests.containsKey(uuid)
			   && CommandRequestUtils.deleteRequests.get(uuid).equalsIgnoreCase(name);
	}


	public void addDeleteFolderRequest(final @NotNull UUID uuid, final String name) {
		CommandRequestUtils.deleteFolderRequests.put(uuid, name);
	}

	public void removeDeleteFolderRequest(final @NotNull UUID uuid) {
		CommandRequestUtils.deleteFolderRequests.remove(uuid);
	}

	public boolean checkDeleteFolderRequest(final @NotNull UUID uuid, final String name) {
		return CommandRequestUtils.deleteFolderRequests.containsKey(uuid)
			   && CommandRequestUtils.deleteFolderRequests.get(uuid).equalsIgnoreCase(name);
	}


	public void addRenameRequest(final @NotNull UUID uuid, final String name) {
		CommandRequestUtils.renameRequests.put(uuid, name);
	}

	public void removeRenameRequest(final @NotNull UUID uuid) {
		CommandRequestUtils.renameRequests.remove(uuid);
	}

	public boolean checkRenameRequest(final @NotNull UUID uuid, final String name) {
		return CommandRequestUtils.renameRequests.containsKey(uuid)
			   && CommandRequestUtils.renameRequests.get(uuid).equalsIgnoreCase(name);
	}


	public void addRenameFolderRequest(final @NotNull UUID uuid, final String name) {
		CommandRequestUtils.renameFolderRequests.put(uuid, name);
	}

	public void removeRenameFolderRequest(final @NotNull UUID uuid) {
		CommandRequestUtils.renameFolderRequests.remove(uuid);
	}

	public boolean checkRenameFolderRequest(final @NotNull UUID uuid, final String name) {
		return CommandRequestUtils.renameFolderRequests.containsKey(uuid)
			   && CommandRequestUtils.renameFolderRequests.get(uuid).equalsIgnoreCase(name);
	}


	public void addOverwriteRequest(final @NotNull UUID uuid, final String name) {
		CommandRequestUtils.overwriteRequests.put(uuid, name);
	}

	public void removeOverWriteRequest(final @NotNull UUID uuid) {
		CommandRequestUtils.overwriteRequests.remove(uuid);
	}

	public boolean checkOverWriteRequest(final @NotNull UUID uuid, final String name) {
		return CommandRequestUtils.overwriteRequests.containsKey(uuid)
			   && CommandRequestUtils.overwriteRequests.get(uuid).equalsIgnoreCase(name);
	}
}