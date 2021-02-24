package de.zeanon.schemmanager.plugin.commands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.GlobalMessageUtils;
import de.zeanon.schemmanager.plugin.utils.SchemUtils;
import de.zeanon.storagemanagercore.internal.utility.basic.BaseFileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class ListFolder {

	public void execute(final @NotNull String @NotNull [] args, final @NotNull Player p, final @NotNull String slash, final @NotNull String schemAlias) {
		new BukkitRunnable() {
			@Override
			public void run() {
				final boolean deep;
				final int modifierCount;

				if (args.length > 2 && (args[2].equalsIgnoreCase("-deep") || args[2].equalsIgnoreCase("-d"))) {
					deep = true;
					modifierCount = 1;
				} else {
					deep = false;
					modifierCount = 0;
				}

				if (args.length <= 4 + modifierCount) {
					if (args.length == 4 + modifierCount && (StringUtils.isNumeric(args[2 + modifierCount]) || !StringUtils.isNumeric(args[3 + modifierCount]))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						ListFolder.usage(p, slash, schemAlias);
					} else if (args.length >= 3 + modifierCount && args[2 + modifierCount].contains("./")) {
						p.sendMessage(ChatColor.RED + "File '" + args[2 + modifierCount] + "'resolution error: Path is not allowed.");
						ListFolder.usage(p, slash, schemAlias);
					} else {
						ListFolder.executeInternally(p, args, deep, modifierCount);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					ListFolder.usage(p, slash, schemAlias);
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}

	public @NotNull String usageMessage(final @NotNull String slash, final @NotNull String schemAlias) {
		return ChatColor.GRAY + slash + schemAlias
			   + ChatColor.AQUA + " listfolder "
			   + ChatColor.YELLOW + "["
			   + ChatColor.DARK_PURPLE + "-d"
			   + ChatColor.YELLOW + "] ["
			   + ChatColor.GREEN + "folder"
			   + ChatColor.YELLOW + "] ["
			   + ChatColor.DARK_PURPLE + "page"
			   + ChatColor.YELLOW + "]";
	}

	public @NotNull String usageHoverMessage(final @NotNull String slash, final @NotNull String schemAlias) {
		return ChatColor.RED + "e.g. "
			   + ChatColor.GRAY + slash + schemAlias
			   + ChatColor.AQUA + " listfolder "
			   + ChatColor.YELLOW + "["
			   + ChatColor.DARK_PURPLE + "-d"
			   + ChatColor.YELLOW + "] ["
			   + ChatColor.GREEN + "folder"
			   + ChatColor.YELLOW + "] ["
			   + ChatColor.DARK_PURPLE + "page"
			   + ChatColor.YELLOW + "]";
	}

	public @NotNull String usageCommand(final @NotNull String slash, final @NotNull String schemAlias) {
		return slash + schemAlias + " listfolder ";
	}

	private void executeInternally(final @NotNull Player p, final @NotNull String @NotNull [] args, final boolean deepSearch, final int modifierCount) {
		final int listmax = ConfigUtils.getInt("Listmax");
		final boolean spaceLists = ConfigUtils.getBoolean("Space Lists");
		final @Nullable Path schemPath = SchemUtils.getSchemPath();

		final @NotNull String deep;
		if (deepSearch) {
			deep = "-d ";
		} else {
			deep = "";
		}

		switch (args.length - modifierCount) {
			case 2:
				ListFolder.twoArgs(schemPath, p, deep, deepSearch, spaceLists, listmax);
				break;
			case 3:
				ListFolder.threeArgs(args[2 + modifierCount], schemPath, p, deep, deepSearch, spaceLists, listmax);
				break;
			default:
				ListFolder.defaultCase(args[2 + modifierCount], args[3 + modifierCount], schemPath, p, deep, deepSearch, spaceLists, listmax);
		}
	}

	private boolean sendListLineFailed(final @NotNull Player p, final @NotNull Path schemFolderPath, final @NotNull Path listPath, final @NotNull File file, final int id, final boolean deepSearch) {
		try {
			final @NotNull String name = file.getName();
			final @NotNull String path = FilenameUtils.separatorsToUnix(schemFolderPath.toRealPath().relativize(file.toPath().toRealPath()).toString());
			final @Nullable String shortenedRelativePath = deepSearch
														   ? FilenameUtils.separatorsToUnix(listPath.relativize(file.toPath().toRealPath()).toString())
														   : null;

			if (deepSearch) {
				GlobalMessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ",
													  ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + shortenedRelativePath + ChatColor.DARK_GRAY + "]",
													  ChatColor.RED + "List the schematics in " + ChatColor.GREEN + path + ".",
													  "//schem list " + path, p);
			} else {
				GlobalMessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ",
													  ChatColor.GREEN + name,
													  ChatColor.RED + "List the schematics in " + ChatColor.GREEN + path + ".",
													  "//schem list " + path, p);
			}
			return false;
		} catch (final @NotNull IOException e) {
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.RED + "An Error occurred while getting the filepaths for the folders, for further information please see [console].");
			e.printStackTrace();
			return true;
		}
	}

	private void twoArgs(final @Nullable Path schemPath, final @NotNull Player p, final @NotNull String deep, final boolean deepSearch, final boolean spaceLists, int listmax) {
		try {
			final @Nullable Path listPath = schemPath;
			final @Nullable File directory = listPath != null ? listPath.toFile() : null;

			if (directory == null || !directory.isDirectory()) {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.RED + "Could not access schematic folder.");
			} else {
				final @NotNull java.util.List<File> files = BaseFileUtils.listFolders(directory, deepSearch);
				final double count = files.size();
				final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

				if (spaceLists) {
					p.sendMessage("");
				}

				if (count < 1) {
					GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
														ChatColor.AQUA + "No folders found",
														ChatColor.AQUA + " ===",
														ChatColor.GRAY + "schematics", p);
				} else {
					GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
														ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side,
														ChatColor.AQUA + " ===",
														ChatColor.GRAY + "schematics", p);
					if (count < listmax) {
						listmax = (int) count;
					}

					for (int i = 0; i < listmax; i++) {
						if (ListFolder.sendListLineFailed(p, schemPath, listPath, files.get(i), i, deepSearch)) {
							return;
						}
					}

					if (side > 1) {
						GlobalMessageUtils.sendScrollMessage("//schem listfolder " + deep + "2",
															 "//schem listfolder " + deep + side,
															 ChatColor.DARK_PURPLE + "Page 2",
															 ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
					} else {
						GlobalMessageUtils.sendScrollMessage("",
															 "",
															 ChatColor.DARK_PURPLE + "There is only one page of folders in this list",
															 ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
					}
				}
			}
		} catch (final @NotNull IOException e) {
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.RED + "Could not access schematic folder, for further information please see [console].");
		}
	}

	private void threeArgs(final @NotNull String arg, final @Nullable Path schemPath, final @NotNull Player p, final @NotNull String deep, final boolean deepSearch, final boolean spaceLists, int listmax) {
		if (StringUtils.isNumeric(arg)) {
			try {
				final @Nullable Path listPath = schemPath;
				final @Nullable File directory = listPath != null ? listPath.toFile() : null;

				if (directory == null || !directory.isDirectory()) {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
								  ChatColor.RED + "Could not access schematic folder.");
				} else {
					final @NotNull java.util.List<File> files = BaseFileUtils.listFolders(directory, deepSearch);
					final double count = files.size();
					final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
					final int sideNumber = Integer.parseInt(arg);

					if (sideNumber > side) {
						GlobalMessageUtils.sendHoverMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]",
															ChatColor.RED + "There are only " + side + " pages of folders in this list",
															"",
															ChatColor.GRAY + "schematics", p);
						return;
					}

					if (spaceLists) {
						p.sendMessage("");
					}

					if (count < 1) {
						GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
															ChatColor.AQUA + "No folders found",
															ChatColor.AQUA + " ===",
															ChatColor.GRAY + "schematics", p);
					} else {
						GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
															ChatColor.AQUA + "" + (int) count + " Folder | Page " + sideNumber + "/" + side,
															ChatColor.AQUA + " ===",
															ChatColor.GRAY + "schematics", p);
						int id = (sideNumber - 1) * listmax;

						if (count < listmax * sideNumber) {
							listmax = (int) count - (listmax * (sideNumber - 1));
						}

						for (int i = 0; i < listmax; i++) {
							if (ListFolder.sendListLineFailed(p, schemPath, listPath, files.get(id), id, deepSearch)) {
								return;
							}
							id++;
						}

						if (side > 1) {
							if (sideNumber > 1) {
								if (sideNumber < side) {
									GlobalMessageUtils.sendScrollMessage("//schem listfolder " + deep + (sideNumber + 1),
																		 "//schem listfolder " + deep + (sideNumber - 1),
																		 ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
																		 ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
								} else {
									GlobalMessageUtils.sendScrollMessage("//schem listfolder " + deep + "1",
																		 "//schem listfolder " + deep + (sideNumber - 1),
																		 ChatColor.DARK_PURPLE + "Page 1",
																		 ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
								}
							} else {
								GlobalMessageUtils.sendScrollMessage("//schem listfolder " + deep + (sideNumber + 1),
																	 "//schem listfolder " + deep + side,
																	 ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
																	 ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
							}
						} else {
							GlobalMessageUtils.sendScrollMessage("",
																 "",
																 ChatColor.DARK_PURPLE + "There is only one page of folders in this list",
																 ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
						}
					}
				}
			} catch (final @NotNull IOException e) {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.RED + "Could not access schematic folder, for further information please see [console].");
				e.printStackTrace();
			}
		} else {
			try {
				final @Nullable Path listPath = schemPath != null ? schemPath.resolve(arg) : null;
				final @Nullable File directory = listPath != null ? listPath.toFile() : null;

				if (directory == null || !directory.isDirectory()) {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
								  ChatColor.GREEN + arg + ChatColor.RED + " is no folder.");
				} else {
					final @NotNull java.util.List<File> files = BaseFileUtils.listFolders(directory, deepSearch);
					final double count = files.size();
					final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

					if (spaceLists) {
						p.sendMessage("");
					}

					if (count < 1) {
						GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
															ChatColor.AQUA + "No folders found",
															ChatColor.AQUA + " ===",
															ChatColor.GRAY + "schematics/" + arg, p);
					} else {
						GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
															ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side,
															ChatColor.AQUA + " ===",
															ChatColor.GRAY + "schematics/" + arg, p);

						if (count < listmax) {
							listmax = (int) count;
						}

						for (int i = 0; i < listmax; i++) {
							if (ListFolder.sendListLineFailed(p, schemPath, listPath, files.get(i), i, deepSearch)) {
								return;
							}
						}

						if (side > 1) {
							GlobalMessageUtils.sendScrollMessage("//schem listfolder " + deep + arg + " 2",
																 "//schem listfolder " + deep + arg + " " + side,
																 ChatColor.DARK_PURPLE + "Page 2",
																 ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
						} else {
							GlobalMessageUtils.sendScrollMessage("",
																 "",
																 ChatColor.DARK_PURPLE + "There is only one page of folders in this list",
																 ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
						}
					}
				}
			} catch (final @NotNull IOException e) {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.GREEN + arg + ChatColor.RED + " could not be accessed, for further information please see [console].");
				e.printStackTrace();
			}
		}
	}

	private void defaultCase(final @NotNull String argTwo, final @NotNull String argThree, final @Nullable Path schemPath, final @NotNull Player p, final @NotNull String deep, final boolean deepSearch, final boolean spaceLists, int listmax) {
		try {
			final @Nullable Path listPath = schemPath != null ? schemPath.resolve(argTwo) : null;
			final @Nullable File directory = listPath != null ? listPath.toFile() : null;

			if (directory == null || !directory.isDirectory()) {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.GREEN + argTwo + ChatColor.RED + " is no folder.");
			} else {
				final @NotNull java.util.List<File> files = BaseFileUtils.listFolders(directory, deepSearch);
				final double count = files.size();
				final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
				final int sideNumber = Integer.parseInt(argThree);

				if (sideNumber > side) {
					GlobalMessageUtils.sendHoverMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]",
														ChatColor.RED + "There are only " + side + " pages of folders in this list",
														"",
														ChatColor.GRAY + "schematics/" + argTwo, p);
					return;
				}

				if (spaceLists) {
					p.sendMessage("");
				}

				if (count < 1) {
					GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
														ChatColor.AQUA + "No folders found",
														ChatColor.AQUA + " ===",
														ChatColor.GRAY + "schematics/" + argTwo, p);
				} else {
					GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
														ChatColor.AQUA + "" + (int) count + " Folder | Page " + sideNumber + "/" + side,
														ChatColor.AQUA + " ===",
														ChatColor.GRAY + "schematics/" + argTwo, p);
					int id = (sideNumber - 1) * listmax;
					if (count < listmax * sideNumber) {
						listmax = (int) count - (listmax * (sideNumber - 1));
					}

					for (int i = 0; i < listmax; i++) {
						if (ListFolder.sendListLineFailed(p, schemPath, listPath, files.get(id), id, deepSearch)) {
							return;
						}
						id++;
					}

					if (side > 1) {
						if (sideNumber > 1) {
							if (sideNumber < side) {
								GlobalMessageUtils.sendScrollMessage("//schem listfolder " + deep + argTwo + " " + (sideNumber + 1),
																	 "//schem listfolder " + deep + argTwo + " " + (sideNumber - 1),
																	 ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
																	 ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
							} else {
								GlobalMessageUtils.sendScrollMessage("//schem listfolder " + deep + argTwo + " 1",
																	 "//schem listfolder " + deep + argTwo + " " + (sideNumber - 1),
																	 ChatColor.DARK_PURPLE + "Page 1",
																	 ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
							}
						} else {
							GlobalMessageUtils.sendScrollMessage("//schem listfolder " + deep + argTwo + " " + (sideNumber + 1),
																 "//schem listfolder " + deep + argTwo + " " + side,
																 ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
																 ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
						}
					} else {
						GlobalMessageUtils.sendScrollMessage("",
															 "",
															 ChatColor.DARK_PURPLE + "There is only one page of folders in this list",
															 ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
					}
				}
			}
		} catch (final @NotNull IOException e) {
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.GREEN + argTwo + ChatColor.RED + " could not be accessed, for further information please see [console].");
			e.printStackTrace();
		}
	}

	private void usage(final @NotNull Player p, @NotNull final String slash, @NotNull final String schemAlias) {
		GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
											  ListFolder.usageMessage(slash, schemAlias),
											  ListFolder.usageHoverMessage(slash, schemAlias),
											  ListFolder.usageCommand(slash, schemAlias), p);
	}
}