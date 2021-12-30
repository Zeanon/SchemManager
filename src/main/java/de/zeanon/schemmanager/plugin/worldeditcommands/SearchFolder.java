package de.zeanon.schemmanager.plugin.worldeditcommands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.SchemUtils;
import de.zeanon.schemmanager.plugin.utils.commands.GlobalMessageUtils;
import de.zeanon.storagemanagercore.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class SearchFolder {

	public void execute(final @NotNull String[] args, final @NotNull Player p, final @NotNull String slash, final @NotNull String schemAlias) {
		new BukkitRunnable() {
			@Override
			public void run() {
				final int modifierCount;
				final boolean deep;
				final boolean caseSensitive;

				if (args.length > 2 && (args[2].equalsIgnoreCase("-deep") || args[2].equalsIgnoreCase("-d"))) {
					deep = true;

					if (args.length > 3 && (args[3].equalsIgnoreCase("-casesensitive") || args[3].equalsIgnoreCase("-c"))) {
						caseSensitive = true;
						modifierCount = 2;
					} else {
						caseSensitive = false;
						modifierCount = 1;
					}
				} else if (args.length > 2 && (args[2].equalsIgnoreCase("-casesensitive") || args[2].equalsIgnoreCase("-c"))) {
					caseSensitive = true;

					if (args.length > 3 && (args[3].equalsIgnoreCase("-deep") || args[3].equalsIgnoreCase("-d"))) {
						deep = true;
						modifierCount = 2;
					} else {
						deep = false;
						modifierCount = 1;
					}
				} else {
					deep = false;
					caseSensitive = false;
					modifierCount = 0;
				}

				if (args.length <= 5 + modifierCount) {
					if (args.length < 3 + modifierCount) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						SearchFolder.usage(p, slash, schemAlias);
					} else if (args[2 + modifierCount].contains("./")) {
						p.sendMessage(ChatColor.RED + "File '" + args[2 + modifierCount] + "' resolution error: Path is not allowed.");
						SearchFolder.usage(p, slash, schemAlias);
					} else if (args.length == 5 + modifierCount
							   && (StringUtils.isNumeric(args[2 + modifierCount])
								   || StringUtils.isNumeric(args[3 + modifierCount])
								   || !StringUtils.isNumeric(args[4 + modifierCount]))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						SearchFolder.usage(p, slash, schemAlias);
					} else {
						SearchFolder.executeInternally(p, args, deep, caseSensitive, modifierCount);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					SearchFolder.usage(p, slash, schemAlias);
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}

	public @NotNull String usageMessage(final @NotNull String slash, final @NotNull String schemAlias) {
		return ChatColor.GRAY + slash + schemAlias
			   + ChatColor.AQUA + " searchfolder "
			   + ChatColor.YELLOW + "["
			   + ChatColor.DARK_PURPLE + "-d"
			   + ChatColor.YELLOW + "] ["
			   + ChatColor.DARK_PURPLE + "-c"
			   + ChatColor.YELLOW + "] ["
			   + ChatColor.DARK_GREEN + "folder"
			   + ChatColor.YELLOW + "] <"
			   + ChatColor.DARK_GREEN + "foldername"
			   + ChatColor.YELLOW + "> ["
			   + ChatColor.DARK_PURPLE + "page"
			   + ChatColor.YELLOW + "]";
	}

	public @NotNull String usageHoverMessage(final @NotNull String slash, final @NotNull String schemAlias) {
		return ChatColor.RED + "e.g. "
			   + ChatColor.GRAY + slash + schemAlias
			   + ChatColor.AQUA + " searchfolder "
			   + ChatColor.YELLOW + "["
			   + ChatColor.DARK_PURPLE + "-d"
			   + ChatColor.YELLOW + "] ["
			   + ChatColor.DARK_PURPLE + "-c"
			   + ChatColor.YELLOW + "] ["
			   + ChatColor.DARK_GREEN + "folder"
			   + ChatColor.YELLOW + "] "
			   + ChatColor.DARK_GREEN + "example"
			   + ChatColor.YELLOW + " ["
			   + ChatColor.DARK_PURPLE + "page"
			   + ChatColor.YELLOW + "]";
	}

	public @NotNull String usageCommand(final @NotNull String slash, final @NotNull String schemAlias) {
		return slash + schemAlias + " searchfolder ";
	}

	private void executeInternally(final @NotNull Player p, final @NotNull String[] args, final boolean deepSearch, final boolean caseSensitiveSearch, final int modifierCount) {
		final int listmax = ConfigUtils.getInt("Listmax");
		final boolean spaceLists = ConfigUtils.getBoolean("Space Lists");
		final @Nullable Path schemPath = SchemUtils.getSchemPath();

		final @NotNull String deep;
		if (deepSearch) {
			deep = "-d ";
		} else {
			deep = "";
		}

		final @NotNull String caseSensitive;
		if (caseSensitiveSearch) {
			caseSensitive = "-c ";
		} else {
			caseSensitive = "";
		}

		switch (args.length - modifierCount) {
			case 3:
				SearchFolder.threeArgs(args[2 + modifierCount], schemPath, p, deep, caseSensitive, deepSearch, caseSensitiveSearch, spaceLists, listmax);
				break;
			case 4:
				SearchFolder.fourArgs(args[2 + modifierCount], args[3 + modifierCount], schemPath, p, deep, caseSensitive, deepSearch, caseSensitiveSearch, spaceLists, listmax);
				break;
			default:
				SearchFolder.defaultCase(args[2 + modifierCount], args[3 + modifierCount], args[4 + modifierCount], schemPath, p, deep, caseSensitive, deepSearch, caseSensitiveSearch, spaceLists, listmax);
		}
	}

	private boolean sendListLineFailed(final @NotNull Player p, final @NotNull Path schemFolderPath, final @NotNull Path listPath, final @NotNull File file, final int id, final boolean deepSearch) {
		try {
			final @NotNull String name = file.getName();
			final @NotNull String path = FilenameUtils.separatorsToUnix(schemFolderPath.toRealPath().relativize(file.toPath().toRealPath()).toString());
			final @Nullable String shortenedRelativePath = deepSearch ? FilenameUtils.separatorsToUnix(listPath.relativize(file.toPath().toRealPath()).toString()) : null;

			if (deepSearch) {
				GlobalMessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ",
													  ChatColor.DARK_GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + shortenedRelativePath + ChatColor.DARK_GRAY + "]",
													  ChatColor.RED + "List the schematics in " + ChatColor.DARK_GREEN + path + ".",
													  "//schem list " + path, p);
			} else {
				GlobalMessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ",
													  ChatColor.DARK_GREEN + name,
													  ChatColor.RED + "List the schematics in " + ChatColor.DARK_GREEN + path + ".",
													  "//schem list " + path, p);
			}
			return false;
		} catch (final @NotNull IOException e) {
			Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.RED + "An Error occurred while getting the filepaths for the folders, for further information please see [console].");
			return true;
		}
	}

	private void threeArgs(final @NotNull String arg, final @Nullable Path schemPath, final @NotNull Player p, final @NotNull String deep, final @NotNull String caseSensitive, final boolean deepSearch, final boolean caseSensitiveSearch, final boolean spaceLists, int listmax) {
		try {
			final @Nullable Path listPath = schemPath != null ? schemPath.toAbsolutePath() : null;
			final @Nullable File directory = listPath != null ? listPath.toFile() : null;
			if (directory == null || !directory.isDirectory()) {
				throw new IOException("Schematic folder does not exist");
			} else {
				final @NotNull List<File> files = Objects.notNull(BaseFileUtils.searchFolders(directory, deepSearch, arg, caseSensitiveSearch));
				final double count = files.size();
				final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

				if (spaceLists) {
					p.sendMessage("");
				}

				if (count < 1) {
					GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
														ChatColor.AQUA + "No folders found",
														ChatColor.AQUA + " ===",
														ChatColor.GRAY + (caseSensitiveSearch ? "schematics [-c]" : "schematics"), p);
				} else {
					GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
														ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side,
														ChatColor.AQUA + " ===",
														ChatColor.GRAY + (caseSensitiveSearch ? "schematics [-c]" : "schematics"), p);
					if (count < listmax) {
						listmax = (int) count;
					}

					for (int i = 0; i < listmax; i++) {
						if (SearchFolder.sendListLineFailed(p, schemPath, listPath, files.get(i), i, deepSearch)) {
							return;
						}
					}

					if (side > 1) {
						GlobalMessageUtils.sendScrollMessage("//schem searchfolder " + deep + caseSensitive + arg + " 2",
															 "//schem searchfolder " + deep + caseSensitive + arg + " " + side,
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
			Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
		}
	}

	private void fourArgs(final @NotNull String argTwo, final @NotNull String argThree, final @Nullable Path schemPath, final @NotNull Player p, final @NotNull String deep, final @NotNull String caseSensitive, final boolean deepSearch, final boolean caseSensitiveSearch, final boolean spaceLists, int listmax) {
		if (StringUtils.isNumeric(argThree)) {
			try {
				final @Nullable Path listPath = schemPath != null ? schemPath.toAbsolutePath() : null;
				final @Nullable File directory = listPath != null ? listPath.toFile() : null;
				if (directory == null || !directory.isDirectory()) {
					throw new IOException("Schematic folder does not exist");
				} else {
					final @NotNull List<File> files = Objects.notNull(Objects.notNull(BaseFileUtils.searchFolders(directory, deepSearch, argTwo, caseSensitiveSearch)));
					final double count = Objects.notNull(files).size();
					final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
					final int sideNumber = Integer.parseInt(argThree);

					if (sideNumber > side) {
						GlobalMessageUtils.sendHoverMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]",
															ChatColor.RED + "There are only " + side + " pages of folders in this list",
															"",
															ChatColor.GRAY + (caseSensitiveSearch ? "schematics [-c]" : "schematics"), p);
						return;
					}
					if (spaceLists) {
						p.sendMessage("");
					}
					if (count < 1) {
						GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
															ChatColor.AQUA + "No folders found",
															ChatColor.AQUA + " ===",
															ChatColor.GRAY + (caseSensitiveSearch ? "schematics [-c]" : "schematics"), p);
					} else {
						GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
															ChatColor.AQUA + "" + (int) count + " Folder | Page " + sideNumber + "/" + side,
															ChatColor.AQUA + " ===",
															ChatColor.GRAY + (caseSensitiveSearch ? "schematics [-c]" : "schematics"), p);

						int id = (sideNumber - 1) * listmax;
						if (count < listmax * sideNumber) {
							listmax = (int) count - (listmax * (sideNumber - 1));
						}

						for (int i = 0; i < listmax; i++) {
							if (SearchFolder.sendListLineFailed(p, schemPath, listPath, files.get(id), id, deepSearch)) {
								return;
							}
							id++;
						}

						if (side > 1) {
							if (sideNumber > 1) {
								if (sideNumber < side) {
									GlobalMessageUtils.sendScrollMessage("//schem searchfolder " + deep + caseSensitive + argTwo + " " + (sideNumber + 1),
																		 "//schem searchfolder " + deep + caseSensitive + argTwo + " " + (sideNumber - 1),
																		 ChatColor.RED + "Page " + (sideNumber + 1),
																		 ChatColor.RED + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
								} else {
									GlobalMessageUtils.sendScrollMessage("//schem searchfolder " + deep + caseSensitive + argTwo + " 1",
																		 "//schem searchfolder " + deep + caseSensitive + argTwo + " " + (sideNumber - 1),
																		 ChatColor.DARK_PURPLE + "Page 1",
																		 ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
								}
							} else {
								GlobalMessageUtils.sendScrollMessage("//schem searchfolder " + deep + caseSensitive + argTwo + " " + (sideNumber + 1),
																	 "//schem searchfolder " + deep + caseSensitive + argTwo + " " + side,
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
				Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
			}
		} else {
			try {
				final @Nullable Path listPath = schemPath != null ? schemPath.resolve(argTwo).toAbsolutePath() : null;
				final @Nullable File directory = listPath != null ? listPath.toFile() : null;
				if (directory == null || !directory.isDirectory()) {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
								  ChatColor.DARK_GREEN + argTwo + ChatColor.RED + " is no folder.");
					return;
				}
				final @NotNull List<File> files = Objects.notNull(BaseFileUtils.searchFolders(directory, deepSearch, argThree, caseSensitiveSearch));
				final double count = Objects.notNull(files).size();
				final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

				if (spaceLists) {
					p.sendMessage("");
				}
				if (count < 1) {
					GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
														ChatColor.AQUA + "No folders found",
														ChatColor.AQUA + " ===",
														ChatColor.GRAY + (caseSensitiveSearch ? "schematics/" + argTwo + " [-c]" : "schematics/" + argTwo), p);
				} else {
					GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
														ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side,
														ChatColor.AQUA + " ===",
														ChatColor.GRAY + (caseSensitiveSearch ? "schematics/" + argTwo + " [-c]" : "schematics/" + argTwo), p);

					if (count < listmax) {
						listmax = (int) count;
					}

					for (int i = 0; i < listmax; i++) {
						if (SearchFolder.sendListLineFailed(p, schemPath, listPath, files.get(i), i, deepSearch)) {
							return;
						}
					}

					if (side > 1) {
						GlobalMessageUtils.sendScrollMessage("//schem searchfolder " + deep + caseSensitive + argTwo + " " + argThree + " 2",
															 "//schem searchfolder " + deep + caseSensitive + argTwo + " " + argThree + " " + side,
															 ChatColor.DARK_PURPLE + "Page 2",
															 ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
					} else {
						GlobalMessageUtils.sendScrollMessage("",
															 "",
															 ChatColor.DARK_PURPLE + "There is only one page of folders in this list",
															 ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
					}
				}
			} catch (final @NotNull IOException e) {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.DARK_GREEN + argTwo + ChatColor.RED + " could not be accessed, for further information please see [console].");
				Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
			}
		}
	}

	private void defaultCase(final @NotNull String argTwo, final @NotNull String argThree, final @NotNull String argFour, final @Nullable Path schemPath, final @NotNull Player p, final @NotNull String deep, final @NotNull String caseSensitive, final boolean deepSearch, final boolean caseSensitiveSearch, final boolean spaceLists, int listmax) {
		try {
			final @Nullable Path listPath = schemPath != null ? schemPath.resolve(argTwo).toAbsolutePath() : null;
			final @Nullable File directory = listPath != null ? listPath.toFile() : null;
			if (directory == null || !directory.isDirectory()) {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.DARK_GREEN + argTwo + ChatColor.RED + " is no folder.");
				return;
			}

			final @NotNull List<File> files = Objects.notNull(BaseFileUtils.searchFolders(directory, deepSearch, argThree, caseSensitiveSearch));
			final double count = Objects.notNull(files).size();
			final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
			final int sideNumber = Integer.parseInt(argFour);

			if (sideNumber > side) {
				GlobalMessageUtils.sendHoverMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]",
													ChatColor.RED + "There are only " + side + " pages of folders in this list",
													"",
													ChatColor.GRAY + (caseSensitiveSearch ? "schematics/" + argTwo + " [-c]" : "schematics/" + argTwo), p);
				return;
			}
			if (spaceLists) {
				p.sendMessage("");
			}
			if (count < 1) {
				GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
													ChatColor.AQUA + "No folders found",
													ChatColor.AQUA + " ===",
													ChatColor.GRAY + (caseSensitiveSearch ? "schematics/" + argTwo + " [-c]" : "schematics/" + argTwo), p);
			} else {
				GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
													ChatColor.AQUA + "" + (int) count + " Folder | Page " + sideNumber + "/" + side,
													ChatColor.AQUA + " ===",
													ChatColor.GRAY + (caseSensitiveSearch ? "schematics/" + argTwo + " [-c]" : "schematics/" + argTwo), p);

				int id = (sideNumber - 1) * listmax;
				if (count < listmax * sideNumber) {
					listmax = (int) count - (listmax * (sideNumber - 1));
				}

				for (int i = 0; i < listmax; i++) {
					if (SearchFolder.sendListLineFailed(p, schemPath, listPath, files.get(id), id, deepSearch)) {
						return;
					}
					id++;
				}

				if (side > 1) {
					if (sideNumber > 1) {
						if (sideNumber < side) {
							GlobalMessageUtils.sendScrollMessage("//schem searchfolder " + deep + caseSensitive + argTwo + " " + argThree + " " + (sideNumber + 1),
																 "//schem searchfolder " + deep + caseSensitive + argTwo + " " + argThree + " " + (sideNumber - 1),
																 ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
																 ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
						} else {
							GlobalMessageUtils.sendScrollMessage("//schem searchfolder " + deep + caseSensitive + argTwo + " " + argThree + " 1",
																 "//schem searchfolder " + deep + caseSensitive + argTwo + " " + argThree + " " + (sideNumber - 1),
																 ChatColor.DARK_PURPLE + "Page 1",
																 ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
						}
					} else {
						GlobalMessageUtils.sendScrollMessage("//schem searchfolder " + deep + caseSensitive + argTwo + " " + argThree + " " + (sideNumber + 1),
															 "//schem searchfolder " + deep + caseSensitive + argTwo + " " + argThree + " " + side,
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
		} catch (final @NotNull IOException e) {
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.DARK_GREEN + argTwo + ChatColor.RED + " could not be accessed, for further information please see [console].");
			Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
		}
	}

	private void usage(final @NotNull Player p, @NotNull final String slash, @NotNull final String schemAlias) {
		GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
											  SearchFolder.usageMessage(slash, schemAlias),
											  SearchFolder.usageHoverMessage(slash, schemAlias),
											  SearchFolder.usageCommand(slash, schemAlias), p);
	}
}