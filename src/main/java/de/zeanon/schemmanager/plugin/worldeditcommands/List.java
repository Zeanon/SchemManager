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
import java.util.logging.Level;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class List {

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
						List.usage(p, slash, schemAlias);
					} else if (args.length >= 3 + modifierCount && (args[2 + modifierCount].contains("./") || args[2 + modifierCount].contains(".\\"))) {
						p.sendMessage(ChatColor.RED + "File '" + args[2 + modifierCount] + "' resolution error: Path is not allowed.");
						List.usage(p, slash, schemAlias);
					} else {
						List.executeInternally(p, args, deep, modifierCount);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					List.usage(p, slash, schemAlias);
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}

	public @NotNull
	String usageMessage(final @NotNull String slash, final @NotNull String schemAlias) {
		return ChatColor.GRAY + slash + schemAlias
			   + ChatColor.AQUA + " list "
			   + ChatColor.YELLOW + "["
			   + ChatColor.DARK_PURPLE + "-d"
			   + ChatColor.YELLOW + "] ["
			   + ChatColor.DARK_GREEN + "folder"
			   + ChatColor.YELLOW + "] ["
			   + ChatColor.DARK_PURPLE + "page"
			   + ChatColor.YELLOW + "]";
	}

	public @NotNull
	String usageHoverMessage(final @NotNull String slash, final @NotNull String schemAlias) {
		return ChatColor.RED + "e.g. "
			   + ChatColor.GRAY + slash + schemAlias
			   + ChatColor.AQUA + " list "
			   + ChatColor.YELLOW + "["
			   + ChatColor.DARK_PURPLE + "-d"
			   + ChatColor.YELLOW + "] ["
			   + ChatColor.DARK_GREEN + "folder"
			   + ChatColor.YELLOW + "] ["
			   + ChatColor.DARK_PURPLE + "page"
			   + ChatColor.YELLOW + "]";
	}

	public @NotNull
	String usageCommand(final @NotNull String slash, final @NotNull String schemAlias) {
		return slash + schemAlias + " list ";
	}

	private void executeInternally(final @NotNull Player p, final @NotNull String[] args, final boolean deepSearch, final int modifierCount) {
		final int listmax = ConfigUtils.getInt("Listmax");
		final boolean spaceLists = ConfigUtils.getBoolean("Space Lists");
		final @Nullable Path schemPath = SchemUtils.getSchemPath();
		final @Nullable java.util.List<String> extensions = ConfigUtils.getStringList("File Extensions");

		final @NotNull String deep;
		if (deepSearch) {
			deep = "-d ";
		} else {
			deep = "";
		}

		switch (args.length - modifierCount) {
			case 2:
				List.twoArgs(schemPath, p, deep, extensions, deepSearch, spaceLists, listmax);
				break;
			case 3:
				List.threeArgs(args[2 + modifierCount], schemPath, p, deep, extensions, deepSearch, spaceLists, listmax);
				break;
			default:
				List.defaultCase(args[2 + modifierCount], args[3 + modifierCount], schemPath, p, deep, extensions, deepSearch, spaceLists, listmax);
		}
	}

	private boolean sendListLineFailed(final @NotNull Player p, final @NotNull Path schemFolderPath, final @NotNull Path listPath, final @NotNull File file, final int id, final boolean deepSearch) {
		try {
			final @NotNull String name;
			final @NotNull String path = FilenameUtils.separatorsToUnix(schemFolderPath.toRealPath().relativize(file.toPath().toRealPath()).toString());
			final @Nullable String shortenedRelativePath = deepSearch
														   ? FilenameUtils.separatorsToUnix(listPath.relativize(file.toPath().toRealPath()).toString())
														   : null;

			if (BaseFileUtils.getExtension(file.getName()).equalsIgnoreCase(Objects.notNull(ConfigUtils.getStringList("File Extensions")).get(0))) {
				name = BaseFileUtils.removeExtension(file.getName());
			} else {
				name = file.getName();
			}

			if (file.isDirectory()) {
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
			} else {
				if (deepSearch) {
					GlobalMessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ",
														  ChatColor.GOLD + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + shortenedRelativePath + ChatColor.DARK_GRAY + "]",
														  ChatColor.RED + "Load " + ChatColor.GOLD + path + ChatColor.RED + " to your clipboard.",
														  "//schem load " + path, p);
				} else {
					GlobalMessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ",
														  ChatColor.GOLD + name,
														  ChatColor.RED + "Load " + ChatColor.GOLD + path + ChatColor.RED + " to your clipboard.",
														  "//schem load " + path, p);
				}
			}
			return false;
		} catch (final @NotNull IOException e) {
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.RED + "An Error occurred while getting the filepaths for the schematics, for further information please see [console].");
			SchemManager.getChatLogger().log(Level.SEVERE, "Error while getting the filepaths for the schematics", e);
			return true;
		}
	}

	private void twoArgs(final @Nullable Path schemPath, final @NotNull Player p, final @NotNull String deep, final @Nullable java.util.List<String> extensions, final boolean deepSearch, final boolean spaceLists, int listmax) {
		try {
			final @Nullable Path listPath = schemPath != null ? schemPath.toAbsolutePath() : null;
			final @Nullable File directory = listPath != null ? listPath.toFile() : null;

			if (directory == null || !directory.isDirectory()) {
				throw new IOException("Schematic folder does not exist");
			} else {
				final @NotNull java.util.List<File> files = BaseFileUtils.listFilesOfTypeAndFolders(directory, deepSearch, Objects.notNull(extensions));
				final double count = files.size();
				final int side = (int) (((count / listmax) % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

				if (spaceLists) {
					p.sendMessage("");
				}

				if (count < 1) {
					GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
														ChatColor.AQUA + "No schematics or folders found",
														ChatColor.AQUA + " ===",
														ChatColor.GRAY + "schematics", p);
				} else {
					GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
														ChatColor.AQUA + "" + (int) count + " Schematics and Folders | Page 1/" + side,
														ChatColor.AQUA + " ===",
														ChatColor.GRAY + "schematics", p);
					if (count < listmax) {
						listmax = (int) count;
					}

					for (int i = 0; i < listmax; i++) {
						if (List.sendListLineFailed(p, schemPath, listPath, files.get(i), i, deepSearch)) {
							return;
						}
					}

					if (side > 1) {
						GlobalMessageUtils.sendScrollMessage("//schem list " + deep + "2",
															 "//schem list " + deep + side,
															 ChatColor.DARK_PURPLE + "Page 2",
															 ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
					} else {
						GlobalMessageUtils.sendScrollMessage("",
															 "",
															 ChatColor.DARK_PURPLE + "There is only one page of schematics and folders in this list",
															 ChatColor.DARK_PURPLE + "There is only one page of schematics and folders in this list", p, ChatColor.BLUE);
					}
				}
			}
		} catch (final @NotNull IOException e) {
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Could not access schematic folder, for further information please see [console].");
			SchemManager.getChatLogger().log(Level.SEVERE, "Error while accessing the schematic folder", e);
		}
	}

	private void threeArgs(final @NotNull String arg, final @Nullable Path schemPath, final @NotNull Player p, final @NotNull String deep, final @Nullable java.util.List<String> extensions, final boolean deepSearch, final boolean spaceLists, int listmax) {
		if (StringUtils.isNumeric(arg)) {
			try {
				final @Nullable Path listPath = schemPath != null ? schemPath.toAbsolutePath() : null;
				final @Nullable File directory = listPath != null ? listPath.toFile() : null;

				if (directory == null || !directory.isDirectory()) {
					throw new IOException("Schematic folder does not exist");
				} else {
					final @NotNull java.util.List<File> files = BaseFileUtils.listFilesOfTypeAndFolders(directory, deepSearch, Objects.notNull(extensions));
					final double count = files.size();
					final int side = (int) (((count / listmax) % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
					final int sideNumber = Integer.parseInt(arg);

					if (sideNumber > side) {
						GlobalMessageUtils.sendHoverMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]",
															ChatColor.RED + "There are only " + side + " pages of schematics in this list",
															"",
															ChatColor.GRAY + "schematics", p);
						return;
					}

					if (spaceLists) {
						p.sendMessage("");
					}

					if (count < 1) {
						GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
															ChatColor.AQUA + "No schematics or folders found",
															ChatColor.AQUA + " ===",
															ChatColor.GRAY + "schematics", p);
					} else {
						GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
															ChatColor.AQUA + "" + (int) count + " Schematics and Folders | Page " + sideNumber + "/" + side,
															ChatColor.AQUA + " ===", ChatColor.GRAY + "schematics", p);
						int id = (sideNumber - 1) * listmax;

						if (count < listmax * sideNumber) {
							listmax = (int) count - (listmax * (sideNumber - 1));
						}

						for (int i = 0; i < listmax; i++) {
							if (List.sendListLineFailed(p, schemPath, listPath, files.get(id), id, deepSearch)) {
								return;
							}
							id++;
						}

						if (side > 1) {
							if (sideNumber > 1) {
								if (sideNumber < side) {
									GlobalMessageUtils.sendScrollMessage("//schem list " + deep + (sideNumber + 1),
																		 "//schem list " + deep + (sideNumber - 1),
																		 ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
																		 ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
								} else {
									GlobalMessageUtils.sendScrollMessage("//schem list " + deep + "1",
																		 "//schem list " + deep + (sideNumber - 1),
																		 ChatColor.DARK_PURPLE + "Page 1",
																		 ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
								}
							} else {
								GlobalMessageUtils.sendScrollMessage("//schem list " + deep + (sideNumber + 1),
																	 "//schem list " + deep + side,
																	 ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
																	 ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
							}
						} else {
							GlobalMessageUtils.sendScrollMessage("",
																 "",
																 ChatColor.DARK_PURPLE + "There is only one page of schematics and folders in this list",
																 ChatColor.DARK_PURPLE + "There is only one page of schematics and folders in this list", p, ChatColor.BLUE);
						}
					}
				}
			} catch (final @NotNull IOException e) {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.RED + "Could not access schematic folder, for further information please see [console].");
				SchemManager.getChatLogger().log(Level.SEVERE, "Error while accessing the schematic folder", e);
			}
		} else {
			try {
				final @Nullable Path listPath = schemPath != null ? schemPath.resolve(arg).toAbsolutePath() : null;
				final @Nullable File directory = listPath != null ? listPath.toFile() : null;

				if (directory == null || !directory.isDirectory()) {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
								  ChatColor.DARK_GREEN + arg + ChatColor.RED + " is no folder.");
					return;
				}
				final @NotNull java.util.List<File> files = BaseFileUtils.listFilesOfTypeAndFolders(directory, deepSearch, Objects.notNull(extensions));
				final double count = files.size();
				final int side = (int) (((count / listmax) % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

				if (spaceLists) {
					p.sendMessage("");
				}

				if (count < 1) {
					GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
														ChatColor.AQUA + "No schematics or folders found",
														ChatColor.AQUA + " ===",
														ChatColor.GRAY + "schematics/" + arg, p);
				} else {
					GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
														ChatColor.AQUA + "" + (int) count + " Schematics and Folders | Page 1/" + side,
														ChatColor.AQUA + " ===",
														ChatColor.GRAY + "schematics/" + arg, p);
					if (count < listmax) {
						listmax = (int) count;
					}

					for (int i = 0; i < listmax; i++) {
						if (List.sendListLineFailed(p, schemPath, listPath, files.get(i), i, deepSearch)) {
							return;
						}
					}

					if (side > 1) {
						GlobalMessageUtils.sendScrollMessage("//schem list " + deep + arg + " 2",
															 "//schem list " + deep + arg + " " + side,
															 ChatColor.DARK_PURPLE + "Page 2",
															 ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
					} else {
						GlobalMessageUtils.sendScrollMessage("",
															 "",
															 ChatColor.DARK_PURPLE + "There is only one page of schematics and folders in this list",
															 ChatColor.DARK_PURPLE + "There is only one page of schematics and folders in this list", p, ChatColor.BLUE);
					}
				}
			} catch (final @NotNull IOException e) {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.DARK_GREEN + arg + ChatColor.RED + " could not be accessed, for further information please see [console].");
				SchemManager.getChatLogger().log(Level.SEVERE, String.format("Error while accessing %s", arg), e);
			}
		}
	}

	private void defaultCase(final @NotNull String argTwo, final @NotNull String argThree, final @Nullable Path schemPath, final @NotNull Player p, final @NotNull String deep, final @Nullable java.util.List<String> extensions, final boolean deepSearch, final boolean spaceLists, int listmax) {
		try {
			final @Nullable Path listPath = schemPath != null ? schemPath.resolve(argTwo).toAbsolutePath() : null;
			final @Nullable File directory = listPath != null ? listPath.toFile() : null;

			if (directory == null || !directory.isDirectory()) {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.DARK_GREEN + argTwo + ChatColor.RED + " is no folder.");
				return;
			}

			final @NotNull java.util.List<File> files = BaseFileUtils.listFilesOfTypeAndFolders(directory, deepSearch, Objects.notNull(extensions));
			final double count = files.size();
			final int side = (int) (((count / listmax) % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
			final int sideNumber = Integer.parseInt(argThree);

			if (sideNumber > side) {
				GlobalMessageUtils.sendHoverMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]",
													ChatColor.RED + "There are only " + side + " schematics in this list",
													"",
													ChatColor.GRAY + "schematics/" + argTwo, p);
				return;
			}

			if (spaceLists) {
				p.sendMessage("");
			}

			if (count < 1) {
				GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
													ChatColor.AQUA + "No schematics or folders found",
													ChatColor.AQUA + " ===",
													ChatColor.GRAY + "schematics/" + argTwo, p);
			} else {
				GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
													ChatColor.AQUA + "" + (int) count + " Schematics and Folders | Page " + sideNumber + "/" + side,
													ChatColor.AQUA + " ===", ChatColor.GRAY + "schematics/" + argTwo, p);
				int id = (sideNumber - 1) * listmax;

				if (count < listmax * sideNumber) {
					listmax = (int) count - (listmax * (sideNumber - 1));
				}

				for (int i = 0; i < listmax; i++) {
					if (List.sendListLineFailed(p, schemPath, listPath, files.get(id), id, deepSearch)) {
						return;
					}
					id++;
				}

				if (side > 1) {
					if (sideNumber > 1) {
						if (sideNumber < side) {
							GlobalMessageUtils.sendScrollMessage("//schem list " + deep + argTwo + " " + (sideNumber + 1),
																 "//schem list " + deep + argTwo + " " + (sideNumber - 1),
																 ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
																 ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
						} else {
							GlobalMessageUtils.sendScrollMessage("//schem list " + deep + argTwo + " 1",
																 "//schem list " + deep + argTwo + " " + (sideNumber - 1),
																 ChatColor.DARK_PURPLE + "Page 1",
																 ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
						}
					} else {
						GlobalMessageUtils.sendScrollMessage("//schem list " + deep + argTwo + " " + (sideNumber + 1),
															 "//schem list " + deep + argTwo + " " + side,
															 ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
															 ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
					}
				} else {
					GlobalMessageUtils.sendScrollMessage("",
														 "",
														 ChatColor.DARK_PURPLE + "There is only one page of schematics and folders in this list",
														 ChatColor.DARK_PURPLE + "There is only one page of schematics and folders in this list", p, ChatColor.BLUE);
				}
			}
		} catch (final @NotNull IOException e) {
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.DARK_GREEN + argTwo + ChatColor.RED + " could not be accessed, for further information please see [console].");
			SchemManager.getChatLogger().log(Level.SEVERE, String.format("Error while accessing %s", argTwo), e);
		}
	}

	private void usage(final @NotNull Player p, @NotNull final String slash, @NotNull final String schemAlias) {
		GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
											  List.usageMessage(slash, schemAlias),
											  List.usageHoverMessage(slash, schemAlias),
											  List.usageCommand(slash, schemAlias), p);
	}
}