package de.zeanon.schemmanager.worldeditmode.commands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.global.utils.MessageUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeSchemUtils;
import de.zeanon.storagemanager.internal.utility.basic.BaseFileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class ListFolders {

	public static void onListFolder(final @NotNull Player p, final @NotNull String[] args, final boolean deepSearch, final int modifierCount) {
		new BukkitRunnable() {
			@Override
			public void run() {
				byte listmax = ConfigUtils.getByte("Listmax");
				final Path schemPath = WorldEditModeSchemUtils.getSchemPath();
				final boolean spaceLists = ConfigUtils.getBoolean("Space Lists");

				final @NotNull String deep;
				if (deepSearch) {
					deep = "-d ";
				} else {
					deep = "";
				}

				if (args.length == 2 + modifierCount) {
					try {
						@Nullable final Path listPath = schemPath != null ? schemPath.toRealPath() : null;
						@Nullable final File directory = listPath != null ? listPath.toFile() : null;
						if (directory == null || !directory.isDirectory()) {
							p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
										  ChatColor.RED + "There is no schematic folder.");
						} else {
							@NotNull final Collection<File> rawFiles = BaseFileUtils.listFolders(directory, deepSearch);
							@NotNull final File[] files = rawFiles.toArray(new File[0]);
							Arrays.sort(files);
							final double count = files.length;
							final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

							if (spaceLists) {
								p.sendMessage("");
							}
							if (count < 1) {
								MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
															  ChatColor.AQUA + "No folders found",
															  ChatColor.AQUA + " ===",
															  ChatColor.GRAY + "Schematics", p);
							} else {
								MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
															  ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side,
															  ChatColor.AQUA + " ===",
															  ChatColor.GRAY + "Schematics", p);

								if (count < listmax) {
									listmax = (byte) count;
								}
								for (byte i = 0; i < listmax; i++) {
									if (ListFolders.sendListLineFailed(p, schemPath, listPath, files[i], i, deepSearch)) {
										return;
									}
								}

								if (side > 1) {
									MessageUtils.sendScrollMessage("//schem listfolders " + deep + "2",
																   "//schem listfolders " + deep + side,
																   ChatColor.DARK_PURPLE + "Page 2",
																   ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
								} else {
									MessageUtils.sendScrollMessage("",
																   "",
																   ChatColor.DARK_PURPLE + "There is only one page of folders in this list",
																   ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
								}
							}
						}
					} catch (@NotNull final IOException e) {
						p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
									  ChatColor.RED + "Could not access schematic folder, for further information please see [console].");
					}
				} else if (args.length == 3 + modifierCount) {
					if (StringUtils.isNumeric(args[2 + modifierCount])) {
						try {
							@Nullable final Path listPath = schemPath != null ? schemPath.toRealPath() : null;
							@Nullable final File directory = listPath != null ? listPath.toFile() : null;
							if (directory == null || !directory.isDirectory()) {
								p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
											  ChatColor.RED + "There is no schematic folder.");
							} else {
								@NotNull final Collection<File> rawFiles = BaseFileUtils.listFolders(directory, deepSearch);
								@NotNull final File[] files = rawFiles.toArray(new File[0]);
								Arrays.sort(files);
								final double count = files.length;
								final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
								final int sideNumber = Integer.parseInt(args[2 + modifierCount]);

								if (sideNumber > side) {
									MessageUtils.sendHoverMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]",
																  ChatColor.RED + "There are only " + side + " pages of folders in this list",
																  "",
																  ChatColor.GRAY + "Schematics", p);
									return;
								}
								if (spaceLists) {
									p.sendMessage("");
								}
								if (count < 1) {
									MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
																  ChatColor.AQUA + "No folders found",
																  ChatColor.AQUA + " ===",
																  ChatColor.GRAY + "Schematics", p);
								} else {
									MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
																  ChatColor.AQUA + "" + (int) count + " Folder | Page " + sideNumber + "/" + side,
																  ChatColor.AQUA + " ===",
																  ChatColor.GRAY + "Schematics", p);

									int id = (sideNumber - 1) * listmax;
									if (count < listmax * sideNumber) {
										listmax = (byte) ((int) count - (listmax * (sideNumber - 1)));
									}
									for (byte i = 0; i < listmax; i++) {
										if (ListFolders.sendListLineFailed(p, schemPath, listPath, files[id], id, deepSearch)) {
											return;
										}
										id++;
									}

									if (side > 1) {
										if (sideNumber > 1) {
											if (sideNumber < side) {
												MessageUtils.sendScrollMessage("//schem listfolders " + deep + (sideNumber + 1),
																			   "//schem listfolders " + deep + (sideNumber - 1),
																			   ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
																			   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
											} else {
												MessageUtils.sendScrollMessage("//schem listfolders " + deep + "1",
																			   "//schem listfolders " + deep + (sideNumber - 1),
																			   ChatColor.DARK_PURPLE + "Page 1",
																			   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
											}
										} else {
											MessageUtils.sendScrollMessage("//schem listfolders " + deep + (sideNumber + 1),
																		   "//schem listfolders " + deep + side,
																		   ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
																		   ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
										}
									} else {
										MessageUtils.sendScrollMessage("",
																	   "",
																	   ChatColor.DARK_PURPLE + "There is only one page of folders in this list",
																	   ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
									}
								}
							}
						} catch (@NotNull final IOException e) {
							p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
										  ChatColor.RED + "Could not access schematic folder, for further information please see [console].");
							e.printStackTrace();
						}
					} else {
						try {
							@Nullable final Path listPath = schemPath != null ? schemPath.resolve(args[2 + modifierCount]).toRealPath() : null;
							@Nullable final File directory = listPath != null ? listPath.toFile() : null;
							if (directory == null || !directory.isDirectory()) {
								p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
											  ChatColor.GREEN + args[2 + modifierCount] + ChatColor.RED + " is no folder.");
							} else {
								@NotNull final Collection<File> rawFiles = BaseFileUtils.listFolders(directory, deepSearch);
								@NotNull final File[] files = rawFiles.toArray(new File[0]);
								Arrays.sort(files);
								final double count = files.length;
								final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

								if (spaceLists) {
									p.sendMessage("");
								}
								if (count < 1) {
									MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
																  ChatColor.AQUA + "No folders found",
																  ChatColor.AQUA + " ===",
																  ChatColor.GRAY + "Schematics/" + args[2 + modifierCount], p);
								} else {
									MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
																  ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side,
																  ChatColor.AQUA + " ===",
																  ChatColor.GRAY + "Schematics/" + args[2 + modifierCount], p);

									if (count < listmax) {
										listmax = (byte) count;
									}
									for (byte i = 0; i < listmax; i++) {
										if (ListFolders.sendListLineFailed(p, schemPath, listPath, files[i], i, deepSearch)) {
											return;
										}
									}
									if (side > 1) {
										MessageUtils.sendScrollMessage("//schem listfolders " + deep + args[2 + modifierCount] + " 2",
																	   "//schem listfolders " + deep + args[2 + modifierCount] + " " + side,
																	   ChatColor.DARK_PURPLE + "Page 2",
																	   ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
									} else {
										MessageUtils.sendScrollMessage("",
																	   "",
																	   ChatColor.DARK_PURPLE + "There is only one page of folders in this list",
																	   ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
									}
								}
							}
						} catch (@NotNull final IOException e) {
							p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
										  ChatColor.GREEN + args[2 + modifierCount] + ChatColor.RED + " could not be accessed, for further information please see [console].");
							e.printStackTrace();
						}
					}
				} else {
					try {
						@Nullable final Path listPath = schemPath != null ? schemPath.resolve(args[2 + modifierCount]).toRealPath() : null;
						@Nullable final File directory = listPath != null ? listPath.toFile() : null;
						if (directory == null || !directory.isDirectory()) {
							p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
										  ChatColor.GREEN + args[2 + modifierCount] + ChatColor.RED + " is no folder.");
						} else {
							@NotNull final Collection<File> rawFiles = BaseFileUtils.listFolders(directory, deepSearch);
							@NotNull final File[] files = rawFiles.toArray(new File[0]);
							Arrays.sort(files);
							final double count = files.length;
							final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
							final int sideNumber = Integer.parseInt(args[3 + modifierCount]);

							if (sideNumber > side) {
								MessageUtils.sendHoverMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]",
															  ChatColor.RED + "There are only " + side + " pages of folders in this list",
															  "",
															  ChatColor.GRAY + "Schematics/" + args[2 + modifierCount], p);
								return;
							}
							if (spaceLists) {
								p.sendMessage("");
							}
							if (count < 1) {
								MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
															  ChatColor.AQUA + "No folders found",
															  ChatColor.AQUA + " ===",
															  ChatColor.GRAY + "Schematics/" + args[2 + modifierCount], p);
							} else {
								MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
															  ChatColor.AQUA + "" + (int) count + " Folder | Page " + sideNumber + "/" + side,
															  ChatColor.AQUA + " ===",
															  ChatColor.GRAY + "Schematics/" + args[2 + modifierCount], p);

								int id = (sideNumber - 1) * listmax;
								if (count < listmax * sideNumber) {
									listmax = (byte) ((int) count - (listmax * (sideNumber - 1)));
								}
								for (byte i = 0; i < listmax; i++) {
									if (ListFolders.sendListLineFailed(p, schemPath, listPath, files[id], id, deepSearch)) {
										return;
									}
									id++;
								}

								if (side > 1) {
									if (sideNumber > 1) {
										if (sideNumber < side) {
											MessageUtils.sendScrollMessage("//schem listfolders " + deep + args[2 + modifierCount] + " " + (sideNumber + 1),
																		   "//schem listfolders " + deep + args[2 + modifierCount] + " " + (sideNumber - 1),
																		   ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
																		   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
										} else {
											MessageUtils.sendScrollMessage("//schem listfolders " + deep + args[2 + modifierCount] + " 1",
																		   "//schem listfolders " + deep + args[2 + modifierCount] + " " + (sideNumber - 1),
																		   ChatColor.DARK_PURPLE + "Page 1",
																		   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
										}
									} else {
										MessageUtils.sendScrollMessage("//schem listfolders " + deep + args[2 + modifierCount] + " " + (sideNumber + 1),
																	   "//schem listfolders " + deep + args[2 + modifierCount] + " " + side,
																	   ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
																	   ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
									}
								} else {
									MessageUtils.sendScrollMessage("",
																   "",
																   ChatColor.DARK_PURPLE + "There is only one page of folders in this list",
																   ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
								}
							}
						}
					} catch (@NotNull final IOException e) {
						p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
									  ChatColor.GREEN + args[2 + modifierCount] + ChatColor.RED + " could not be accessed, for further information please see [console].");
						e.printStackTrace();
					}
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}


	private static boolean sendListLineFailed(final @NotNull Player p, final @NotNull Path schemFolderPath, final @NotNull Path listPath, final @NotNull File file, final int id, final boolean deepSearch) {
		return (!ListFolders.sendListLine(p, schemFolderPath, listPath, file, id, deepSearch));
	}

	private static boolean sendListLine(final @NotNull Player p, final @NotNull Path schemFolderPath, final @NotNull Path listPath, final @NotNull File file, final int id, final boolean deepSearch) {
		try {
			@NotNull final String name = file.getName();
			final String path = FilenameUtils.separatorsToUnix(schemFolderPath.toRealPath().relativize(file.toPath().toRealPath()).toString());
			@Nullable final String shortenedRelativePath = deepSearch
														   ? FilenameUtils.separatorsToUnix(listPath.relativize(file.toPath().toRealPath()).toString())
														   : null;
			if (deepSearch) {
				MessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ",
												ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + shortenedRelativePath + ChatColor.DARK_GRAY + "]",
												ChatColor.RED + "List the schematics in " + ChatColor.GREEN + path,
												"//schem list " + path, p);
				return true;
			} else {
				MessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ",
												ChatColor.GREEN + name,
												ChatColor.RED + "List the schematics in " + ChatColor.GREEN + path,
												"//schem list " + path, p);
				return true;
			}
		} catch (@NotNull final IOException e) {
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.RED + "An Error occurred while getting the filepaths for the folders, for further information please see [console].");
			e.printStackTrace();
			return false;
		}
	}
}