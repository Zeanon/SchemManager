package de.zeanon.schemmanager.worldeditversion.commands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.global.utils.InternalFileUtils;
import de.zeanon.schemmanager.global.utils.MessageUtils;
import de.zeanon.schemmanager.worldeditversion.utils.WorldEditVersionSchemUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


@SuppressWarnings("Duplicates")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Folder {

	public static void onFolder(final Player p, final String[] args, final boolean deepSearch) {
		new BukkitRunnable() {
			@Override
			public void run() {
				int listmax = ConfigUtils.getInt("Listmax");
				Path schemPath = WorldEditVersionSchemUtils.getSchemPath();
				boolean spaceLists = ConfigUtils.getBoolean("Space Lists");

				String deep = "";
				if (deepSearch) {
					deep = "-deep ";
				}

				if (args.length == 2) {
					try {
						Path listPath = schemPath != null ? schemPath.toRealPath() : null;
						File directory = listPath != null ? listPath.toFile() : null;
						if (directory == null || !directory.isDirectory()) {
							p.sendMessage(ChatColor.RED + "There is no schematic folder.");
						} else {
							ArrayList<File> rawFiles = InternalFileUtils.getFolders(directory, deepSearch);
							File[] files = rawFiles.toArray(new File[0]);
							Arrays.sort(files);
							double count = files.length;
							int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

							if (spaceLists) {
								p.sendMessage("");
							}
							if (count < 1) {
								MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No folders found", ChatColor.AQUA + " ===", ChatColor.GRAY + "Schematics", p);
							} else {
								MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "Schematics", p);

								if (count < listmax) {
									listmax = (int) count;
								}
								for (int i = 0; i < listmax; i++) {
									if (sendListLineFailed(p, schemPath, listPath, files[i], i, deepSearch)) {
										return;
									}
								}

								if (side > 1) {
									MessageUtils.sendScrollMessage("//schem folder " + deep + "2", "//schem folder " + deep + side, ChatColor.DARK_PURPLE + "Page 2", ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
								} else {
									MessageUtils.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
								}
							}
						}
					} catch (IOException e) {
						p.sendMessage(ChatColor.RED + "There is no schematic folder.");
					}
				} else if (args.length == 3) {
					if (StringUtils.isNumeric(args[2])) {
						try {
							Path listPath = schemPath != null ? schemPath.toRealPath() : null;
							File directory = listPath != null ? listPath.toFile() : null;
							if (directory == null || !directory.isDirectory()) {
								p.sendMessage(ChatColor.RED + "There is no schematic folder.");
							} else {
								ArrayList<File> rawFiles = InternalFileUtils.getFolders(directory, deepSearch);
								File[] files = rawFiles.toArray(new File[0]);
								Arrays.sort(files);
								double count = files.length;
								int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
								int side_number = Integer.parseInt(args[2]);

								if (side_number > side) {
									MessageUtils.sendHoverMessage("", ChatColor.RED + "There are only " + side + " pages of folders in this list", "", ChatColor.GRAY + "Schematics", p);
									return;
								}
								if (spaceLists) {
									p.sendMessage("");
								}
								if (count < 1) {
									MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No folders found", ChatColor.AQUA + " ===", ChatColor.GRAY + "Schematics", p);
								} else {
									MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page " + side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "Schematics", p);

									int id = (side_number - 1) * listmax;
									if (count < listmax * side_number) {
										listmax = (int) count - (listmax * (side_number - 1));
									}
									for (int i = 0; i < listmax; i++) {
										if (sendListLineFailed(p, schemPath, listPath, files[id], id, deepSearch)) {
											return;
										}
										id++;
									}

									if (side > 1) {
										if (side_number > 1) {
											if (side_number < side) {
												MessageUtils.sendScrollMessage("//schem folder " + deep + (side_number + 1), "//schem folder " + deep + (side_number - 1), ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
											} else {
												MessageUtils.sendScrollMessage("//schem folder " + deep + "1", "//schem folder " + deep + (side_number - 1), ChatColor.DARK_PURPLE + "Page 1", ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
											}
										} else {
											MessageUtils.sendScrollMessage("//schem folder " + deep + (side_number + 1), "//schem folder " + deep + side, ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
										}
									} else {
										MessageUtils.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
									}
								}
							}
						} catch (IOException e) {
							p.sendMessage(ChatColor.RED + "There is no schematic folder.");
						}
					} else {
						try {
							Path listPath = schemPath != null ? schemPath.resolve(args[2]).toRealPath() : null;
							File directory = listPath != null ? listPath.toFile() : null;
							if (directory == null || !directory.isDirectory()) {
								p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " is no folder.");
							} else {
								ArrayList<File> rawFiles = InternalFileUtils.getFolders(directory, deepSearch);
								File[] files = rawFiles.toArray(new File[0]);
								Arrays.sort(files);
								double count = files.length;
								int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

								if (spaceLists) {
									p.sendMessage("");
								}
								if (count < 1) {
									MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No folders found", ChatColor.AQUA + " ===", ChatColor.GRAY + "Schematics/" + args[2], p);
								} else {
									MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "Schematics/" + args[2], p);

									if (count < listmax) {
										listmax = (int) count;
									}
									for (int i = 0; i < listmax; i++) {
										if (sendListLineFailed(p, schemPath, listPath, files[i], i, deepSearch)) {
											return;
										}
									}
									if (side > 1) {
										MessageUtils.sendScrollMessage("//schem folder " + deep + args[2] + " 2", "//schem folder " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Page 2", ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
									} else {
										MessageUtils.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
									}
								}
							}
						} catch (IOException e) {
							p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " is no folder.");
						}
					}
				} else {
					try {
						Path listPath = schemPath != null ? schemPath.resolve(args[2]).toRealPath() : null;
						File directory = listPath != null ? listPath.toFile() : null;
						if (directory == null || !directory.isDirectory()) {
							p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " is no folder.");
						} else {
							ArrayList<File> rawFiles = InternalFileUtils.getFolders(directory, deepSearch);
							File[] files = rawFiles.toArray(new File[0]);
							Arrays.sort(files);
							double count = files.length;
							int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
							int side_number = Integer.parseInt(args[3]);

							if (side_number > side) {
								MessageUtils.sendHoverMessage("", ChatColor.RED + "There are only " + side + " pages of folders in this list", "", ChatColor.GRAY + "Schematics/" + args[2], p);
								return;
							}
							if (spaceLists) {
								p.sendMessage("");
							}
							if (count < 1) {
								MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No folders found", ChatColor.AQUA + " ===", ChatColor.GRAY + "Schematics/" + args[2], p);
							} else {
								MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page " + side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "Schematics/" + args[2], p);

								int id = (side_number - 1) * listmax;
								if (count < listmax * side_number) {
									listmax = (int) count - (listmax * (side_number - 1));
								}
								for (int i = 0; i < listmax; i++) {
									if (sendListLineFailed(p, schemPath, listPath, files[id], id, deepSearch)) {
										return;
									}
									id++;
								}

								if (side > 1) {
									if (side_number > 1) {
										if (side_number < side) {
											MessageUtils.sendScrollMessage("//schem folder " + deep + args[2] + " " + (side_number + 1), "//schem folder " + deep + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
										} else {
											MessageUtils.sendScrollMessage("//schem folder " + deep + args[2] + " 1", "//schem folder " + deep + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page 1", ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
										}
									} else {
										MessageUtils.sendScrollMessage("//schem folder " + deep + args[2] + " " + (side_number + 1), "//schem folder " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
									}
								} else {
									MessageUtils.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
								}
							}
						}
					} catch (IOException e) {
						p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " is no folder.");
					}
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}


	private static boolean sendListLineFailed(final Player p, final Path schemFolderPath, final Path listPath, final File file, final int id, final boolean deepSearch) {
		return (!sendListLine(p, schemFolderPath, listPath, file, id, deepSearch));
	}

	private static boolean sendListLine(final Player p, final Path schemFolderPath, final Path listPath, final File file, final int id, final boolean deepSearch) {
		try {
			String name;
			String path;
			String shortenedRelativePath;
			if (InternalFileUtils.getExtension(file.getName()).equals("schem")) {
				name = InternalFileUtils.removeExtension(file.getName());
				path = FilenameUtils.separatorsToUnix(InternalFileUtils.removeExtension(schemFolderPath.toRealPath().relativize(file.toPath().toRealPath()).toString()));
				shortenedRelativePath = deepSearch ? FilenameUtils.separatorsToUnix(InternalFileUtils.removeExtension(listPath.relativize(file.toPath().toRealPath()).toString())) : null;
			} else {
				name = file.getName();
				path = FilenameUtils.separatorsToUnix(schemFolderPath.toRealPath().relativize(file.toPath().toRealPath()).toString());
				shortenedRelativePath = deepSearch ? FilenameUtils.separatorsToUnix(listPath.relativize(file.toPath().toRealPath()).toString()) : null;
			}
			if (deepSearch) {
				MessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + shortenedRelativePath + ChatColor.DARK_GRAY + "]", ChatColor.RED + "List the schematics in " + ChatColor.GREEN + path, "//schem list " + path, p);
				return true;
			} else {
				MessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name, ChatColor.RED + "List the schematics in " + ChatColor.GREEN + path, "//schem list " + path, p);
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			p.sendMessage(ChatColor.RED + "An Error occurred while getting the filepaths for the folders, please see console for further information.");
			return false;
		}
	}
}