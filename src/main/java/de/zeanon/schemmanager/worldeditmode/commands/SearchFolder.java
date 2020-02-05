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
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class SearchFolder {

	public void execute(final @NotNull String[] args, final @NotNull Player p, final @NotNull String slash, final @NotNull String schemAlias) {
		new BukkitRunnable() {
			@SuppressWarnings("DuplicatedCode")
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
						p.sendMessage(ChatColor.RED + "File '" + args[2 + modifierCount] + "'resolution error: Path is not allowed.");
						SearchFolder.usage(p, slash, schemAlias);
					} else if (args.length == 5 + modifierCount
							   && (StringUtils.isNumeric(args[2 + modifierCount])
								   || StringUtils.isNumeric(args[3 + modifierCount])
								   || !StringUtils.isNumeric(args[4 + modifierCount]))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						SearchFolder.usage(p, slash, schemAlias);
					} else {
						SearchFolder.onSearchFolder(p, args, deep, caseSensitive, modifierCount);
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
			   + ChatColor.GREEN + "folder"
			   + ChatColor.YELLOW + "] <"
			   + ChatColor.GOLD + "filename"
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
			   + ChatColor.GREEN + "folder"
			   + ChatColor.YELLOW + "] "
			   + ChatColor.GOLD + "example"
			   + ChatColor.YELLOW + " ["
			   + ChatColor.DARK_PURPLE + "page"
			   + ChatColor.YELLOW + "]";
	}

	public @NotNull String usageCommand(final @NotNull String slash, final @NotNull String schemAlias) {
		return slash + schemAlias + " searchfolder ";
	}

	private void onSearchFolder(final @NotNull Player p, final @NotNull String[] args, final boolean deepSearch, final boolean caseSensitiveSearch, final int modifierCount) {
		final byte listmax = ConfigUtils.getByte("Listmax");
		final boolean spaceLists = ConfigUtils.getBoolean("Space Lists");
		final @Nullable Path schemPath = WorldEditModeSchemUtils.getSchemPath();

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

	private @NotNull
	File[] getFileArray(final @NotNull File directory, final boolean deepSearch, final boolean caseSensitive, final @NotNull String sequence) throws IOException {
		return BaseFileUtils.listFiles(directory, deepSearch)
							.parallelStream()
							.filter(file -> (!caseSensitive && file.getName().toLowerCase().contains(sequence.toLowerCase()))
											|| (caseSensitive && file.getName().contains(sequence)))
							.toArray(File[]::new);
	}

	private boolean sendListLineFailed(final @NotNull Player p, final @NotNull Path schemFolderPath, final @NotNull Path listPath, final @NotNull File file, final int id, final boolean deepSearch) {
		return (!SearchFolder.sendListLine(p, schemFolderPath, listPath, file, id, deepSearch));
	}

	private boolean sendListLine(final @NotNull Player p, final @NotNull Path schemFolderPath, final @NotNull Path listPath, final @NotNull File file, final int id, final boolean deepSearch) {
		try {
			final @NotNull String name = file.getName();
			final @NotNull String path = FilenameUtils.separatorsToUnix(schemFolderPath.toRealPath().relativize(file.toPath().toRealPath()).toString());
			final @Nullable String shortenedRelativePath = deepSearch ? FilenameUtils.separatorsToUnix(listPath.relativize(file.toPath().toRealPath()).toString()) : null;

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
			e.printStackTrace();
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.RED + "An Error occurred while getting the filepaths for the folders, for further information please see [console].");
			return false;
		}
	}

	private void threeArgs(final @NotNull String arg, final @Nullable Path schemPath, final @NotNull Player p, final @NotNull String deep, final @NotNull String caseSensitive, final boolean deepSearch, final boolean caseSensitiveSearch, final boolean spaceLists, byte listmax) {
		try {
			final @Nullable Path listPath = schemPath != null ? schemPath.toRealPath() : null;
			final @Nullable File directory = listPath != null ? listPath.toFile() : null;
			if (directory == null || !directory.isDirectory()) {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.RED + "There is no schematic folder.");
			} else {
				final @NotNull File[] files = SearchFolder.getFileArray(directory, deepSearch, caseSensitiveSearch, arg);
				final double count = files.length;
				final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

				if (spaceLists) {
					p.sendMessage("");
				}

				if (count < 1) {
					MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
												  ChatColor.AQUA + "No folders found",
												  ChatColor.AQUA + " ===",
												  ChatColor.GRAY + (caseSensitiveSearch ? "Schematics [-c]" : "Schematics"), p);
				} else {
					MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
												  ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side,
												  ChatColor.AQUA + " ===",
												  ChatColor.GRAY + (caseSensitiveSearch ? "Schematics [-c]" : "Schematics"), p);
					if (count < listmax) {
						listmax = (byte) count;
					}

					Arrays.sort(files);
					for (byte i = 0; i < listmax; i++) {
						if (SearchFolder.sendListLineFailed(p, schemPath, listPath, files[i], i, deepSearch)) {
							return;
						}
					}

					if (side > 1) {
						MessageUtils.sendScrollMessage("//schem searchfolder " + deep + caseSensitive + arg + " 2",
													   "//schem searchfolder " + deep + caseSensitive + arg + " " + side,
													   ChatColor.DARK_PURPLE + "Page 2",
													   ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
					} else {
						MessageUtils.sendScrollMessage("",
													   "",
													   ChatColor.DARK_PURPLE + "There is only one page of folders in SearchFolder list",
													   ChatColor.DARK_PURPLE + "There is only one page of folders in SearchFolder list", p, ChatColor.BLUE);
					}
				}
			}
		} catch (final @NotNull IOException e) {
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.RED + "Could not access schematic folder, for further information please see [console].");
			e.printStackTrace();
		}
	}

	private void fourArgs(final @NotNull String argTwo, final @NotNull String argThree, final @Nullable Path schemPath, final @NotNull Player p, final @NotNull String deep, final @NotNull String caseSensitive, final boolean deepSearch, final boolean caseSensitiveSearch, final boolean spaceLists, byte listmax) {
		if (StringUtils.isNumeric(argThree)) {
			try {
				final @Nullable Path listPath = schemPath != null ? schemPath.toRealPath() : null;
				final @Nullable File directory = listPath != null ? listPath.toFile() : null;
				if (directory == null || !directory.isDirectory()) {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
								  ChatColor.RED + "There is no schematic folder.");
				} else {
					final @NotNull File[] files = SearchFolder.getFileArray(directory, deepSearch, caseSensitiveSearch, argTwo);
					final double count = files.length;
					final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
					final int sideNumber = Integer.parseInt(argThree);

					if (sideNumber > side) {
						MessageUtils.sendHoverMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]",
													  ChatColor.RED + "There are only " + side + " pages of folders in SearchFolder list",
													  "",
													  ChatColor.GRAY + (caseSensitiveSearch ? "Schematics [-c]" : "Schematics"), p);
						return;
					}
					if (spaceLists) {
						p.sendMessage("");
					}
					if (count < 1) {
						MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
													  ChatColor.AQUA + "No folders found",
													  ChatColor.AQUA + " ===",
													  ChatColor.GRAY + (caseSensitiveSearch ? "Schematics [-c]" : "Schematics"), p);
					} else {
						MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
													  ChatColor.AQUA + "" + (int) count + " Folder | Page " + sideNumber + "/" + side,
													  ChatColor.AQUA + " ===",
													  ChatColor.GRAY + (caseSensitiveSearch ? "Schematics [-c]" : "Schematics"), p);

						int id = (sideNumber - 1) * listmax;
						if (count < listmax * sideNumber) {
							listmax = (byte) ((int) count - (listmax * (sideNumber - 1)));
						}
						for (byte i = 0; i < listmax; i++) {
							if (SearchFolder.sendListLineFailed(p, schemPath, listPath, files[id], id, deepSearch)) {
								return;
							}
							id++;
						}

						if (side > 1) {
							if (sideNumber > 1) {
								if (sideNumber < side) {
									MessageUtils.sendScrollMessage("//schem searchfolder " + deep + caseSensitive + argTwo + " " + (sideNumber + 1),
																   "//schem searchfolder " + deep + caseSensitive + argTwo + " " + (sideNumber - 1),
																   ChatColor.RED + "Page " + (sideNumber + 1),
																   ChatColor.RED + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
								} else {
									MessageUtils.sendScrollMessage("//schem searchfolder " + deep + caseSensitive + argTwo + " 1",
																   "//schem searchfolder " + deep + caseSensitive + argTwo + " " + (sideNumber - 1),
																   ChatColor.DARK_PURPLE + "Page 1",
																   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
								}
							} else {
								MessageUtils.sendScrollMessage("//schem searchfolder " + deep + caseSensitive + argTwo + " " + (sideNumber + 1),
															   "//schem searchfolder " + deep + caseSensitive + argTwo + " " + side,
															   ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
															   ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
							}
						} else {
							MessageUtils.sendScrollMessage("",
														   "",
														   ChatColor.DARK_PURPLE + "There is only one page of folders in SearchFolder list",
														   ChatColor.DARK_PURPLE + "There is only one page of folders in SearchFolder list", p, ChatColor.BLUE);
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
				final @Nullable Path listPath = schemPath != null ? schemPath.resolve(argTwo).toRealPath() : null;
				final @Nullable File directory = listPath != null ? listPath.toFile() : null;
				if (directory == null || !directory.isDirectory()) {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
								  ChatColor.GREEN + argTwo + ChatColor.RED + " is no folder.");
				} else {
					final @NotNull File[] files = SearchFolder.getFileArray(directory, deepSearch, caseSensitiveSearch, argThree);
					final double count = files.length;
					final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

					if (spaceLists) {
						p.sendMessage("");
					}
					if (count < 1) {
						MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
													  ChatColor.AQUA + "No folders found",
													  ChatColor.AQUA + " ===",
													  ChatColor.GRAY + (caseSensitiveSearch ? "Schematics/" + argTwo + " [-c]" : "Schematics/" + argTwo), p);
					} else {
						MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
													  ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side,
													  ChatColor.AQUA + " ===",
													  ChatColor.GRAY + (caseSensitiveSearch ? "Schematics/" + argTwo + " [-c]" : "Schematics/" + argTwo), p);

						if (count < listmax) {
							listmax = (byte) count;
						}
						for (byte i = 0; i < listmax; i++) {
							if (SearchFolder.sendListLineFailed(p, schemPath, listPath, files[i], i, deepSearch)) {
								return;
							}
						}

						if (side > 1) {
							MessageUtils.sendScrollMessage("//schem searchfolder " + deep + caseSensitive + argTwo + " " + argThree + " 2",
														   "//schem searchfolder " + deep + caseSensitive + argTwo + " " + argThree + " " + side,
														   ChatColor.DARK_PURPLE + "Page 2",
														   ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
						} else {
							MessageUtils.sendScrollMessage("",
														   "",
														   ChatColor.DARK_PURPLE + "There is only one page of folders in SearchFolder list",
														   ChatColor.DARK_PURPLE + "There is only one page of folders in SearchFolder list", p, ChatColor.BLUE);
						}
					}
				}
			} catch (final @NotNull IOException e) {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.GREEN + argTwo + ChatColor.RED + " could not be accessed, for further information please see [console].");
				e.printStackTrace();
			}
		}
	}

	private void defaultCase(final @NotNull String argTwo, final @NotNull String argThree, final @NotNull String argFour, final @Nullable Path schemPath, final @NotNull Player p, final @NotNull String deep, final @NotNull String caseSensitive, final boolean deepSearch, final boolean caseSensitiveSearch, final boolean spaceLists, byte listmax) {
		try {
			final @Nullable Path listPath = schemPath != null ? schemPath.resolve(argTwo).toRealPath() : null;
			final @Nullable File directory = listPath != null ? listPath.toFile() : null;
			if (directory == null || !directory.isDirectory()) {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.GREEN + argTwo + ChatColor.RED + " is no folder.");
			} else {
				final @NotNull File[] files = SearchFolder.getFileArray(directory, deepSearch, caseSensitiveSearch, argThree);
				final double count = files.length;
				final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
				final int sideNumber = Integer.parseInt(argFour);

				if (sideNumber > side) {
					MessageUtils.sendHoverMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]",
												  ChatColor.RED + "There are only " + side + " pages of folders in SearchFolder list",
												  "",
												  ChatColor.GRAY + (caseSensitiveSearch ? "Schematics/" + argTwo + " [-c]" : "Schematics/" + argTwo), p);
					return;
				}
				if (spaceLists) {
					p.sendMessage("");
				}
				if (count < 1) {
					MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
												  ChatColor.AQUA + "No folders found",
												  ChatColor.AQUA + " ===",
												  ChatColor.GRAY + (caseSensitiveSearch ? "Schematics/" + argTwo + " [-c]" : "Schematics/" + argTwo), p);
				} else {
					MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
												  ChatColor.AQUA + "" + (int) count + " Folder | Page " + sideNumber + "/" + side,
												  ChatColor.AQUA + " ===",
												  ChatColor.GRAY + (caseSensitiveSearch ? "Schematics/" + argTwo + " [-c]" : "Schematics/" + argTwo), p);

					int id = (sideNumber - 1) * listmax;
					if (count < listmax * sideNumber) {
						listmax = (byte) ((int) count - (listmax * (sideNumber - 1)));
					}
					for (byte i = 0; i < listmax; i++) {
						if (SearchFolder.sendListLineFailed(p, schemPath, listPath, files[id], id, deepSearch)) {
							return;
						}
						id++;
					}

					if (side > 1) {
						if (sideNumber > 1) {
							if (sideNumber < side) {
								MessageUtils.sendScrollMessage("//schem searchfolder " + deep + caseSensitive + argTwo + " " + argThree + " " + (sideNumber + 1),
															   "//schem searchfolder " + deep + caseSensitive + argTwo + " " + argThree + " " + (sideNumber - 1),
															   ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
															   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
							} else {
								MessageUtils.sendScrollMessage("//schem searchfolder " + deep + caseSensitive + argTwo + " " + argThree + " 1",
															   "//schem searchfolder " + deep + caseSensitive + argTwo + " " + argThree + " " + (sideNumber - 1),
															   ChatColor.DARK_PURPLE + "Page 1",
															   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
							}
						} else {
							MessageUtils.sendScrollMessage("//schem searchfolder " + deep + caseSensitive + argTwo + " " + argThree + " " + (sideNumber + 1),
														   "//schem searchfolder " + deep + caseSensitive + argTwo + " " + argThree + " " + side,
														   ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
														   ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
						}
					} else {
						MessageUtils.sendScrollMessage("",
													   "",
													   ChatColor.DARK_PURPLE + "There is only one page of folders in SearchFolder list",
													   ChatColor.DARK_PURPLE + "There is only one page of folders in SearchFolder list", p, ChatColor.BLUE);
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
										SearchFolder.usageMessage(slash, schemAlias),
										SearchFolder.usageHoverMessage(slash, schemAlias),
										SearchFolder.usageCommand(slash, schemAlias), p);
	}
}