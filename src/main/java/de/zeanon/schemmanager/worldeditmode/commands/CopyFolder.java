package de.zeanon.schemmanager.worldeditmode.commands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.global.utils.MessageUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeRequestUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeSchemUtils;
import de.zeanon.storage.internal.utils.SMFileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


@SuppressWarnings("DuplicatedCode")
public class CopyFolder {

	public static void onCopyFolder(final Player p, final String[] args) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					Path schemPath = WorldEditModeSchemUtils.getSchemPath();
					File directory_old = schemPath != null ? schemPath.resolve(args[2]).toFile() : null;
					File directory_new = schemPath != null ? schemPath.resolve(args[3]).toFile() : null;

					if (args.length == 4) {
						if (directory_old == null || !directory_old.exists() || !directory_old.isDirectory()) {
							p.sendMessage(ChatColor.GREEN + args[2]
										  + ChatColor.RED + " does not exist.");
							return;
						} else if (directory_new.exists() && directory_new.isDirectory()) {
							p.sendMessage(ChatColor.GREEN + args[3]
										  + ChatColor.RED + " already exists, the folders will be merged.");
							int id = 0;
							List<String> extensions = ConfigUtils.getStringList("File Extensions");
							for (File oldFile : SMFileUtils.listFiles(directory_old, extensions, true)) {
								for (File newFile : SMFileUtils.listFiles(directory_new, extensions, true)) {
									if (SMFileUtils.removeExtension(newFile.getName())
												   .equalsIgnoreCase(SMFileUtils.removeExtension(oldFile.getName()))
										&& newFile.toPath().relativize(directory_new.toPath())
												  .equals(oldFile.toPath().relativize(directory_old.toPath()))) {
										if (id == 0) {
											p.sendMessage(ChatColor.RED + "These schematics already exist in "
														  + ChatColor.GREEN + args[3]
														  + ChatColor.RED + ", they will be overwritten.");
										}
										String name;
										String path;
										String shortenedRelativePath;
										if (SMFileUtils.getExtension(newFile.getName()).equals(ConfigUtils.getStringList("File Extensions").get(0))) {
											name = SMFileUtils.removeExtension(newFile.getName());
											path = SMFileUtils.separatorsToUnix(
													SMFileUtils.removeExtension(
															schemPath.toRealPath()
																	 .relativize(newFile.toPath().toRealPath())
																	 .toString()));
											shortenedRelativePath = SMFileUtils.separatorsToUnix(
													SMFileUtils.removeExtension(
															schemPath.resolve(args[3])
																	 .toRealPath()
																	 .relativize(newFile.toPath().toRealPath())
																	 .toString()));
										} else {
											name = newFile.getName();
											path = SMFileUtils.separatorsToUnix(
													schemPath.toRealPath()
															 .relativize(newFile.toPath().toRealPath())
															 .toString());
											shortenedRelativePath = SMFileUtils.separatorsToUnix(
													schemPath.resolve(args[3])
															 .toRealPath()
															 .relativize(newFile.toPath().toRealPath())
															 .toString());
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
							for (File oldFolder : SMFileUtils.listFolders(directory_old, true)) {
								for (File newFolder : SMFileUtils.listFolders(directory_new, true)) {
									if (newFolder.getName()
												 .equalsIgnoreCase(oldFolder.getName())
										&& newFolder.toPath().relativize(directory_new.toPath())
													.equals(oldFolder.toPath().relativize(directory_old.toPath()))) {
										if (i == 0) {
											p.sendMessage(ChatColor.RED + "These folders already exist in "
														  + ChatColor.GREEN + args[3]
														  + ChatColor.RED + ", they will be merged.");
										}
										String name = newFolder.getName();
										String path = SMFileUtils.separatorsToUnix(
												schemPath.toRealPath()
														 .relativize(newFolder.toPath().toRealPath())
														 .toString());
										String shortenedRelativePath = SMFileUtils.separatorsToUnix(
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
								p.sendMessage(ChatColor.RED + "There are already "
											  + ChatColor.DARK_PURPLE + id
											  + ChatColor.RED + " schematics and "
											  + ChatColor.DARK_PURPLE + i
											  + ChatColor.RED + " folders with the same name in "
											  + ChatColor.GREEN + args[3]
											  + ChatColor.RED + ".");
							} else if (id > 0) {
								p.sendMessage(ChatColor.RED + "There are already "
											  + ChatColor.DARK_PURPLE + id
											  + ChatColor.RED + " schematics with the same name in "
											  + ChatColor.GREEN + args[3]
											  + ChatColor.RED + ".");
							} else if (i > 0) {
								p.sendMessage(ChatColor.RED + "There are already "
											  + ChatColor.DARK_PURPLE + i
											  + ChatColor.RED + " folders with the same name in "
											  + ChatColor.GREEN + args[3]
											  + ChatColor.RED + ".");
							}
						}
						MessageUtils.sendBooleanMessage(ChatColor.RED + "Do you really want to copy "
														+ ChatColor.GREEN + args[2]
														+ ChatColor.RED + "?",
														"//schem copyfolder " + args[2] + " " + args[3] + " confirm",
														"//schem copyfolder " + args[2] + " " + args[3] + " deny", p);
						WorldEditModeRequestUtils.addCopyFolderRequest(p, args[2]);
					} else if (args.length == 5 && WorldEditModeRequestUtils.checkCopyFolderRequest(p, args[2])) {
						if (args[4].equalsIgnoreCase("confirm")) {
							WorldEditModeRequestUtils.removeCopyFolderRequest(p);
							if (directory_old != null && directory_old.exists() && directory_old.isDirectory()) {
								if (deepMerge(directory_old, directory_new)) {
									p.sendMessage(ChatColor.GREEN + args[2]
												  + ChatColor.RED + " was copied successfully.");
								} else {
									p.sendMessage(ChatColor.GREEN + args[2]
												  + ChatColor.RED + " could not be copied.");
								}
							} else {
								p.sendMessage(ChatColor.GREEN + args[2]
											  + ChatColor.RED + " does not exist.");
							}
						} else if (args[4].equalsIgnoreCase("deny")) {
							WorldEditModeRequestUtils.removeCopyFolderRequest(p);
							p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " was not copied");
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					p.sendMessage(ChatColor.RED + "An Error occurred while getting the filepaths for the schematics and folders, please see console for further information.");
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}


	private static boolean deepMerge(final File oldFile, final File newFile) {
		if (Objects.notNull(oldFile.listFiles()).length == 0) {
			return true;
		} else {
			try {
				for (File tempFile : Objects.notNull(oldFile.listFiles())) {
					if (new File(newFile, tempFile.getName()).exists()) {
						if (tempFile.isDirectory()) {
							if (!deepMerge(tempFile, new File(newFile, tempFile.getName()))) {
								return false;
							}
						} else {
							if (new File(newFile, tempFile.getName()).delete()) {
								FileUtils.copyFileToDirectory(tempFile, newFile, true);
							} else {
								return false;
							}
						}
					} else {
						if (tempFile.isDirectory()) {
							FileUtils.copyDirectoryToDirectory(tempFile, newFile);
						} else {
							FileUtils.copyFileToDirectory(tempFile, newFile, true);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
	}
}