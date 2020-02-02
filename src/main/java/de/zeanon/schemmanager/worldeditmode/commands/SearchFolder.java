package de.zeanon.schemmanager.worldeditmode.commands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.global.utils.MessageUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeSchemUtils;
import de.zeanon.storagemanager.external.browniescollections.GapList;
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

	public void onSearchFolder(final @NotNull Player p, final @NotNull String[] args, final boolean deepSearch, final boolean caseSensitiveSearch, final int modifierCount) {
		new BukkitRunnable() {
			@Override
			public void run() {
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
		}.runTaskAsynchronously(SchemManager.getInstance());
	}

	@NotNull
	private File[] getFileArray(final @NotNull File directory, final boolean deepSearch, final boolean caseSensitive, final @NotNull String sequence) throws IOException {
		final @NotNull java.util.List<File> files = new GapList<>();
		for (final @NotNull File file : BaseFileUtils.listFolders(directory, deepSearch)) {
			if ((!caseSensitive && file.getName().toLowerCase().contains(sequence.toLowerCase())) || (caseSensitive && file.getName().contains(sequence))) {
				files.add(file);
			}
		}
		final @NotNull File[] fileArray = files.toArray(new File[0]);
		return fileArray;
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
													  ChatColor.RED + "There are only " + side + " pages of folders in this list",
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
												  ChatColor.RED + "There are only " + side + " pages of folders in this list",
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
}