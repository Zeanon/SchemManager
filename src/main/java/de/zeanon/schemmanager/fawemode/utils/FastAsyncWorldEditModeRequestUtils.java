package de.zeanon.schemmanager.fawemode.utils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class FastAsyncWorldEditModeRequestUtils {

	private final @NotNull Map<UUID, String> deleteRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<UUID, String> deleteFolderRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<UUID, String> renameRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<UUID, String> renameFolderRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<UUID, String> overwriteRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<UUID, String> copyRequest = new ConcurrentHashMap<>();
	private final @NotNull Map<UUID, String> copyFolderRequest = new ConcurrentHashMap<>();


	public void addCopyRequest(final @NotNull UUID uuid, final String name) {
		FastAsyncWorldEditModeRequestUtils.copyRequest.put(uuid, name);
	}

	public void removeCopyRequest(final @NotNull UUID uuid) {
		FastAsyncWorldEditModeRequestUtils.copyRequest.remove(uuid);
	}

	public boolean checkCopyRequest(final @NotNull UUID uuid, final String name) {
		return FastAsyncWorldEditModeRequestUtils.copyRequest.containsKey(uuid)
			   && FastAsyncWorldEditModeRequestUtils.copyRequest.get(uuid).equalsIgnoreCase(name);
	}

	public void addCopyFolderRequest(final @NotNull UUID uuid, final String name) {
		FastAsyncWorldEditModeRequestUtils.copyFolderRequest.put(uuid, name);
	}

	public void removeCopyFolderRequest(final @NotNull UUID uuid) {
		FastAsyncWorldEditModeRequestUtils.copyFolderRequest.remove(uuid);
	}

	public boolean checkCopyFolderRequest(final @NotNull UUID uuid, final String name) {
		return FastAsyncWorldEditModeRequestUtils.copyFolderRequest.containsKey(uuid)
			   && FastAsyncWorldEditModeRequestUtils.copyFolderRequest.get(uuid).equalsIgnoreCase(name);
	}

	public void addDeleteRequest(final @NotNull UUID uuid, final String name) {
		FastAsyncWorldEditModeRequestUtils.deleteRequests.put(uuid, name);
	}

	public void removeDeleteRequest(final @NotNull UUID uuid) {
		FastAsyncWorldEditModeRequestUtils.deleteRequests.remove(uuid);
	}

	public boolean checkDeleteRequest(final @NotNull UUID uuid, final String name) {
		return FastAsyncWorldEditModeRequestUtils.deleteRequests.containsKey(uuid)
			   && FastAsyncWorldEditModeRequestUtils.deleteRequests.get(uuid).equalsIgnoreCase(name);
	}


	public void addDeleteFolderRequest(final @NotNull UUID uuid, final String name) {
		FastAsyncWorldEditModeRequestUtils.deleteFolderRequests.put(uuid, name);
	}

	public void removeDeleteFolderRequest(final @NotNull UUID uuid) {
		FastAsyncWorldEditModeRequestUtils.deleteFolderRequests.remove(uuid);
	}

	public boolean checkDeleteFolderRequest(final @NotNull UUID uuid, final String name) {
		return FastAsyncWorldEditModeRequestUtils.deleteFolderRequests.containsKey(uuid)
			   && FastAsyncWorldEditModeRequestUtils.deleteFolderRequests.get(uuid).equalsIgnoreCase(name);
	}


	public void addRenameRequest(final @NotNull UUID uuid, final String name) {
		FastAsyncWorldEditModeRequestUtils.renameRequests.put(uuid, name);
	}

	public void removeRenameRequest(final @NotNull UUID uuid) {
		FastAsyncWorldEditModeRequestUtils.renameRequests.remove(uuid);
	}

	public boolean checkRenameRequest(final @NotNull UUID uuid, final String name) {
		return FastAsyncWorldEditModeRequestUtils.renameRequests.containsKey(uuid)
			   && FastAsyncWorldEditModeRequestUtils.renameRequests.get(uuid).equalsIgnoreCase(name);
	}


	public void addRenameFolderRequest(final @NotNull UUID uuid, final String name) {
		FastAsyncWorldEditModeRequestUtils.renameFolderRequests.put(uuid, name);
	}

	public void removeRenameFolderRequest(final @NotNull UUID uuid) {
		FastAsyncWorldEditModeRequestUtils.renameFolderRequests.remove(uuid);
	}

	public boolean checkRenameFolderRequest(final @NotNull UUID uuid, final String name) {
		return FastAsyncWorldEditModeRequestUtils.renameFolderRequests.containsKey(uuid)
			   && FastAsyncWorldEditModeRequestUtils.renameFolderRequests.get(uuid).equalsIgnoreCase(name);
	}


	public void addOverwriteRequest(final @NotNull UUID uuid, final String name) {
		FastAsyncWorldEditModeRequestUtils.overwriteRequests.put(uuid, name);
	}

	public void removeOverWriteRequest(final @NotNull UUID uuid) {
		FastAsyncWorldEditModeRequestUtils.overwriteRequests.remove(uuid);
	}

	public boolean checkOverWriteRequest(final @NotNull UUID uuid, final String name) {
		return FastAsyncWorldEditModeRequestUtils.overwriteRequests.containsKey(uuid)
			   && FastAsyncWorldEditModeRequestUtils.overwriteRequests.get(uuid).equalsIgnoreCase(name);
	}
}