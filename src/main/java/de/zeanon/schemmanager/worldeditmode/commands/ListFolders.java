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

	public void execute(final @NotNull String[] args, final @NotNull Player p, final @NotNull String slash, final @NotNull String schemAlias) {
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
						ListFolders.usage(p, slash, schemAlias);
					} else if (args.length >= 3 + modifierCount && args[2 + modifierCount].contains("./")) {
						p.sendMessage(ChatColor.RED + "File '" + args[2 + modifierCount] + "'resolution error: Path is not allowed.");
						ListFolders.usage(p, slash, schemAlias);
					} else {
						ListFolders.onListFolder(p, args, deep, modifierCount);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					ListFolders.usage(p, slash, schemAlias);
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}

	public @NotNull String usageMessage(final @NotNull String slash, final @NotNull String schemAlias) {
		return ChatColor.GRAY + slash + schemAlias
			   + ChatColor.AQUA + " listfolders "
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
			   + ChatColor.AQUA + " listfolders "
			   + ChatColor.YELLOW + "["
			   + ChatColor.DARK_PURPLE + "-d"
			   + ChatColor.YELLOW + "] ["
			   + ChatColor.GREEN + "folder"
			   + ChatColor.YELLOW + "] ["
			   + ChatColor.DARK_PURPLE + "page"
			   + ChatColor.YELLOW + "]";
	}

	public @NotNull String usageCommand(final @NotNull String slash, final @NotNull String schemAlias) {
		return slash + schemAlias + " listfolders ";
	}

	private void onListFolder(final @NotNull Player p, final @NotNull String[] args, final boolean deepSearch, final int modifierCount) {
		final byte listmax = ConfigUtils.getByte("Listmax");
		final boolean spaceLists = ConfigUtils.getBoolean("Space Lists");
		final @Nullable Path schemPath = WorldEditModeSchemUtils.getSchemPath();

		final @NotNull String deep;
		if (deepSearch) {
			deep = "-d ";
		} else {
			deep = "";
		}

		switch (args.length - modifierCount) {
			case 2:
				ListFolders.twoArgs(schemPath, p, deep, deepSearch, spaceLists, listmax);
				break;
			case 3:
				ListFolders.threeArgs(args[2 + modifierCount], schemPath, p, deep, deepSearch, spaceLists, listmax);
				break;
			default:
				ListFolders.defaultCase(args[2 + modifierCount], args[3 + modifierCount], schemPath, p, deep, deepSearch, spaceLists, listmax);
		}
	}

	private boolean sendListLineFailed(final @NotNull Player p, final @NotNull Path schemFolderPath, final @NotNull Path listPath, final @NotNull File file, final int id, final boolean deepSearch) {
		return (!ListFolders.sendListLine(p, schemFolderPath, listPath, file, id, deepSearch));
	}

	private boolean sendListLine(final @NotNull Player p, final @NotNull Path schemFolderPath, final @NotNull Path listPath, final @NotNull File file, final int id, final boolean deepSearch) {
		try {
			final @NotNull String name = file.getName();
			final @NotNull String path = FilenameUtils.separatorsToUnix(schemFolderPath.toRealPath().relativize(file.toPath().toRealPath()).toString());
			final @Nullable String shortenedRelativePath = deepSearch
														   ? FilenameUtils.separatorsToUnix(listPath.relativize(file.toPath().toRealPath()).toString())
														   : null;

			if (deepSearch) {
				MessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ",
												ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + shortenedRelativePath + ChatColor.DARK_GRAY + "]",
												ChatColor.RED + "List the schematics in " + ChatColor.GREEN + path,
												"//schem list " + path, p);
			} else {
				MessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ",
												ChatColor.GREEN + name,
												ChatColor.RED + "List the schematics in " + ChatColor.GREEN + path,
												"//schem list " + path, p);
			}
			return true;
		} catch (final @NotNull IOException e) {
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.RED + "An Error occurred while getting the filepaths for the folders, for further information please see [console].");
			e.printStackTrace();
			return false;
		}
	}

	private void twoArgs(final @Nullable Path schemPath, final @NotNull Player p, final @NotNull String deep, final boolean deepSearch, final boolean spaceLists, byte listmax) {
		try {
			final @Nullable Path listPath = schemPath != null ? schemPath.toRealPath() : null;
			final @Nullable File directory = listPath != null ? listPath.toFile() : null;

			if (directory == null || !directory.isDirectory()) {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.RED + "There is no schematic folder.");
			} else {
				final @NotNull Collection<File> rawFiles = BaseFileUtils.listFolders(directory, deepSearch);
				final @NotNull File[] files = rawFiles.toArray(new File[0]);
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

					Arrays.sort(files);
					for (int i = 0; i < listmax; i++) {
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
													   ChatColor.DARK_PURPLE + "There is only one page of folders in ListFolders list",
													   ChatColor.DARK_PURPLE + "There is only one page of folders in ListFolders list", p, ChatColor.BLUE);
					}
				}
			}
		} catch (final @NotNull IOException e) {
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.RED + "Could not access schematic folder, for further information please see [console].");
		}
	}

	private void threeArgs(final @NotNull String arg, final @Nullable Path schemPath, final @NotNull Player p, final @NotNull String deep, final boolean deepSearch, final boolean spaceLists, byte listmax) {
		if (StringUtils.isNumeric(arg)) {
			try {
				final @Nullable Path listPath = schemPath != null ? schemPath.toRealPath() : null;
				final @Nullable File directory = listPath != null ? listPath.toFile() : null;

				if (directory == null || !directory.isDirectory()) {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
								  ChatColor.RED + "There is no schematic folder.");
				} else {
					final @NotNull Collection<File> rawFiles = BaseFileUtils.listFolders(directory, deepSearch);
					final @NotNull File[] files = rawFiles.toArray(new File[0]);
					final double count = files.length;
					final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
					final int sideNumber = Integer.parseInt(arg);

					if (sideNumber > side) {
						MessageUtils.sendHoverMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]",
													  ChatColor.RED + "There are only " + side + " pages of folders in ListFolders list",
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

						Arrays.sort(files);
						for (int i = 0; i < listmax; i++) {
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
														   ChatColor.DARK_PURPLE + "There is only one page of folders in ListFolders list",
														   ChatColor.DARK_PURPLE + "There is only one page of folders in ListFolders list", p, ChatColor.BLUE);
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
				final @Nullable Path listPath = schemPath != null ? schemPath.resolve(arg).toRealPath() : null;
				final @Nullable File directory = listPath != null ? listPath.toFile() : null;

				if (directory == null || !directory.isDirectory()) {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
								  ChatColor.GREEN + arg + ChatColor.RED + " is no folder.");
				} else {
					final @NotNull Collection<File> rawFiles = BaseFileUtils.listFolders(directory, deepSearch);
					final @NotNull File[] files = rawFiles.toArray(new File[0]);
					final double count = files.length;
					final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

					if (spaceLists) {
						p.sendMessage("");
					}

					if (count < 1) {
						MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
													  ChatColor.AQUA + "No folders found",
													  ChatColor.AQUA + " ===",
													  ChatColor.GRAY + "Schematics/" + arg, p);
					} else {
						MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
													  ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side,
													  ChatColor.AQUA + " ===",
													  ChatColor.GRAY + "Schematics/" + arg, p);

						if (count < listmax) {
							listmax = (byte) count;
						}

						Arrays.sort(files);
						for (int i = 0; i < listmax; i++) {
							if (ListFolders.sendListLineFailed(p, schemPath, listPath, files[i], i, deepSearch)) {
								return;
							}
						}

						if (side > 1) {
							MessageUtils.sendScrollMessage("//schem listfolders " + deep + arg + " 2",
														   "//schem listfolders " + deep + arg + " " + side,
														   ChatColor.DARK_PURPLE + "Page 2",
														   ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
						} else {
							MessageUtils.sendScrollMessage("",
														   "",
														   ChatColor.DARK_PURPLE + "There is only one page of folders in ListFolders list",
														   ChatColor.DARK_PURPLE + "There is only one page of folders in ListFolders list", p, ChatColor.BLUE);
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

	private void defaultCase(final @NotNull String argTwo, final @NotNull String argThree, final @Nullable Path schemPath, final @NotNull Player p, final @NotNull String deep, final boolean deepSearch, final boolean spaceLists, byte listmax) {
		try {
			final @Nullable Path listPath = schemPath != null ? schemPath.resolve(argTwo).toRealPath() : null;
			final @Nullable File directory = listPath != null ? listPath.toFile() : null;

			if (directory == null || !directory.isDirectory()) {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.GREEN + argTwo + ChatColor.RED + " is no folder.");
			} else {
				final @NotNull Collection<File> rawFiles = BaseFileUtils.listFolders(directory, deepSearch);
				final @NotNull File[] files = rawFiles.toArray(new File[0]);
				final double count = files.length;
				final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
				final int sideNumber = Integer.parseInt(argThree);

				if (sideNumber > side) {
					MessageUtils.sendHoverMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]",
												  ChatColor.RED + "There are only " + side + " pages of folders in ListFolders list",
												  "",
												  ChatColor.GRAY + "Schematics/" + argTwo, p);
					return;
				}

				if (spaceLists) {
					p.sendMessage("");
				}

				if (count < 1) {
					MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
												  ChatColor.AQUA + "No folders found",
												  ChatColor.AQUA + " ===",
												  ChatColor.GRAY + "Schematics/" + argTwo, p);
				} else {
					MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
												  ChatColor.AQUA + "" + (int) count + " Folder | Page " + sideNumber + "/" + side,
												  ChatColor.AQUA + " ===",
												  ChatColor.GRAY + "Schematics/" + argTwo, p);
					int id = (sideNumber - 1) * listmax;
					if (count < listmax * sideNumber) {
						listmax = (byte) ((int) count - (listmax * (sideNumber - 1)));
					}

					Arrays.sort(files);
					for (int i = 0; i < listmax; i++) {
						if (ListFolders.sendListLineFailed(p, schemPath, listPath, files[id], id, deepSearch)) {
							return;
						}
						id++;
					}

					if (side > 1) {
						if (sideNumber > 1) {
							if (sideNumber < side) {
								MessageUtils.sendScrollMessage("//schem listfolders " + deep + argTwo + " " + (sideNumber + 1),
															   "//schem listfolders " + deep + argTwo + " " + (sideNumber - 1),
															   ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
															   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
							} else {
								MessageUtils.sendScrollMessage("//schem listfolders " + deep + argTwo + " 1",
															   "//schem listfolders " + deep + argTwo + " " + (sideNumber - 1),
															   ChatColor.DARK_PURPLE + "Page 1",
															   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
							}
						} else {
							MessageUtils.sendScrollMessage("//schem listfolders " + deep + argTwo + " " + (sideNumber + 1),
														   "//schem listfolders " + deep + argTwo + " " + side,
														   ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
														   ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
						}
					} else {
						MessageUtils.sendScrollMessage("",
													   "",
													   ChatColor.DARK_PURPLE + "There is only one page of folders in ListFolders list",
													   ChatColor.DARK_PURPLE + "There is only one page of folders in ListFolders list", p, ChatColor.BLUE);
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
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ListFolders.usageMessage(slash, schemAlias),
										ListFolders.usageHoverMessage(slash, schemAlias),
										ListFolders.usageCommand(slash, schemAlias), p);
	}
}