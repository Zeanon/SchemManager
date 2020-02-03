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


@UtilityClass
public class Search {

	public void execute(final @NotNull String[] args, final @NotNull Player p, final @NotNull String slash, final @NotNull String schemAlias) {
		final int modifierCount;
		final boolean deep;
		final boolean caseSensitive;

		if (args.length > 2 && (args[2].equalsIgnoreCase("-deep") || args[2].equalsIgnoreCase("-d"))) {
			deep = true;

			if (args.length > 3 && (args[3].equalsIgnoreCase("-casesensitive") || args[3].equalsIgnoreCase("-c"))) {
				modifierCount = 2;
				caseSensitive = true;
			} else {
				modifierCount = 1;
				caseSensitive = false;
			}
		} else if (args.length > 2 && (args[2].equalsIgnoreCase("-casesensitive") || args[2].equalsIgnoreCase("-c"))) {
			caseSensitive = true;

			if (args.length > 3 && (args[3].equalsIgnoreCase("-deep") || args[3].equalsIgnoreCase("-d"))) {
				modifierCount = 2;
				deep = true;
			} else {
				modifierCount = 1;
				deep = false;
			}
		} else {
			modifierCount = 0;
			deep = false;
			caseSensitive = false;
		}

		if (args.length <= 5 + modifierCount) {
			if (args.length < 3 + modifierCount) {
				p.sendMessage(ChatColor.RED + "Missing argument for "
							  + ChatColor.YELLOW + "<"
							  + ChatColor.GOLD + "filename"
							  + ChatColor.YELLOW + ">");
				Search.searchUsage(p, slash, schemAlias);
			} else if (args[2 + modifierCount].contains("./")) {
				p.sendMessage(ChatColor.RED + "File '" + args[2 + modifierCount] + "'resolution error: Path is not allowed.");
				Search.searchUsage(p, slash, schemAlias);
			} else if (args.length == 5 + modifierCount
					   && (StringUtils.isNumeric(args[2 + modifierCount])
						   || StringUtils.isNumeric(args[3 + modifierCount])
						   || !StringUtils.isNumeric(args[4 + modifierCount]))) {
				p.sendMessage(ChatColor.RED + "Too many arguments.");
				Search.searchUsage(p, slash, schemAlias);
			} else {
				Search.onSearch(p, args, deep, caseSensitive, modifierCount);
			}
		} else {
			p.sendMessage(ChatColor.RED + "Too many arguments.");
			Search.searchUsage(p, slash, schemAlias);
		}
	}

	private void onSearch(final @NotNull Player p, final @NotNull String[] args, final boolean deepSearch, final boolean caseSensitiveSearch, final int modifierCount) {
		new BukkitRunnable() {
			@Override
			public void run() {
				final byte listmax = ConfigUtils.getByte("Listmax");
				final boolean spaceLists = ConfigUtils.getBoolean("Space Lists");
				final @Nullable Path schemPath = WorldEditModeSchemUtils.getSchemPath();
				final @Nullable java.util.List<String> extensions = ConfigUtils.getStringList("File Extensions");

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
						Search.threeArgs(args[2 + modifierCount], schemPath, p, deep, caseSensitive, extensions, deepSearch, caseSensitiveSearch, spaceLists, listmax);
						break;
					case 4:
						Search.fourArgs(args[2 + modifierCount], args[3 + modifierCount], schemPath, p, deep, caseSensitive, extensions, deepSearch, caseSensitiveSearch, spaceLists, listmax);
						break;
					default:
						Search.defaultCase(args[2 + modifierCount], args[3 + modifierCount], args[4 + modifierCount], schemPath, p, deep, caseSensitive, extensions, deepSearch, caseSensitiveSearch, spaceLists, listmax);
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}

	private @NotNull File[] getFileArray(final @NotNull File directory, final @NotNull List<String> extensions, final boolean deepSearch, final boolean caseSensitive, final @NotNull String sequence) throws IOException {
		final @NotNull java.util.List<File> files = new GapList<>();
		for (final @NotNull File file : BaseFileUtils.listFiles(directory, deepSearch, extensions)) {
			if ((!caseSensitive && BaseFileUtils.removeExtension(file.getName()).toLowerCase().contains(sequence.toLowerCase())) || (caseSensitive && BaseFileUtils.removeExtension(file.getName()).contains(sequence))) {
				files.add(file);
			}
		}
		final @NotNull File[] fileArray = files.toArray(new File[0]);
		return fileArray;
	}

	private boolean sendListLineFailed(final @NotNull Player p, final @NotNull Path schemFolderPath, final @NotNull Path listPath, final @NotNull File file, final int id, final boolean deepSearch) {
		return (!Search.sendListLine(p, schemFolderPath, listPath, file, id, deepSearch));
	}

	private boolean sendListLine(final @NotNull Player p, final @NotNull Path schemFolderPath, final @NotNull Path listPath, final @NotNull File file, final int id, final boolean deepSearch) {
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

			if (deepSearch) {
				MessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ",
												ChatColor.GOLD + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + shortenedRelativePath + ChatColor.DARK_GRAY + "]",
												ChatColor.RED + "Load " + ChatColor.GOLD + path + ChatColor.RED + " to your clipboard",
												"//schem load " + path, p);
			} else {
				MessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ",
												ChatColor.GOLD + name,
												ChatColor.RED + "Load " + ChatColor.GOLD + path + ChatColor.RED + " to your clipboard",
												"//schem load " + path, p);
			}
			return true;
		} catch (final @NotNull IOException e) {
			e.printStackTrace();
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.RED + "An Error occurred while getting the filepaths for the schematics, for further information please see [console].");
			return false;
		}
	}

	private void threeArgs(final @NotNull String arg, final @Nullable Path schemPath, final @NotNull Player p, final @NotNull String deep, final @NotNull String caseSensitive, final @Nullable java.util.List<String> extensions, final boolean deepSearch, final boolean caseSensitiveSearch, final boolean spaceLists, byte listmax) {
		try {
			final @Nullable Path listPath = schemPath != null ? schemPath.toRealPath() : null;
			final @Nullable File directory = listPath != null ? listPath.toFile() : null;

			if (directory == null || !directory.isDirectory()) {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.RED + "There is no schematic folder.");
			} else {
				final @NotNull File[] files = Search.getFileArray(directory, Objects.notNull(extensions), deepSearch, caseSensitiveSearch, arg);
				final double count = files.length;
				final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

				if (spaceLists) {
					p.sendMessage("");
				}

				if (count < 1) {
					MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
												  ChatColor.AQUA + "No schematics found",
												  ChatColor.AQUA + " ===",
												  ChatColor.GRAY + (caseSensitiveSearch ? "Schematics [-c]" : "Schematics"), p);
				} else {
					MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
												  ChatColor.AQUA + "" + (int) count + " Schematics | Page 1/" + side,
												  ChatColor.AQUA + " ===",
												  ChatColor.GRAY + (caseSensitiveSearch ? "Schematics [-c]" : "Schematics"), p);
					if (count < listmax) {
						listmax = (byte) count;
					}

					Arrays.sort(files);
					for (byte i = 0; i < listmax; i++) {
						if (Search.sendListLineFailed(p, schemPath, listPath, files[i], i, deepSearch)) {
							return;
						}
					}

					if (side > 1) {
						MessageUtils.sendScrollMessage("//schem search " + deep + caseSensitive + arg + " 2",
													   "//schem search " + deep + caseSensitive + arg + " " + side,
													   ChatColor.DARK_PURPLE + "Page 2",
													   ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
					} else {
						MessageUtils.sendScrollMessage("",
													   "",
													   ChatColor.DARK_PURPLE + "There is only one page of schematics in Search list",
													   ChatColor.DARK_PURPLE + "There is only one page of schematics in Search list", p, ChatColor.BLUE);
					}
				}
			}
		} catch (final @NotNull IOException e) {
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.RED + "Could not access schematic folder, for further information please see [console].");
			e.printStackTrace();
		}
	}

	private void fourArgs(final @NotNull String argTwo, final @NotNull String argThree, final @Nullable Path schemPath, final @NotNull Player p, final @NotNull String deep, final @NotNull String caseSensitive, final @Nullable java.util.List<String> extensions, final boolean deepSearch, final boolean caseSensitiveSearch, final boolean spaceLists, byte listmax) {
		if (StringUtils.isNumeric(argThree)) {
			try {
				final @Nullable Path listPath = schemPath != null ? schemPath.toRealPath() : null;
				final @Nullable File directory = listPath != null ? listPath.toFile() : null;
				if (directory == null || !directory.isDirectory()) {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
								  ChatColor.RED + "There is no schematic folder.");
				} else {
					final @NotNull File[] files = Search.getFileArray(directory, Objects.notNull(extensions), deepSearch, caseSensitiveSearch, argTwo);
					final double count = files.length;
					final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
					final int sideNumber = Integer.parseInt(argThree);

					if (sideNumber > side) {
						MessageUtils.sendHoverMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]",
													  ChatColor.RED + "There are only " + side + " schematics in Search list",
													  "",
													  ChatColor.GRAY + (caseSensitiveSearch ? "Schematics [-c]" : "Schematics"), p);
						return;
					}
					if (spaceLists) {
						p.sendMessage("");
					}
					if (count < 1) {
						MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
													  ChatColor.AQUA + "No schematics found",
													  ChatColor.AQUA + " ===",
													  ChatColor.GRAY + (caseSensitiveSearch ? "Schematics [-c]" : "Schematics"), p);
					} else {
						MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
													  ChatColor.AQUA + "" + (int) count + " Schematics | Page " + sideNumber + "/" + side,
													  ChatColor.AQUA + " ===",
													  ChatColor.GRAY + (caseSensitiveSearch ? "Schematics [-c]" : "Schematics"), p);
						int id = (sideNumber - 1) * listmax;

						if (count < listmax * sideNumber) {
							listmax = (byte) ((int) count - (listmax * (sideNumber - 1)));
						}

						Arrays.sort(files);
						for (byte i = 0; i < listmax; i++) {
							if (Search.sendListLineFailed(p, schemPath, listPath, files[id], id, deepSearch)) {
								return;
							}
							id++;
						}

						if (side > 1) {
							if (sideNumber > 1) {
								if (sideNumber < side) {
									MessageUtils.sendScrollMessage("//schem search " + deep + caseSensitive + argTwo + " " + (sideNumber + 1),
																   "//schem search " + deep + caseSensitive + argTwo + " " + (sideNumber - 1),
																   ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
																   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
								} else {
									MessageUtils.sendScrollMessage("//schem search " + deep + caseSensitive + argTwo + " 1",
																   "//schem search " + deep + caseSensitive + argTwo + " " + (sideNumber - 1),
																   ChatColor.DARK_PURPLE + "Page 1",
																   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
								}
							} else {
								MessageUtils.sendScrollMessage("//schem search " + deep + caseSensitive + argTwo + " " + (sideNumber + 1),
															   "//schem search " + deep + caseSensitive + argTwo + " " + side,
															   ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
															   ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
							}
						} else {
							MessageUtils.sendScrollMessage("",
														   "",
														   ChatColor.DARK_PURPLE + "There is only one page of schematics in Search list",
														   ChatColor.DARK_PURPLE + "There is only one page of schematics in Search list", p, ChatColor.BLUE);
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
					final @NotNull File[] files = Search.getFileArray(directory, Objects.notNull(extensions), deepSearch, caseSensitiveSearch, argThree);
					final double count = files.length;
					final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

					if (spaceLists) {
						p.sendMessage("");
					}

					if (count < 1) {
						MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
													  ChatColor.AQUA + "No schematics found",
													  ChatColor.AQUA + " ===",
													  ChatColor.GRAY + (caseSensitiveSearch ? "Schematics/" + argTwo + " [-c]" : "Schematics/" + argTwo), p);
					} else {
						MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
													  ChatColor.AQUA + "" + (int) count + " Schematics | Page 1/" + side,
													  ChatColor.AQUA + " ===",
													  ChatColor.GRAY + (caseSensitiveSearch ? "Schematics/" + argTwo + " [-c]" : "Schematics/" + argTwo), p);
						if (count < listmax) {
							listmax = (byte) count;
						}

						Arrays.sort(files);
						for (byte i = 0; i < listmax; i++) {
							if (Search.sendListLineFailed(p, schemPath, listPath, files[i], i, deepSearch)) {
								return;
							}
						}

						if (side > 1) {
							MessageUtils.sendScrollMessage("//schem search " + deep + caseSensitive + argTwo + " " + argThree + " 2",
														   "//schem search " + deep + caseSensitive + argTwo + " " + argThree + " " + side,
														   ChatColor.DARK_PURPLE + "Page 2",
														   ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
						} else {
							MessageUtils.sendScrollMessage("",
														   "",
														   ChatColor.DARK_PURPLE + "There is only one page of schematics in Search list",
														   ChatColor.DARK_PURPLE + "There is only one page of schematics in Search list", p, ChatColor.BLUE);
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

	private void defaultCase(final @NotNull String argTwo, final @NotNull String argThree, final @NotNull String argFour, final @Nullable Path schemPath, final @NotNull Player p, final @NotNull String deep, final @NotNull String caseSensitive, final @Nullable java.util.List<String> extensions, final boolean deepSearch, final boolean caseSensitiveSearch, final boolean spaceLists, byte listmax) {
		try {
			final @Nullable Path listPath = schemPath != null ? schemPath.resolve(argTwo).toRealPath() : null;
			final @Nullable File directory = listPath != null ? listPath.toFile() : null;
			if (directory == null || !directory.isDirectory()) {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.GREEN + argTwo + ChatColor.RED + " is no folder.");
			} else {
				final @NotNull File[] files = Search.getFileArray(directory, Objects.notNull(extensions), deepSearch, caseSensitiveSearch, argThree);
				final double count = files.length;
				final int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
				final int sideNumber = Integer.parseInt(argFour);

				if (sideNumber > side) {
					MessageUtils.sendHoverMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]",
												  ChatColor.RED + "There are only " + side + " schematics in Search list",
												  "",
												  ChatColor.GRAY + (caseSensitiveSearch ? "Schematics/" + argTwo + " [-c]" : "Schematics/" + argTwo), p);
					return;
				}

				if (spaceLists) {
					p.sendMessage("");
				}

				if (count < 1) {
					MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
												  ChatColor.AQUA + "No schematics found",
												  ChatColor.AQUA + " ===",
												  ChatColor.GRAY + (caseSensitiveSearch ? "Schematics/" + argTwo + " [-c]" : "Schematics/" + argTwo), p);
				} else {
					MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
												  ChatColor.AQUA + "" + (int) count + " Schematics | Page " + sideNumber + "/" + side,
												  ChatColor.AQUA + " ===",
												  ChatColor.GRAY + (caseSensitiveSearch ? "Schematics/" + argTwo + " [-c]" : "Schematics/" + argTwo), p);
					int id = (sideNumber - 1) * listmax;

					if (count < listmax * sideNumber) {
						listmax = (byte) ((int) count - (listmax * (sideNumber - 1)));
					}

					Arrays.sort(files);
					for (byte i = 0; i < listmax; i++) {
						if (Search.sendListLineFailed(p, schemPath, listPath, files[id], id, deepSearch)) {
							return;
						}
						id++;
					}

					if (side > 1) {
						if (sideNumber > 1) {
							if (sideNumber < side) {
								MessageUtils.sendScrollMessage("//schem search " + deep + caseSensitive + argTwo + " " + argThree + " " + (sideNumber + 1),
															   "//schem search " + deep + caseSensitive + argTwo + " " + argThree + " " + (sideNumber - 1),
															   ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
															   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
							} else {
								MessageUtils.sendScrollMessage("//schem search " + deep + caseSensitive + argTwo + " " + argThree + " 1",
															   "//schem search " + deep + caseSensitive + argTwo + " " + argThree + " " + (sideNumber - 1),
															   ChatColor.DARK_PURPLE + "Page 1",
															   ChatColor.DARK_PURPLE + "Page " + (sideNumber - 1), p, ChatColor.DARK_AQUA);
							}
						} else {
							MessageUtils.sendScrollMessage("//schem search " + deep + caseSensitive + argTwo + " " + argThree + " " + (sideNumber + 1),
														   "//schem search " + deep + caseSensitive + argTwo + " " + argThree + " " + side,
														   ChatColor.DARK_PURPLE + "Page " + (sideNumber + 1),
														   ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
						}
					} else {
						MessageUtils.sendScrollMessage("",
													   "",
													   ChatColor.DARK_PURPLE + "There is only one page of schematics in Search list",
													   ChatColor.DARK_PURPLE + "There is only one page of schematics in Search list", p, ChatColor.BLUE);
					}
				}
			}
		} catch (final @NotNull IOException e) {
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.GREEN + argTwo + ChatColor.RED + " could not be accessed, for further information please see [console].");
			e.printStackTrace();
		}
	}

	private void searchUsage(final @NotNull Player p, final String slash, final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " search "
										+ ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "-d"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.GREEN + "folder"
										+ ChatColor.YELLOW + "] <"
										+ ChatColor.GOLD + "filename"
										+ ChatColor.YELLOW + "> ["
										+ ChatColor.DARK_PURPLE + "page"
										+ ChatColor.YELLOW + "]",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " search "
										+ ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "-d"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.GREEN + "folder"
										+ ChatColor.YELLOW + "] "
										+ ChatColor.GOLD + "example"
										+ ChatColor.YELLOW + " ["
										+ ChatColor.DARK_PURPLE + "page"
										+ ChatColor.YELLOW + "]",
										slash + schemAlias + " search ", p);
	}
}