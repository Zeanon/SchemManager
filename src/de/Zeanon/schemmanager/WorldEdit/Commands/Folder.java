package de.zeanon.schemmanager.worldedit.commands;

import de.zeanon.schemmanager.worldedit.helper.Helper;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Folder {

	public static boolean onFolder(Player p, String[] args, boolean deepSearch) {
		int listmax = Helper.getInt("Listmax");
		String schemFolderPath = Helper.getSchemPath();
		boolean spaceLists = Helper.getBoolean("Space Lists");

		String deep = "";
		if (deepSearch) {
			deep = "-deep ";
		}

		if (args.length == 2) {
			int side = 0;

			File directory = new File(schemFolderPath);
			if (!directory.exists() || !directory.isDirectory()) {
				p.sendMessage(ChatColor.RED + "There is no schematic folder.");
				return false;
			} else {
				ArrayList<File> rawFiles = Helper.getFolders(directory, deepSearch);
				File[] files = rawFiles.toArray(new File[rawFiles.size()]);
				Arrays.sort(files);

				double count = files.length;
				double side_count = count / listmax;
				if (side_count % 1 != 0) {
					side = (int) side_count + 1;
				} else {
					side = (int) side_count;
				}
				if (count < listmax) {
					listmax = (int) count;
				}

				if (spaceLists) {
					p.sendMessage(" ");
				}
				if (count < 1) {
					Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Folder | Page 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
					return true;
				} else {
					Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);

					for (int i = 0; i < listmax; i++) {
						if (files[i].isDirectory()) {
							String name = files[i].getName();
							String path = files[i].getAbsolutePath().replaceAll(schemFolderPath, "").replaceAll("\\\\", "/");
							if (deepSearch) {
								Helper.sendCommandMessage(ChatColor.RED + Integer.toString(i + 1) + ": ", ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
							} else {
								Helper.sendCommandMessage(ChatColor.RED + Integer.toString(i + 1) + ": ", ChatColor.GREEN + name, ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
							}
						}
					}

					if (side > 1) {
						Helper.sendScrollMessage("//schem folder " + deep + "2", "//schem folder " + deep + side, ChatColor.DARK_PURPLE + "Page 2", ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
						return true;
					} else {
						Helper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
						return true;
					}
				}
			}
		} else if (args.length == 3) {
			if (StringUtils.isNumeric(args[2])) {
				int side_number = Integer.parseInt(args[2]);
				int side = 0;

				File directory = new File(schemFolderPath);
				if (!directory.exists() || !directory.isDirectory()) {
					p.sendMessage(ChatColor.RED + "There is no schematic folder.");
					return false;
				} else {
					ArrayList<File> rawFiles = Helper.getFolders(directory, deepSearch);
					File[] files = rawFiles.toArray(new File[rawFiles.size()]);
					Arrays.sort(files);

					double count = files.length;
					double side_count = count / listmax;
					if (side_count % 1 != 0) {
						side = (int) side_count + 1;
					} else {
						side = (int) side_count;
					}

					if (spaceLists) {
						p.sendMessage(" ");
					}
					if (side_number > side) {
						Helper.sendHoverMessage("", ChatColor.RED + "There are only " + side + " pages of folders in this list", "", ChatColor.GRAY + "global", p);
						return false;
					} else if (count < 1) {
						Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Folder | Page 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
						return true;
					} else {
						Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page " + side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);

						if (count >= listmax * side_number) {
							int id = (side_number - 1) * listmax;
							for (int i = 0; i < listmax; i++) {
								if (files[id].isDirectory()) {
									String name = files[id].getName();
									String path = files[id].getAbsolutePath().replaceAll(schemFolderPath, "").replaceAll("\\\\", "/");
									if (deepSearch) {
										Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
									} else {
										Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name, ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
									}
								}
								id++;
							}
						} else {
							int id = (side_number - 1) * listmax;
							for (int i = 0; i < count - ((side_number - 1) * listmax); i++) {
								if (files[id].isDirectory()) {
									String name = files[id].getName();
									String path = files[id].getAbsolutePath().replaceAll(schemFolderPath, "").replaceAll("\\\\", "/");
									if (deepSearch) {
										Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
									} else {
										Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name, ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
									}
								}
								id++;
							}
						}

						if (side > 1) {
							if (side_number > 1) {
								if (side_number < side) {
									Helper.sendScrollMessage("//schem folder " + deep + (side_number + 1), "//schem folder " + (side_number - 1), ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
									return true;
								} else {
									Helper.sendScrollMessage("//schem folder " + deep + "1", "//schem folder " + deep + (side_number - 1), ChatColor.DARK_PURPLE + "Page 1", ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
									return true;
								}
							} else {
								Helper.sendScrollMessage("//schem folder " + deep + (side_number + 1), "//schem folder " + deep + side, ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
								return true;
							}
						} else {
							Helper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
							return true;
						}
					}
				}
			} else {
				int side = 0;

				File directory = new File(schemFolderPath + args[2]);
				if (!directory.exists() || !directory.isDirectory()) {
					p.sendMessage(ChatColor.RED + args[2] + " is no folder.");
					return false;
				} else {
					ArrayList<File> rawFiles = Helper.getFolders(directory, deepSearch);
					File[] files = rawFiles.toArray(new File[rawFiles.size()]);
					Arrays.sort(files);

					double count = files.length;
					double side_count = count / listmax;
					if (side_count % 1 != 0) {
						side = (int) side_count + 1;
					} else {
						side = (int) side_count;
					}
					if (count < listmax) {
						listmax = (int) count;
					}

					if (spaceLists) {
						p.sendMessage(" ");
					}
					if (count < 1) {
						Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Folder | Page 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
						return true;
					} else {
						Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);

						for (int i = 0; i < listmax; i++) {
							if (files[i].isDirectory()) {
								String name = files[i].getName();
								String path = files[i].getAbsolutePath().replaceAll(schemFolderPath, "").replaceAll("\\\\", "/");
								if (deepSearch) {
									Helper.sendCommandMessage(ChatColor.RED + Integer.toString(i + 1) + ": ", ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
								} else {
									Helper.sendCommandMessage(ChatColor.RED + Integer.toString(i + 1) + ": ", ChatColor.GREEN + name, ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
								}
							}
						}
						if (side > 1) {
							Helper.sendScrollMessage("//schem folder " + deep + args[2] + " 2", "//schem folder " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Page 2", ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
							return true;
						} else {
							Helper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
							return true;
						}
					}
				}
			}
		} else {
			int side_number = Integer.parseInt(args[3]);
			int side = 0;

			File directory = new File(schemFolderPath + args[2]);
			if (!directory.exists() || !directory.isDirectory()) {
				p.sendMessage(ChatColor.RED + args[2] + " is no folder.");
				return false;
			} else {
				ArrayList<File> rawFiles = Helper.getFolders(directory, deepSearch);
				File[] files = rawFiles.toArray(new File[rawFiles.size()]);
				Arrays.sort(files);

				double count = files.length;
				double side_count = count / listmax;
				if (side_count % 1 != 0) {
					side = (int) side_count + 1;
				} else {
					side = (int) side_count;
				}

				if (spaceLists) {
					p.sendMessage(" ");
				}
				if (side_number > side) {
					Helper.sendHoverMessage("", ChatColor.RED + "There are only " + side + " pages of folders in this list", "", ChatColor.GRAY + args[2], p);
					return false;
				} else if (count < 1) {
					Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Folder | Page 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
					return true;
				} else {
					Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page " + side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);

					if (count >= listmax * side_number) {
						int id = (side_number - 1) * listmax;
						for (int i = 0; i < listmax; i++) {
							if (files[id].isDirectory()) {
								String name = files[id].getName();
								String path = files[id].getAbsolutePath().replaceAll(schemFolderPath, "").replaceAll("\\\\", "/");
								if (deepSearch) {
									Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
								} else {
									Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name, ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
								}
							}
							id++;
						}
					} else {
						int id = (side_number - 1) * listmax;
						for (int i = 0; i < count - ((side_number - 1) * listmax); i++) {
							if (files[id].isDirectory()) {
								String name = files[id].getName();
								String path = files[id].getAbsolutePath().replaceAll(schemFolderPath, "").replaceAll("\\\\", "/");
								if (deepSearch) {
									Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
								} else {
									Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name, ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
								}
							}
							id++;
						}
					}
					if (side > 1) {
						if (side_number > 1) {
							if (side_number < side) {
								Helper.sendScrollMessage("//schem folder " + deep + args[2] + " " + (side_number + 1), "//schem folder " + deep + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
								return true;
							} else {
								Helper.sendScrollMessage("//schem folder " + deep + args[2] + " 1", "//schem folder " + deep + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page 1", ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
								return true;
							}
						} else {
							Helper.sendScrollMessage("//schem folder " + deep + args[2] + " " + (side_number + 1), "//schem folder " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
							return true;
						}
					} else {
						Helper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
						return true;
					}
				}
			}
		}
	}
}