package de.Zeanon.SchemManager;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Command_Folder {
	
	public static boolean onFolder(Player p, String[] args) {
		if (args.length == 2) {
			int listmax = 10;
			int side = 0;
			File directory = new File(Helper.getSchemPath());
			File[] files = directory.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.isDirectory();
				}
			});
			Arrays.sort(files);
			
			double count = files.length;
			double side_count = count / listmax;
			if (side_count % 1 != 0) {
				side = (int)side_count + 1;
			}
			if (side_count % 1 == 0) {
				side = (int)side_count;
			}
			if (count < listmax) {
				listmax = (int) count;
			}
			
			if (Helper.getSpacer()) {
				p.sendMessage(" ");
			}
			if (count < 1) {
				Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Ordner | Seite 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
				return true;
			}
			Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int)count + " Ordner | Seite 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);

			for (int i = 0; i < listmax; i++) {
				if (files[i].isDirectory()) {
					String name = files[i].getName();
					Helper.sendCommandMessage(ChatColor.RED + Integer.toString(i + 1) + ": ", ChatColor.GREEN + name, ChatColor.RED + "Öffne " + ChatColor.GREEN + name, "//schem list " + name, p);
				}
			}
			
			if (side > 1) {
				Helper.sendScrollMessage("//schem folder 2", "//schem folder " + side, ChatColor.DARK_PURPLE + "Seite 2", ChatColor.DARK_PURPLE + "Seite " + side, p, ChatColor.DARK_AQUA);
				return true;
			}
			else {
				Helper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "Es existiert nur eine Seite Ordner in dieser Liste", ChatColor.DARK_PURPLE + "Es existiert nur eine Seite Ordner in dieser Liste", p, ChatColor.BLUE);
				return true;
			}
		}
		
		
		if (args.length == 3) {
			if (StringUtils.isNumeric(args[2])) {
				int side_number = Integer.parseInt(args[2]);
				int listmax = 10;
				int side = 0;
				
				File directory = new File(Helper.getSchemPath());
				if (directory.exists()) {
					File[] files = directory.listFiles(new FileFilter() {
						@Override
						public boolean accept(File pathname) {
							return pathname.isDirectory();
						}
					});
					Arrays.sort(files);
					
					double count = files.length;
					double side_count = count / listmax;
					if (side_count % 1 != 0) {
						side = (int)side_count + 1;
					}
					if (side_count % 1 == 0) {
						side = (int)side_count;
					}
					
					if (Helper.getSpacer()) {
						p.sendMessage(" ");
					}
					if (count < 1) {
						Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Ordner | Seite 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
						return true;
					}
					if (side_number > side) {
						Helper.sendHoverMessage("", ChatColor.RED + "Es existieren nur " + side + " Seiten Ordner in dieser Liste.", "", ChatColor.GRAY + "global", p);
						return false;
					}
					Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int)count + " Ordner | Seite "+ side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
					
					if (count >= listmax * side_number) {
						int id = (side_number-1) * listmax;
						for (int i = 0; i < listmax; i++) {
							if (files[id].isDirectory()) {
								String name = files[id].getName();
								Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name, ChatColor.RED + "Öffne " + ChatColor.GREEN + name, "//schem list " + name, p);
							}
							id++;
						}
					}
					if (count < listmax * side_number) {
						int id = (side_number-1) * listmax;
						for (int i = 0; i < count - ((side_number-1) * listmax); i++) {
							if (files[id].isDirectory()) {
								String name = files[id].getName();
								Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name, ChatColor.RED + "Öffne " + ChatColor.GREEN + name, "//schem list " + name, p);
							}
							id++;
						}
					}
					
					if (side > 1) {
						if (side_number > 1) {
							if (side_number < side) {
								Helper.sendScrollMessage("//schem folder " + (side_number + 1), "//schem folder " + (side_number - 1), ChatColor.DARK_PURPLE + "Seite " + (side_number + 1), ChatColor.DARK_PURPLE + "Seite " + (side_number - 1), p, ChatColor.DARK_AQUA);
								return true;
							}
							else {
								Helper.sendScrollMessage("//schem folder 1", "//schem folder " + (side_number - 1), ChatColor.DARK_PURPLE + "Seite 1", ChatColor.DARK_PURPLE + "Seite " + (side_number - 1), p, ChatColor.DARK_AQUA);
								return true;
							}
						}
						else {
							Helper.sendScrollMessage("//schem folder " + (side_number + 1), "//schem folder " + side, ChatColor.DARK_PURPLE + "Seite " + (side_number + 1), ChatColor.DARK_PURPLE + "Seite " + side, p, ChatColor.DARK_AQUA);
							return true;
						}
					}
					else {
						Helper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "Es existiert nur eine Seite Ordner in dieser Liste", ChatColor.DARK_PURPLE + "Es existiert nur eine Seite Ordner in dieser Liste", p, ChatColor.BLUE);
						return true;
					}
				}
				return false;
			}
			
			if (!StringUtils.isNumeric(args[2])) {
				int listmax = 10;
				int side = 0;
				
				File directory = new File(Helper.getSchemPath() + args[2]);
					if (!directory.exists() || !directory.isDirectory()) {
					p.sendMessage(ChatColor.RED + args[2] + " is no folder.");
					return false;
				}
				if (directory.exists()) {
					File[] files = directory.listFiles(new FileFilter() {
						@Override
						public boolean accept(File pathname) {
							return pathname.isDirectory();
						}
					});
					Arrays.sort(files);
					
					double count = files.length;
					double side_count = count / listmax;
					if (side_count % 1 != 0) {
						side = (int)side_count + 1;
					}
					if (side_count % 1 == 0) {
						side = (int)side_count;
					}
					if (count < listmax) {
						listmax = (int) count;
					}
					
					if (Helper.getSpacer()) {
						p.sendMessage(" ");
					}
					if (count < 1) {
						Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Ordner | Seite 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
						return true;
					}
					Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int)count + " Ordner | Seite 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);

					for (int i = 0; i < listmax; i++) {
						if (files[i].isDirectory()) {
							String name = files[i].getName();
							String path = files[i].getAbsolutePath().replaceAll(Helper.getSchemPath(), "");
							Helper.sendCommandMessage(ChatColor.RED + Integer.toString(i + 1) + ": ", ChatColor.GREEN + name, ChatColor.RED + "Öffne " + ChatColor.GREEN + name, "//schem list " + path, p);
						}
					}
					if (side > 1) {
						Helper.sendScrollMessage("//schem list " + args[2] + " 2", "//schem list " + args[2] + " " + side, ChatColor.DARK_PURPLE + "Seite 2", ChatColor.DARK_PURPLE + "Seite " + side, p, ChatColor.DARK_AQUA);
						return true;
					}
					else {
						Helper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "Es existiert nur eine Seite Ordner in dieser Liste", ChatColor.DARK_PURPLE + "Es existiert nur eine Seite Ordner in dieser Liste", p, ChatColor.BLUE);
						return true;
					}
				}
			}
			return false;
		}
		
		
		if (args.length == 4) {
			if (!StringUtils.isNumeric(args[2]) && StringUtils.isNumeric(args[3])) {
				int side_number = Integer.parseInt(args[3]);
				int listmax = 10;
				int side = 0;
				
				File directory = new File(Helper.getSchemPath() + args[2]);
				if (!directory.exists() || !directory.isDirectory()) {
					p.sendMessage(ChatColor.RED + args[2] + " is no folder.");
					return false;
				}
				if (directory.exists()) {
					File[] files = directory.listFiles(new FileFilter() {
						@Override
						public boolean accept(File pathname) {
							return pathname.isDirectory();
						}
					});
					Arrays.sort(files);
					
					double count = files.length;
					double side_count = count / listmax;
					if (side_count % 1 != 0) {
						side = (int)side_count + 1;
					}
					if (side_count % 1 == 0) {
						side = (int)side_count;
					}
					
					if (Helper.getSpacer()) {
						p.sendMessage(" ");
					}
					if (count < 1) {
						Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Ordner | Seite 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
						return true;
					}
					if (side_number > side) {
						Helper.sendHoverMessage("", ChatColor.RED + "Es existieren nur " + side + " Seiten Ordner in dieser Liste.", "", ChatColor.GRAY + args[2], p);
						return false;
					}
					Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int)count + " Ordner | Seite "+ side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
					
					if (count >= listmax * side_number) {
						int id = (side_number-1) * listmax;
						for (int i = 0; i < listmax; i++) {
							if (files[id].isDirectory()) {
								String name = files[id].getName();
								String path = files[id].getAbsolutePath().replaceAll(Helper.getSchemPath(), "");
								Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name, ChatColor.RED + "Öffne " + ChatColor.GREEN + name, "//schem list " + path, p);
							}
							id++;
						}
					}
					if (count < listmax * side_number) {
						int id = (side_number-1) * listmax;
						for (int i = 0; i < count - ((side_number-1) * listmax); i++) {
							if (files[id].isDirectory()) {
								String name = files[id].getName();
								String path = files[id].getAbsolutePath().replaceAll(Helper.getSchemPath(), "");
								Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name, ChatColor.RED + "Öffne " + ChatColor.GREEN + name, "//schem list " + path, p);
							}
							id++;
						}
					}
					if (side > 1) {
						if (side_number > 1) {
							if (side_number < side) {
								Helper.sendScrollMessage("//schem folder " + args[2] + " " + (side_number + 1), "//schem folder " + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Seite " + (side_number + 1), ChatColor.DARK_PURPLE + "Seite " + (side_number - 1), p, ChatColor.DARK_AQUA);
								return true;
							}
							else {
								Helper.sendScrollMessage("//schem folder " + args[2] + " 1", "//schem folder " + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Seite 1", ChatColor.DARK_PURPLE + "Seite " + (side_number - 1), p, ChatColor.DARK_AQUA);
								return true;
							}
						}
						else {
							Helper.sendScrollMessage("//schem folder " + args[2] + " " + (side_number + 1), "//schem folder " + args[2] + " " + side, ChatColor.DARK_PURPLE + "Seite " + (side_number + 1), ChatColor.DARK_PURPLE + "Seite " + side, p, ChatColor.DARK_AQUA);
							return true;
						}
					}
					else {
						Helper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "Es existiert nur eine Seite Ordner in dieser Liste", ChatColor.DARK_PURPLE + "Es existiert nur eine Seite Ordner in dieser Liste", p, ChatColor.BLUE);
						return true;
					}
				}
			}
		}
		return false;
	}
}