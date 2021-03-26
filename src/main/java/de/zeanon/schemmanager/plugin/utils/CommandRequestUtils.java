package de.zeanon.schemmanager.plugin.utils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class CommandRequestUtils {

	private final @NotNull Map<String, String> deleteRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<String, String> deleteFolderRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<String, String> renameRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<String, String> renameFolderRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<String, String> overwriteRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<String, String> downloadRequests = new ConcurrentHashMap<>();
	private final @NotNull Map<String, String> copyRequest = new ConcurrentHashMap<>();
	private final @NotNull Map<String, String> copyFolderRequest = new ConcurrentHashMap<>();


	public void addCopyRequest(final @NotNull UUID uuid, final String name) {
		CommandRequestUtils.copyRequest.put(uuid.toString(), name);
	}

	public void removeCopyRequest(final @NotNull UUID uuid) {
		CommandRequestUtils.copyRequest.remove(uuid.toString());
	}

	public boolean checkCopyRequest(final @NotNull UUID uuid, final String name) {
		return CommandRequestUtils.copyRequest.containsKey(uuid.toString())
			   && CommandRequestUtils.copyRequest.get(uuid.toString()).equalsIgnoreCase(name);
	}

	public void addCopyFolderRequest(final @NotNull UUID uuid, final String name) {
		CommandRequestUtils.copyFolderRequest.put(uuid.toString(), name);
	}

	public void removeCopyFolderRequest(final @NotNull UUID uuid) {
		CommandRequestUtils.copyFolderRequest.remove(uuid.toString());
	}

	public boolean checkCopyFolderRequest(final @NotNull UUID uuid, final String name) {
		return CommandRequestUtils.copyFolderRequest.containsKey(uuid.toString())
			   && CommandRequestUtils.copyFolderRequest.get(uuid.toString()).equalsIgnoreCase(name);
	}

	public void addDeleteRequest(final @NotNull UUID uuid, final String name) {
		CommandRequestUtils.deleteRequests.put(uuid.toString(), name);
	}

	public void removeDeleteRequest(final @NotNull UUID uuid) {
		CommandRequestUtils.deleteRequests.remove(uuid.toString());
	}

	public boolean checkDeleteRequest(final @NotNull UUID uuid, final String name) {
		return CommandRequestUtils.deleteRequests.containsKey(uuid.toString())
			   && CommandRequestUtils.deleteRequests.get(uuid.toString()).equalsIgnoreCase(name);
	}


	public void addDeleteFolderRequest(final @NotNull UUID uuid, final String name) {
		CommandRequestUtils.deleteFolderRequests.put(uuid.toString(), name);
	}

	public void removeDeleteFolderRequest(final @NotNull UUID uuid) {
		CommandRequestUtils.deleteFolderRequests.remove(uuid.toString());
	}

	public boolean checkDeleteFolderRequest(final @NotNull UUID uuid, final String name) {
		return CommandRequestUtils.deleteFolderRequests.containsKey(uuid.toString())
			   && CommandRequestUtils.deleteFolderRequests.get(uuid.toString()).equalsIgnoreCase(name);
	}


	public void addRenameRequest(final @NotNull UUID uuid, final String name) {
		CommandRequestUtils.renameRequests.put(uuid.toString(), name);
	}

	public void removeRenameRequest(final @NotNull UUID uuid) {
		CommandRequestUtils.renameRequests.remove(uuid.toString());
	}

	public boolean checkRenameRequest(final @NotNull UUID uuid, final String name) {
		return CommandRequestUtils.renameRequests.containsKey(uuid.toString())
			   && CommandRequestUtils.renameRequests.get(uuid.toString()).equalsIgnoreCase(name);
	}


	public void addRenameFolderRequest(final @NotNull UUID uuid, final String name) {
		CommandRequestUtils.renameFolderRequests.put(uuid.toString(), name);
	}

	public void removeRenameFolderRequest(final @NotNull UUID uuid) {
		CommandRequestUtils.renameFolderRequests.remove(uuid.toString());
	}

	public boolean checkRenameFolderRequest(final @NotNull UUID uuid, final String name) {
		return CommandRequestUtils.renameFolderRequests.containsKey(uuid.toString())
			   && CommandRequestUtils.renameFolderRequests.get(uuid.toString()).equalsIgnoreCase(name);
	}


	public void addOverwriteRequest(final @NotNull UUID uuid, final String name) {
		CommandRequestUtils.overwriteRequests.put(uuid.toString(), name);
	}

	public void removeOverWriteRequest(final @NotNull UUID uuid) {
		CommandRequestUtils.overwriteRequests.remove(uuid.toString());
	}

	public boolean checkOverWriteRequest(final @NotNull UUID uuid, final String name) {
		return CommandRequestUtils.overwriteRequests.containsKey(uuid.toString())
			   && CommandRequestUtils.overwriteRequests.get(uuid.toString()).equalsIgnoreCase(name);
	}

	public void addDownloadRequest(final @NotNull UUID uuid, final String name) {
		CommandRequestUtils.downloadRequests.put(uuid.toString(), name);
	}

	public void removeDownloadRequest(final @NotNull UUID uuid) {
		CommandRequestUtils.downloadRequests.remove(uuid.toString());
	}

	public boolean checkDownloadRequest(final @NotNull UUID uuid, final @NotNull String name) {
		return CommandRequestUtils.downloadRequests.containsKey(uuid.toString())
			   && CommandRequestUtils.downloadRequests.get(uuid.toString()).equalsIgnoreCase(name);
	}
}