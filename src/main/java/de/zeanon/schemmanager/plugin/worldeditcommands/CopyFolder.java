package de.zeanon.schemmanager.plugin.worldeditcommands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.SchemUtils;
import de.zeanon.schemmanager.plugin.utils.commands.CommandRequestUtils;
import de.zeanon.schemmanager.plugin.utils.commands.GlobalMessageUtils;
import de.zeanon.storagemanagercore.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
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
						CopyFolder.usage(p, slash, schemAlias);
					} else if (args[2].contains("./") || args[2].contains(".\\") || args.length >= 4 && (args[3].contains("./") || args[3].contains(".\\"))) {
						final String name = args[2].contains("./") || args[2].contains(".\\") ? args[2] : args[3];
						p.sendMessage(ChatColor.RED + "File '" + name + "' resolution error: Path is not allowed.");
						CopyFolder.usage(p, slash, schemAlias);
					} else if (args.length == 5
							   && !args[4].equalsIgnoreCase("-confirm")
							   && !args[4].equalsIgnoreCase("-deny")
							   && !CommandRequestUtils.checkCopyFolderRequest(p.getUniqueId(), args[2])) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						CopyFolder.usage(p, slash, schemAlias);
					} else {
						CopyFolder.executeInternally(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					CopyFolder.usage(p, slash, schemAlias);
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}

	public @NotNull
	String usageMessage(final @NotNull String slash, final @NotNull String schemAlias) {
		return ChatColor.GRAY + slash + schemAlias
			   + ChatColor.AQUA + " copyfolder "
			   + ChatColor.YELLOW + "<"
			   + ChatColor.GREEN + "foldername"
			   + ChatColor.YELLOW + "> <"
			   + ChatColor.GREEN + "newname"
			   + ChatColor.YELLOW + ">";
	}

	public @NotNull
	String usageHoverMessage(final @NotNull String slash, final @NotNull String schemAlias) {
		return ChatColor.RED + "e.g. "
			   + ChatColor.GRAY + slash + schemAlias
			   + ChatColor.AQUA + " copyfolder "
			   + ChatColor.GREEN + "example newname";
	}

	public @NotNull
	String usageCommand(final @NotNull String slash, final @NotNull String schemAlias) {
		return slash + schemAlias + " copyfolder ";
	}

	@SuppressWarnings("DuplicatedCode")
	private void executeInternally(final @NotNull Player p, final @NotNull String[] args) {
		try {
			final @Nullable Path schemPath = SchemUtils.getSchemPath();
			final @Nullable File directoryOld = schemPath.resolve(args[2]).toFile();
			final @Nullable File directoryNew = schemPath.resolve(args[3]).toFile();

			if (args.length == 4) {
				if (!directoryOld.exists() || !directoryOld.isDirectory()) {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
								  ChatColor.GREEN + args[2] + ChatColor.RED + " does not exist.");
					return;
				} else if (directoryNew.exists() && directoryNew.isDirectory()) {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
								  ChatColor.GREEN + args[3] + ChatColor.RED + " already exists, the folders will be merged.");

					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
								  ChatColor.RED + "These schematics already exist in " +
								  ChatColor.GREEN + args[3] +
								  ChatColor.RED + ", they will be overwritten:");
					int id = 0;
					final @Nullable List<String> extensions = Objects.notNull(ConfigUtils.getStringList("File Extensions"));
					for (final @NotNull File oldFile : BaseFileUtils.listFilesOfType(directoryOld, true, extensions)) {
						for (final @NotNull File newFile : BaseFileUtils.searchFilesOfType(directoryNew, true, BaseFileUtils.removeExtension(oldFile.getName()), extensions)) {
							if (BaseFileUtils.removeExtension(newFile.toPath().relativize(directoryNew.toPath()).toString())
											 .equalsIgnoreCase(BaseFileUtils.removeExtension(oldFile.toPath().relativize(directoryOld.toPath()).toString()))) {

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

								GlobalMessageUtils.sendCommandMessage(
										ChatColor.RED + Integer.toString(id + 1) + ": ",
										ChatColor.GOLD + name
										+ ChatColor.DARK_GRAY + " ["
										+ ChatColor.GRAY + shortenedRelativePath
										+ ChatColor.DARK_GRAY + "]",
										ChatColor.RED + "Load " + ChatColor.GOLD + path + ChatColor.RED + " to your clipboard.",
										"//schem load " + path, p);
								id++;
							}
						}
					}

					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
								  ChatColor.RED + "These folders already exist in " +
								  ChatColor.GREEN + args[3] +
								  ChatColor.RED + ", they will be merged:");
					int i = 0;
					for (final @NotNull File oldFolder : BaseFileUtils.listFolders(directoryOld, true)) {
						for (final @NotNull File newFolder : BaseFileUtils.searchFolders(directoryNew, true, oldFolder.getName())) {
							if (BaseFileUtils.removeExtension(newFolder.toPath().relativize(directoryNew.toPath()).toString())
											 .equalsIgnoreCase(BaseFileUtils.removeExtension(oldFolder.toPath().relativize(directoryOld.toPath()).toString()))) {

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
								GlobalMessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(i + 1) + ": ",
																	  ChatColor.GREEN + name
																	  + ChatColor.DARK_GRAY + " ["
																	  + ChatColor.GRAY + shortenedRelativePath
																	  + ChatColor.DARK_GRAY + "]",
																	  ChatColor.RED + "List the schematics in " + ChatColor.GREEN + path + ".",
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

				GlobalMessageUtils.sendBooleanMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
													  ChatColor.RED + "Do you really want to copy " +
													  ChatColor.GREEN + args[2] +
													  ChatColor.RED + " to " +
													  ChatColor.GREEN + args[3] +
													  ChatColor.RED + "?",
													  "//schem copyfolder " + args[2] + " " + args[3] + " -confirm",
													  "//schem copyfolder " + args[2] + " " + args[3] + " -deny", p);
				CommandRequestUtils.addCopyFolderRequest(p.getUniqueId(), args[2]);
			} else if (args.length == 5 && CommandRequestUtils.checkCopyFolderRequest(p.getUniqueId(), args[2])) {
				if (args[4].equalsIgnoreCase("-confirm")) {
					CommandRequestUtils.removeCopyFolderRequest(p.getUniqueId());
					if (directoryOld.exists() && directoryOld.isDirectory()) {
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
				} else if (args[4].equalsIgnoreCase("-deny")) {
					CommandRequestUtils.removeCopyFolderRequest(p.getUniqueId());
					p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " was not copied");
				}
			}
		} catch (final @NotNull IOException e) {
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.RED + "An Error occurred while getting the filepaths for the schematics and folders, for further information please see [console].");
			SchemManager.getChatLogger().log(Level.SEVERE, "Error while getting filepaths for the schematics and folders", e);
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
				SchemManager.getChatLogger().log(Level.SEVERE, "Error while merging the folders", e);
				return false;
			}
		}
		return true;
	}

	private void usage(final @NotNull Player p, @NotNull final String slash, @NotNull final String schemAlias) {
		GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
											  CopyFolder.usageMessage(slash, schemAlias),
											  CopyFolder.usageHoverMessage(slash, schemAlias),
											  CopyFolder.usageCommand(slash, schemAlias), p);
	}
}