package de.zeanon.schemmanager.worldeditmode.commands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.global.utils.MessageUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeSchemUtils;
import de.zeanon.storagemanager.external.browniescollections.GapList;
import de.zeanon.storagemanager.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanager.internal.utility.basic.Objects;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("DuplicatedCode")
@UtilityClass
public class Search {

	public static void onSearch(final @NotNull Player p, final @NotNull String[] args, final boolean deepSearch) {
		new BukkitRunnable() {
			@Override
			public void run() {
				byte listmax = ConfigUtils.getByte("Listmax");
				final Path schemPath = WorldEditModeSchemUtils.getSchemPath();
				final boolean spaceLists = ConfigUtils.getBoolean("Space Lists");
				@Nullable final java.util.List<String> extensions = ConfigUtils.getStringList("File Extensions");

				@NotNull String deep = "";
				if (deepSearch) {
					deep = "-d ";
				}

				if (args.length == 3) {
					try {
						@Nullable final Path listPath = schemPath != null ? schemPath.toRealPath() : null;
						@Nullable final File directory = listPath != null ? listPath.toFile() : null;
						if (directory == null || !directory.isDirectory()) {
							p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]  " +
										  ChatColor.RED + "There is no schematic folder.");
						} else {
							@NotNull final File[] files = Search.getFileArray(directory, Objects.notNull(extensions), deepSearch, args[2]);
							final double count = files.length;
							final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

							if (spaceLists) {
								p.sendMessage("");
							}
							if (count < 1) {
								MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
															  ChatColor.AQUA + "No schematics found",
															  ChatColor.AQUA + " ===",
															  ChatColor.GRAY + "Schematics", p);
							} else {
								MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
															  ChatColor.AQUA + "" + (int) count + " Schematics | Page 1/" + side,
															  ChatColor.AQUA + " ===",
															  ChatColor.GRAY + "Schematics", p);

								if (count < listmax) {
									listmax = (byte) count;
								}
								for (byte i = 0; i < listmax; i++) {
									if (Search.sendListLineFailed(p, schemPath, listPath, files[i], i, deepSearch)) {
										return;
									}
								}

								if (side > 1) {
									MessageUtils.sendScrollMessage("//schem search " + deep + args[2] + " 2",
																   "//schem search " + deep + args[2] + " " + side,
																   ChatColor.DARK_PURPLE + "Page 2",
																   ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
								} else {
									MessageUtils.sendScrollMessage("",
																   "",
																   ChatColor.DARK_PURPLE + "There is only one page of schematics in this list",
																   ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", p, ChatColor.BLUE);
								}
							}
						}
					} catch (@NotNull final IOException e) {
						p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]  " +
									  ChatColor.RED + "Could not access schematic folder, for further information please see [console].");
						e.printStackTrace();
					}
				} else if (args.length == 4) {
					if (StringUtils.isNumeric(args[3])) {
						try {
							@Nullable final Path listPath = schemPath != null ? schemPath.toRealPath() : null;
							@Nullable final File directory = listPath != null ? listPath.toFile() : null;
							if (directory == null || !directory.isDirectory()) {
								p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]  " +
											  ChatColor.RED + "There is no schematic folder.");
							} else {
								@NotNull final File[] files = Search.getFileArray(directory, Objects.notNull(extensions), deepSearch, args[2]);
								final double count = files.length;
								final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
								final int sideNumber = Integer.parseInt(args[3]);

								if (sideNumber > side) {
									MessageUtils.sendHoverMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]",
																  ChatColor.RED + "There are only " + side + " schematics in this list",
																  "",
																  ChatColor.GRAY + "Schematics", p);
									return;
								}
								if (spaceLists) {
									p.sendMessage("");
								}
								if (count < 1) {
									MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
																  ChatColor.AQUA + "No schematics found",
																  ChatColor.AQUA + " ===",
																  ChatColor.GRAY + "Schematics", p);
								} else {
									MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
																  ChatColor.AQUA + "" + (int) count + " Schematics | Page " + sideNumber + "/" + side,
																  ChatColor.AQUA + " ===",
																  ChatColor.GRAY + "Schematics", p);

									int id = (sideNumber - 1) * listmax;
									if (count < listmax * sideNumber) {
										listmax = (byte) ((int) count - (listmax * (sideNumber - 1)));
									}
									for (byte i = 0; i < listmax; i++) {
										if (Search.sendListLineFailed(p, schemPath, listPath, files[id], id, deepSearch)) {
											return;
										}
										id++;
									}

									if (side > 1) {
										if (sideNumber > 1) {
											if (sideNumber < side) {
												MessageUtils.sendScrollMessage("//schem search " + deep + args[2] + " " + (sideNumber + 1),
																			   "//schem search " + deep + args[2] + " " + (sideNumber - 1),
																			   ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
																			   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
											} else {
												MessageUtils.sendScrollMessage("//schem search " + deep + args[2] + " 1",
																			   "//schem search " + deep + args[2] + " " + (sideNumber - 1),
																			   ChatColor.DARK_PURPLE + "Page 1",
																			   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
											}
										} else {
											MessageUtils.sendScrollMessage("//schem search " + deep + args[2] + " " + (sideNumber + 1),
																		   "//schem search " + deep + args[2] + " " + side,
																		   ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
																		   ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
										}
									} else {
										MessageUtils.sendScrollMessage("",
																	   "",
																	   ChatColor.DARK_PURPLE + "There is only one page of schematics in this list",
																	   ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", p, ChatColor.BLUE);
									}
								}
							}
						} catch (@NotNull final IOException e) {
							p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]  " +
										  ChatColor.RED + "Could not access schematic folder, for further information please see [console].");
							e.printStackTrace();
						}
					} else {
						try {
							@Nullable final Path listPath = schemPath != null ? schemPath.resolve(args[2]).toRealPath() : null;
							@Nullable final File directory = listPath != null ? listPath.toFile() : null;
							if (directory == null || !directory.isDirectory()) {
								p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]  " +
											  ChatColor.GREEN + args[2] + ChatColor.RED + " is no folder.");
							} else {
								@NotNull final File[] files = Search.getFileArray(directory, Objects.notNull(extensions), deepSearch, args[3]);
								final double count = files.length;
								final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

								if (spaceLists) {
									p.sendMessage("");
								}
								if (count < 1) {
									MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
																  ChatColor.AQUA + "No schematics found",
																  ChatColor.AQUA + " ===",
																  ChatColor.GRAY + "Schematics/" + args[2], p);
								} else {
									MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
																  ChatColor.AQUA + "" + (int) count + " Schematics | Page 1/" + side,
																  ChatColor.AQUA + " ===",
																  ChatColor.GRAY + "Schematics/" + args[2], p);

									if (count < listmax) {
										listmax = (byte) count;
									}
									for (byte i = 0; i < listmax; i++) {
										if (Search.sendListLineFailed(p, schemPath, listPath, files[i], i, deepSearch)) {
											return;
										}
									}

									if (side > 1) {
										MessageUtils.sendScrollMessage("//schem search " + deep + args[2] + " " + args[3] + " 2",
																	   "//schem search " + deep + args[2] + " " + args[3] + " " + side,
																	   ChatColor.DARK_PURPLE + "Page 2",
																	   ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
									} else {
										MessageUtils.sendScrollMessage("",
																	   "",
																	   ChatColor.DARK_PURPLE + "There is only one page of schematics in this list",
																	   ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", p, ChatColor.BLUE);
									}
								}
							}
						} catch (@NotNull final IOException e) {
							p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]  " +
										  ChatColor.GREEN + args[2] + ChatColor.RED + " could not be accessed, for further information please see [console].");
							e.printStackTrace();
						}
					}
				} else {
					try {
						@Nullable final Path listPath = schemPath != null ? schemPath.resolve(args[2]).toRealPath() : null;
						@Nullable final File directory = listPath != null ? listPath.toFile() : null;
						if (directory == null || !directory.isDirectory()) {
							p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]  " +
										  ChatColor.GREEN + args[2] + ChatColor.RED + " is no folder.");
						} else {
							@NotNull final File[] files = Search.getFileArray(directory, Objects.notNull(extensions), deepSearch, args[3]);
							final double count = files.length;
							final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
							final int sideNumber = Integer.parseInt(args[4]);

							if (sideNumber > side) {
								MessageUtils.sendHoverMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]",
															  ChatColor.RED + "There are only " + side + " schematics in this list",
															  "",
															  ChatColor.GRAY + "Schematics/" + args[2], p);
								return;
							}
							if (spaceLists) {
								p.sendMessage("");
							}
							if (count < 1) {
								MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
															  ChatColor.AQUA + "No schematics found",
															  ChatColor.AQUA + " ===",
															  ChatColor.GRAY + "Schematics/" + args[2], p);
							} else {
								MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
															  ChatColor.AQUA + "" + (int) count + " Schematics | Page " + sideNumber + "/" + side,
															  ChatColor.AQUA + " ===",
															  ChatColor.GRAY + "Schematics/" + args[2], p);

								int id = (sideNumber - 1) * listmax;
								if (count < listmax * sideNumber) {
									listmax = (byte) ((int) count - (listmax * (sideNumber - 1)));
								}
								for (byte i = 0; i < listmax; i++) {
									if (Search.sendListLineFailed(p, schemPath, listPath, files[id], id, deepSearch)) {
										return;
									}
									id++;
								}

								if (side > 1) {
									if (sideNumber > 1) {
										if (sideNumber < side) {
											MessageUtils.sendScrollMessage("//schem search " + deep + args[2] + " " + args[3] + " " + (sideNumber + 1),
																		   "//schem search " + deep + args[2] + " " + args[3] + " " + (sideNumber - 1),
																		   ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
																		   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
										} else {
											MessageUtils.sendScrollMessage("//schem search " + deep + args[2] + " " + args[3] + " 1",
																		   "//schem search " + deep + args[2] + " " + args[3] + " " + (sideNumber - 1),
																		   ChatColor.DARK_PURPLE + "Page 1",
																		   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
										}
									} else {
										MessageUtils.sendScrollMessage("//schem search " + deep + args[2] + " " + args[3] + " " + (sideNumber + 1),
																	   "//schem search " + deep + args[2] + " " + args[3] + " " + side,
																	   ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
																	   ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
									}
								} else {
									MessageUtils.sendScrollMessage("",
																   "",
																   ChatColor.DARK_PURPLE + "There is only one page of schematics in this list",
																   ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", p, ChatColor.BLUE);
								}
							}
						}
					} catch (@NotNull final IOException e) {
						p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]  " +
									  ChatColor.GREEN + args[2] + ChatColor.RED + " could not be accessed, for further information please see [console].");
						e.printStackTrace();
					}
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}

	@NotNull
	private static File[] getFileArray(final @NotNull File directory, final @NotNull List<String> extensions, final boolean deepSearch, final @NotNull String regex) throws IOException {
		final @NotNull java.util.List<File> files = new GapList<>();
		for (@NotNull final File file : BaseFileUtils.listFiles(directory, deepSearch, extensions)) {
			if (BaseFileUtils.removeExtension(file.getName()).toLowerCase().contains(regex.toLowerCase())) {
				files.add(file);
			}
		}
		@NotNull final File[] fileArray = files.toArray(new File[0]);
		Arrays.sort(fileArray);
		return fileArray;
	}

	private static boolean sendListLineFailed(final @NotNull Player p, final @NotNull Path schemFolderPath, final @NotNull Path listPath, final @NotNull File file, final int id, final boolean deepSearch) {
		return (!Search.sendListLine(p, schemFolderPath, listPath, file, id, deepSearch));
	}

	private static boolean sendListLine(final @NotNull Player p, final @NotNull Path schemFolderPath, final @NotNull Path listPath, final @NotNull File file, final int id, final boolean deepSearch) {
		try {
			@NotNull final String name;
			final String path = FilenameUtils.separatorsToUnix(schemFolderPath.toRealPath().relativize(file.toPath().toRealPath()).toString());
			@Nullable final String shortenedRelativePath = deepSearch
														   ? FilenameUtils.separatorsToUnix(listPath.relativize(file.toPath().toRealPath()).toString())
														   : null;
			if (BaseFileUtils.getExtension(file.getName()).equals(Objects.notNull(ConfigUtils.getStringList("File Extensions")).get(0))) {
				name = BaseFileUtils.removeExtension(file.getName());
			} else {
				name = file.getName();
			}
			if (deepSearch) {
				MessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ",
												ChatColor.GOLD + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + shortenedRelativePath + ChatColor.DARK_GRAY + "]",
												ChatColor.RED + "Load " + ChatColor.GOLD + path + ChatColor.RED + " to your clipboard",
												"//schem load " + path, p);
				return true;
			} else {
				MessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ",
												ChatColor.GOLD + name,
												ChatColor.RED + "Load " + ChatColor.GOLD + path + ChatColor.RED + " to your clipboard",
												"//schem load " + path, p);
				return true;
			}
		} catch (@NotNull final IOException e) {
			e.printStackTrace();
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]  " +
						  ChatColor.RED + "An Error occurred while getting the filepaths for the schematics, for further information please see [console].");
			return false;
		}
	}
}