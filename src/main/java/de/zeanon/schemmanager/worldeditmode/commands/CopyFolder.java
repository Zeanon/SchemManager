package de.zeanon.schemmanager.worldeditmode.commands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.global.utils.MessageUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeRequestUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeSchemUtils;
import de.zeanon.storagemanager.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanager.internal.utility.basic.Objects;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class CopyFolder {

	public void execute(final @NotNull String[] args, final @NotNull Player p, final @NotNull String slash, final @NotNull String schemAlias) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GREEN + "filename"
									  + ChatColor.YELLOW + ">");
						CopyFolder.copyFolderUsage(p, slash, schemAlias);
					} else if (args[2].contains("./") || args.length >= 4 && args[3].contains("./")) {
						String name = args[2].contains("./") ? args[2] : args[3];
						p.sendMessage(ChatColor.RED + "File '" + name + "'resolution error: Path is not allowed.");
						CopyFolder.copyFolderUsage(p, slash, schemAlias);
					} else if (args.length == 5
							   && !args[4].equalsIgnoreCase("confirm")
							   && !args[4].equalsIgnoreCase("deny")
							   && !WorldEditModeRequestUtils.checkRenameFolderRequest(p, args[2])) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						CopyFolder.copyFolderUsage(p, slash, schemAlias);
					} else {
						CopyFolder.onCopyFolder(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					CopyFolder.copyFolderUsage(p, slash, schemAlias);
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}

	private void onCopyFolder(final @NotNull Player p, final @NotNull String[] args) {
		try {
			final @Nullable Path schemPath = WorldEditModeSchemUtils.getSchemPath();
			final @Nullable File directoryOld = schemPath != null ? schemPath.resolve(args[2]).toFile() : null;
			final @Nullable File directoryNew = schemPath != null ? schemPath.resolve(args[3]).toFile() : null;

			if (args.length == 4) {
				if (directoryOld == null || !directoryOld.exists() || !directoryOld.isDirectory()) {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
								  ChatColor.GREEN + args[2] + ChatColor.RED + " does not exist.");
					return;
				} else if (directoryNew.exists() && directoryNew.isDirectory()) {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
								  ChatColor.GREEN + args[3] + ChatColor.RED + " already exists, the folders will be merged.");
					int id = 0;
					final @Nullable List<String> extensions = ConfigUtils.getStringList("File Extensions");
					for (final @NotNull File oldFile : BaseFileUtils.listFiles(directoryOld, true, Objects.notNull(extensions))) {
						for (final @NotNull File newFile : BaseFileUtils.listFiles(directoryNew, true, extensions)) {
							if (BaseFileUtils.removeExtension(newFile.getName())
											 .equalsIgnoreCase(BaseFileUtils.removeExtension(oldFile.getName()))
								&& newFile.toPath().relativize(directoryNew.toPath())
										  .equals(oldFile.toPath().relativize(directoryOld.toPath()))) {
								if (id == 0) {
									p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
												  ChatColor.RED + "These schematics already exist in " +
												  ChatColor.GREEN + args[3] +
												  ChatColor.RED + ", they will be overwritten:");
								}

								final @NotNull String path = FilenameUtils.separatorsToUnix(
										schemPath.toRealPath()
												 .relativize(newFile.toPath().toRealPath())
												 .toString());
								final @NotNull String shortenedRelativePath = FilenameUtils.separatorsToUnix(
										schemPath.resolve(args[3])
												 .toRealPath()
												 .relativize(newFile.toPath().toRealPath())
												 .toString());

								final @NotNull String name;
								if (BaseFileUtils.getExtension(newFile.getName()).equalsIgnoreCase(Objects.notNull(ConfigUtils.getStringList("File Extensions")).get(0))) {
									name = BaseFileUtils.removeExtension(newFile.getName());
								} else {
									name = newFile.getName();
								}

								MessageUtils.sendCommandMessage(
										ChatColor.RED + Integer.toString(id + 1) + ": ",
										ChatColor.GOLD + name
										+ ChatColor.DARK_GRAY + " ["
										+ ChatColor.GRAY + shortenedRelativePath
										+ ChatColor.DARK_GRAY + "]",
										ChatColor.RED + "Load "
										+ ChatColor.GOLD + path
										+ ChatColor.RED + " to your clipboard",
										"//schem load " + path, p);
								id++;
							}
						}
					}

					int i = 0;
					for (final @NotNull File oldFolder : BaseFileUtils.listFolders(directoryOld, true)) {
						for (final @NotNull File newFolder : BaseFileUtils.listFolders(directoryNew, true)) {
							if (newFolder.getName()
										 .equalsIgnoreCase(oldFolder.getName())
								&& newFolder.toPath().relativize(directoryNew.toPath())
											.equals(oldFolder.toPath().relativize(directoryOld.toPath()))) {

								if (i == 0) {
									p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
												  ChatColor.RED + "These folders already exist in " +
												  ChatColor.GREEN + args[3] +
												  ChatColor.RED + ", they will be merged:");
								}

								final @NotNull String name = newFolder.getName();
								final @NotNull String path = FilenameUtils.separatorsToUnix(
										schemPath.toRealPath()
												 .relativize(newFolder.toPath().toRealPath())
												 .toString());
								final @NotNull String shortenedRelativePath = FilenameUtils.separatorsToUnix(
										schemPath.resolve(args[3])
												 .toRealPath()
												 .relativize(newFolder.toPath().toRealPath())
												 .toString());
								MessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(i + 1) + ": ",
																ChatColor.GREEN + name
																+ ChatColor.DARK_GRAY + " ["
																+ ChatColor.GRAY + shortenedRelativePath
																+ ChatColor.DARK_GRAY + "]",
																ChatColor.RED + "List the schematics in "
																+ ChatColor.GREEN + path,
																"//schem list " + path, p);
								i++;
							}
						}
					}

					if (id > 0 && i > 0) {
						p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
									  ChatColor.RED + "There are already "
									  + ChatColor.DARK_PURPLE + id
									  + ChatColor.RED + " schematics and "
									  + ChatColor.DARK_PURPLE + i
									  + ChatColor.RED + " folders with the same name in "
									  + ChatColor.GREEN + args[3]
									  + ChatColor.RED + ".");
					} else if (id > 0) {
						p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
									  ChatColor.RED + "There are already "
									  + ChatColor.DARK_PURPLE + id
									  + ChatColor.RED + " schematics with the same name in "
									  + ChatColor.GREEN + args[3]
									  + ChatColor.RED + ".");
					} else if (i > 0) {
						p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
									  ChatColor.RED + "There are already "
									  + ChatColor.DARK_PURPLE + i
									  + ChatColor.RED + " folders with the same name in "
									  + ChatColor.GREEN + args[3]
									  + ChatColor.RED + ".");
					}
				}

				MessageUtils.sendBooleanMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
												ChatColor.RED + "Do you really want to copy "
												+ ChatColor.GREEN + args[2]
												+ ChatColor.RED + "?",
												"//schem copyfolder " + args[2] + " " + args[3] + " confirm",
												"//schem copyfolder " + args[2] + " " + args[3] + " deny", p);
				WorldEditModeRequestUtils.addCopyFolderRequest(p, args[2]);
			} else if (args.length == 5 && WorldEditModeRequestUtils.checkCopyFolderRequest(p, args[2])) {
				if (args[4].equalsIgnoreCase("confirm")) {
					WorldEditModeRequestUtils.removeCopyFolderRequest(p);
					if (directoryOld != null && directoryOld.exists() && directoryOld.isDirectory()) {
						if (CopyFolder.deepMerge(directoryOld, directoryNew)) {
							p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
										  ChatColor.GREEN + args[2] + ChatColor.RED + " was copied successfully.");
						} else {
							p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
										  ChatColor.GREEN + args[2] + ChatColor.RED + " could not be copied, for further information please see [console].");
						}
					} else {
						p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
									  ChatColor.GREEN + args[2] + ChatColor.RED + " does not exist.");
					}
				} else if (args[4].equalsIgnoreCase("deny")) {
					WorldEditModeRequestUtils.removeCopyFolderRequest(p);
					p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " was not copied");
				}
			}
		} catch (final @NotNull IOException e) {
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.RED + "An Error occurred while getting the filepaths for the schematics and folders, for further information please see [console].");
			e.printStackTrace();
		}
	}

	private boolean deepMerge(final @NotNull File oldFile, final @NotNull File newFile) {
		if (Objects.notNull(oldFile.listFiles()).length != 0) {
			try {
				for (final @NotNull File tempFile : Objects.notNull(oldFile.listFiles())) {
					if (new File(newFile, tempFile.getName()).exists()) {
						if (tempFile.isDirectory()) {
							if (!CopyFolder.deepMerge(tempFile, new File(newFile, tempFile.getName()))) {
								return false;
							}
						} else {
							Files.delete(new File(newFile, tempFile.getName()).toPath());
							FileUtils.copyFileToDirectory(tempFile, newFile, true);
						}
					} else {
						if (tempFile.isDirectory()) {
							FileUtils.copyDirectoryToDirectory(tempFile, newFile);
						} else {
							FileUtils.copyFileToDirectory(tempFile, newFile, true);
						}
					}
				}
			} catch (final @NotNull IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	private void copyFolderUsage(final @NotNull Player p, final String slash, final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " copyfolder "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GREEN + "filename"
										+ ChatColor.YELLOW + "> <"
										+ ChatColor.GREEN + "newname"
										+ ChatColor.YELLOW + ">",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " copyfolder "
										+ ChatColor.GREEN + "example newname",
										slash + schemAlias + " copyfolder ", p);
	}
}