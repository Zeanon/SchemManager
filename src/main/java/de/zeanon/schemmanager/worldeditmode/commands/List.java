package de.zeanon.schemmanager.worldeditmode.commands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.global.utils.MessageUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeSchemUtils;
import de.zeanon.storage.internal.utility.utils.basic.BaseFileUtils;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("Duplicates")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class List {

	public static void onList(final @NotNull Player p, final @NotNull String[] args, final boolean deepSearch) {
		new BukkitRunnable() {
			@Override
			public void run() {
				byte listmax = ConfigUtils.getByte("Listmax");
				Path schemPath = WorldEditModeSchemUtils.getSchemPath();
				boolean spaceLists = ConfigUtils.getBoolean("Space Lists");
				@Nullable java.util.List<String> extensions = ConfigUtils.getStringList("File Extensions");

				@NotNull String deep = "";
				if (deepSearch) {
					deep = "-d ";
				}

				if (args.length == 2) {
					try {
						@Nullable Path listPath = schemPath != null ? schemPath.toRealPath() : null;
						@Nullable File directory = listPath != null ? listPath.toFile() : null;
						if (directory == null || !directory.isDirectory()) {
							p.sendMessage(ChatColor.RED + "There is no schematic folder.");
						} else {
							@NotNull Collection<File> rawFiles = BaseFileUtils.listFiles(directory, Objects.notNull(extensions), deepSearch);
							@NotNull File[] files = rawFiles.toArray(new File[0]);
							Arrays.sort(files);
							double count = files.length;
							int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

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
									if (sendListLineFailed(p, schemPath, listPath, files[i], i, deepSearch)) {
										return;
									}
								}

								if (side > 1) {
									MessageUtils.sendScrollMessage("//schem list " + deep + "2",
																   "//schem list " + deep + side,
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
					} catch (IOException e) {
						p.sendMessage(ChatColor.RED + "There is no schematic folder.");
					}
				} else if (args.length == 3) {
					if (StringUtils.isNumeric(args[2])) {
						try {
							@Nullable Path listPath = schemPath != null ? schemPath.toRealPath() : null;
							@Nullable File directory = listPath != null ? listPath.toFile() : null;
							if (directory == null || !directory.isDirectory()) {
								p.sendMessage(ChatColor.RED + "There is no schematic folder.");
							} else {
								@NotNull Collection<File> rawFiles = BaseFileUtils.listFiles(directory, Objects.notNull(extensions), deepSearch);
								@NotNull File[] files = rawFiles.toArray(new File[0]);
								Arrays.sort(files);
								double count = files.length;
								int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
								int sideNumber = Integer.parseInt(args[2]);

								if (sideNumber > side) {
									MessageUtils.sendHoverMessage("",
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
																  ChatColor.AQUA + " ===", ChatColor.GRAY + "Schematics", p);

									int id = (sideNumber - 1) * listmax;
									if (count < listmax * sideNumber) {
										listmax = (byte) ((int) count - (listmax * (sideNumber - 1)));
									}
									for (byte i = 0; i < listmax; i++) {
										if (sendListLineFailed(p, schemPath, listPath, files[id], id, deepSearch)) {
											return;
										}
										id++;
									}

									if (side > 1) {
										if (sideNumber > 1) {
											if (sideNumber < side) {
												MessageUtils.sendScrollMessage("//schem list " + deep + (sideNumber + 1),
																			   "//schem list " + deep + (sideNumber - 1),
																			   ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
																			   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
											} else {
												MessageUtils.sendScrollMessage("//schem list " + deep + "1",
																			   "//schem list " + deep + (sideNumber - 1),
																			   ChatColor.DARK_PURPLE + "Page 1",
																			   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
											}
										} else {
											MessageUtils.sendScrollMessage("//schem list " + deep + (sideNumber + 1),
																		   "//schem list " + deep + side,
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
						} catch (IOException e) {
							p.sendMessage(ChatColor.RED + "There is no schematic folder.");
						}
					} else {
						try {
							@Nullable Path listPath = schemPath != null ? schemPath.resolve(args[2]).toRealPath() : null;
							@Nullable File directory = listPath != null ? listPath.toFile() : null;
							if (directory == null || !directory.isDirectory()) {
								p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " is no folder.");
							} else {
								@NotNull Collection<File> rawFiles = BaseFileUtils.listFiles(directory, Objects.notNull(extensions), deepSearch);
								@NotNull File[] files = rawFiles.toArray(new File[0]);
								Arrays.sort(files);
								double count = files.length;
								int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

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
										if (sendListLineFailed(p, schemPath, listPath, files[i], i, deepSearch)) {
											return;
										}
									}

									if (side > 1) {
										MessageUtils.sendScrollMessage("//schem list " + deep + args[2] + " 2",
																	   "//schem list " + deep + args[2] + " " + side,
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
						} catch (IOException e) {
							p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " is no folder.");
						}
					}
				} else {
					try {
						@Nullable Path listPath = schemPath != null ? schemPath.resolve(args[2]).toRealPath() : null;
						@Nullable File directory = listPath != null ? listPath.toFile() : null;
						if (directory == null || !directory.isDirectory()) {
							p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " is no folder.");
						} else {
							@NotNull Collection<File> rawFiles = BaseFileUtils.listFiles(directory, Objects.notNull(extensions), deepSearch);
							@NotNull File[] files = rawFiles.toArray(new File[0]);
							Arrays.sort(files);
							double count = files.length;
							int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
							int sideNumber = Integer.parseInt(args[3]);

							if (sideNumber > side) {
								MessageUtils.sendHoverMessage("",
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
															  ChatColor.AQUA + " ===", ChatColor.GRAY + "Schematics/" + args[2], p);

								int id = (sideNumber - 1) * listmax;
								if (count < listmax * sideNumber) {
									listmax = (byte) ((int) count - (listmax * (sideNumber - 1)));
								}
								for (byte i = 0; i < listmax; i++) {
									if (sendListLineFailed(p, schemPath, listPath, files[id], id, deepSearch)) {
										return;
									}
									id++;
								}

								if (side > 1) {
									if (sideNumber > 1) {
										if (sideNumber < side) {
											MessageUtils.sendScrollMessage("//schem list " + deep + args[2] + " " + (sideNumber + 1),
																		   "//schem list " + deep + args[2] + " " + (sideNumber - 1),
																		   ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
																		   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
										} else {
											MessageUtils.sendScrollMessage("//schem list " + deep + args[2] + " 1",
																		   "//schem list " + deep + args[2] + " " + (sideNumber - 1),
																		   ChatColor.DARK_PURPLE + "Page 1",
																		   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
										}
									} else {
										MessageUtils.sendScrollMessage("//schem list " + deep + args[2] + " " + (sideNumber + 1),
																	   "//schem list " + deep + args[2] + " " + side,
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
					} catch (IOException e) {
						p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " is no folder.");
					}
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}


	private static boolean sendListLineFailed(final @NotNull Player p, final @NotNull Path schemFolderPath, final @NotNull Path listPath, final @NotNull File file, final int id, final boolean deepSearch) {
		return (!sendListLine(p, schemFolderPath, listPath, file, id, deepSearch));
	}

	private static boolean sendListLine(final @NotNull Player p, final @NotNull Path schemFolderPath, final @NotNull Path listPath, final @NotNull File file, final int id, final boolean deepSearch) {
		try {
			String name;
			String path = FilenameUtils.separatorsToUnix(schemFolderPath.toRealPath().relativize(file.toPath().toRealPath()).toString());
			@Nullable String shortenedRelativePath = deepSearch
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
		} catch (IOException e) {
			e.printStackTrace();
			p.sendMessage(ChatColor.RED + "An Error occurred while getting the filepaths for the schematics, please see console for further information.");
			return false;
		}
	}
}