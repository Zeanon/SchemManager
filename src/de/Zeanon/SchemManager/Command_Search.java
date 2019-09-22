package de.Zeanon.SchemManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Command_Search {

	public static boolean onSearch(Player p, String[] args, Boolean deepSearch) {
		String deep = "";
		if (deepSearch) {
			deep = "-deep ";
		}
		
		if (args.length == 3) {
			int listmax = 10;
			int side = 0;
			
			File directory = new File(Helper.getSchemPath());
			String[] extension = {"schematic", "schem"};
			Collection<File> rawFiles = FileUtils.listFiles(directory, extension, deepSearch);
			ArrayList<File> fileArray = new ArrayList<File>();
			for (File file : rawFiles) {
				String named = file.getName().toLowerCase().replaceAll("\\.schematic", "").replaceAll("\\.schem", "");
				String name = file.getName().toLowerCase();
				if (named.contains(args[2].toLowerCase()) && (name.endsWith(".schematic") || name.endsWith(".schem")) && file.isFile()) {
					fileArray.add(file);
				}
			}
			File[] files = fileArray.toArray(new File[fileArray.size()]);
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
				Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Schematics | Seite 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
				return true;
			}
			Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int)count + " Schematics | Seite 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
			
			for (int i = 0; i < listmax; i++) {
				if (!files[i].isDirectory()) {
					String name = files[i].getName().replaceAll("\\.schematic", "").replaceAll("\\.schem", "");
					String path = files[i].getAbsolutePath().replaceAll(Helper.getSchemPath(), "").replaceAll("\\.schematic", "").replaceAll("\\.schem", "");
					Helper.sendCommandMessage(ChatColor.RED + Integer.toString(i + 1) + ": ", ChatColor.GOLD + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Lade " + ChatColor.GOLD + path + ChatColor.RED + " in dein Clipboard", "//schem load " + path, p);
				}
			}
			
			if (side > 1) {
				Helper.sendScrollMessage("//schem search " + deep + args[2] + " 2", "//schem search " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Seite 2", ChatColor.DARK_PURPLE + "Seite " + side, p, ChatColor.DARK_AQUA);
				return true;
			}
			else {
				Helper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "Es existiert nur eine Seite Schematics in dieser Liste", ChatColor.DARK_PURPLE + "Es existiert nur eine Seite Schematics in dieser Liste", p, ChatColor.BLUE);
				return true;
			}
		}
		
		
		if (args.length == 4) {
			if (StringUtils.isNumeric(args[3])) {
				int listmax = 10;
				int side = 0;
				int side_number = Integer.parseInt(args[3]);
				
				File directory = new File(Helper.getSchemPath());
				String[] extension = {"schematic", "schem"};
				Collection<File> rawFiles = FileUtils.listFiles(directory, extension, deepSearch);
				ArrayList<File> fileArray = new ArrayList<File>();
				for (File file : rawFiles) {
					String named = file.getName().toLowerCase().replaceAll("\\.schematic", "").replaceAll("\\.schem", "");
					String name = file.getName().toLowerCase();
					if (named.contains(args[2].toLowerCase()) && (name.endsWith(".schematic") || name.endsWith(".schem")) && file.isFile()) {
						fileArray.add(file);
					}
				}
				File[] files = fileArray.toArray(new File[fileArray.size()]);
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
				if (side_number > side) {
					Helper.sendHoverMessage("", ChatColor.RED + "Es existieren nur " + side + " Seiten Schematics in dieser Liste.", "", ChatColor.GRAY + "global", p);
					return false;
				}
				if (count < 1) {
					Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Schematics | Seite 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
					return true;
				}
				Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int)count + " Schematics | Seite "+ side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
				
				if (count >= listmax * side_number) {
					int id = (side_number-1) * listmax;
					for (int i = 0; i < listmax; i++) {
						if (!files[id].isDirectory()) {
							String name = files[id].getName().replaceAll("\\.schematic", "").replaceAll("\\.schem", "");
							String path = files[id].getAbsolutePath().replaceAll(Helper.getSchemPath(), "").replaceAll("\\.schematic", "").replaceAll("\\.schem", "");
							Helper.sendCommandMessage(ChatColor.RED + Integer.toString(i + 1) + ": ", ChatColor.GOLD + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Lade " + ChatColor.GOLD + path + ChatColor.RED + " in dein Clipboard", "//schem load " + path, p);
						}
						id++;
					}
				}
				if (count < listmax * side_number) {
					int id = (side_number-1) * listmax;
					for (int i = 0; i < count - ((side_number-1) * listmax); i++) {
						if (!files[id].isDirectory()) {
							String name = files[id].getName().replaceAll("\\.schematic", "").replaceAll("\\.schem", "");
							String path = files[id].getAbsolutePath().replaceAll(Helper.getSchemPath(), "").replaceAll("\\.schematic", "").replaceAll("\\.schem", "");
							Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GOLD + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Lade " + ChatColor.GOLD + path + ChatColor.RED + " in dein Clipboard", "//schem load " + path, p);
						}
						id++;
					}
				}
				
				if (side > 1) {
					if (side_number > 1) {
						if (side_number < side) {
							Helper.sendScrollMessage("//schem search " + deep + args[2] + " " + (side_number + 1), "//schem search " + deep + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Seite " + (side_number + 1), ChatColor.DARK_PURPLE + "Seite " + (side_number - 1), p, ChatColor.DARK_AQUA);
							return true;
						}
						else {
							Helper.sendScrollMessage("//schem search " + deep + args[2] + " 1", "//schem search " + deep + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Seite 1", ChatColor.DARK_PURPLE + "Seite " + (side_number - 1), p, ChatColor.DARK_AQUA);
							return true;
						}
					}
					else {
						Helper.sendScrollMessage("//schem search " + deep + args[2] + " " + (side_number + 1), "//schem search " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Seite " + (side_number + 1), ChatColor.DARK_PURPLE + "Seite " + side, p, ChatColor.DARK_AQUA);
						return true;
					}
				}
				else {
					Helper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "Es existiert nur eine Seite Schematics in dieser Liste", ChatColor.DARK_PURPLE + "Es existiert nur eine Seite Schematics in dieser Liste", p, ChatColor.BLUE);
					return true;
				}	
			}
			
			if (!StringUtils.isNumeric(args[3])) {
				int listmax = 10;
				int side = 0;

				File directory = new File(Helper.getSchemPath() + args[2]);
				if (!directory.exists() || !directory.isDirectory()) {
					p.sendMessage(ChatColor.RED + args[2] + " is no folder.");
					return false;
				}
				
				if (directory.exists()) {
					String[] extension = {"schematic", "schem"};
					Collection<File> rawFiles = FileUtils.listFiles(directory, extension, deepSearch);
					ArrayList<File> fileArray = new ArrayList<File>();
					for (File file : rawFiles) {
						String named = file.getName().toLowerCase().replaceAll("\\.schematic", "").replaceAll("\\.schem", "");
						String name = file.getName().toLowerCase();
						if (named.contains(args[2].toLowerCase()) && (name.endsWith(".schematic") || name.endsWith(".schem")) && file.isFile()) {
							fileArray.add(file);
						}
					}
					File[] files = fileArray.toArray(new File[fileArray.size()]);
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
						Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Schematics | Seite 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
						return true;
					}
					Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int)count + " Schematics | Seite 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
					
					for (int i = 0; i < listmax; i++) {
						if (!files[i].isDirectory()) {
							String name = files[i].getName().replaceAll("\\.schematic", "").replaceAll("\\.schem", "");
							String path = files[i].getAbsolutePath().replaceAll(Helper.getSchemPath(), "").replaceAll("\\.schematic", "").replaceAll("\\.schem", "");
							Helper.sendCommandMessage(ChatColor.RED + Integer.toString(i + 1) + ": ", ChatColor.GOLD + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Lade " + ChatColor.GOLD + path + ChatColor.RED + " in dein Clipboard", "//schem load " + path, p);
						}
					}
					
					if (side > 1) {
						Helper.sendScrollMessage("//schem search " + deep + args[3] + " 2", "//schem search " + deep + args[3] + " " + side, ChatColor.DARK_PURPLE + "Seite 2", ChatColor.DARK_PURPLE + "Seite " + side, p, ChatColor.DARK_AQUA);
						return true;
					}
					else {
						Helper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "Es existiert nur eine Seite Schematics in dieser Liste", ChatColor.DARK_PURPLE + "Es existiert nur eine Seite Schematics in dieser Liste", p, ChatColor.BLUE);
						return true;
					}
				}
			}
			return false;
		}
		
		
		if (args.length == 5) {
			if (!StringUtils.isNumeric(args[3]) && StringUtils.isNumeric(args[4])) {
				int listmax = 10;
				int side = 0;
				int side_number = Integer.parseInt(args[4]);
				File directory = new File(Helper.getSchemPath() + args[2]);
				if (!directory.exists() || !directory.isDirectory()) {
					p.sendMessage(ChatColor.RED + args[2] + " is no folder.");
					return false;
				}
				
				if (directory.exists()) {
					String[] extension = {"schematic", "schem"};
					Collection<File> rawFiles = FileUtils.listFiles(directory, extension, deepSearch);
					ArrayList<File> fileArray = new ArrayList<File>();
					for (File file : rawFiles) {
						String named = file.getName().toLowerCase().replaceAll("\\.schematic", "").replaceAll("\\.schem", "");
						String name = file.getName().toLowerCase();
						if (named.contains(args[2].toLowerCase()) && (name.endsWith(".schematic") || name.endsWith(".schem")) && file.isFile()) {
							fileArray.add(file);
						}
					}
					File[] files = fileArray.toArray(new File[fileArray.size()]);
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
					if (side_number > side) {
						p.sendMessage(ChatColor.RED + "Es existieren nur " + side + " Seiten Schematics in dieser Liste.");
						return false;
					}
					if (count < 1) {
						Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Schematics | Seite 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
						return true;
					}
					Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int)count + " Schematics | Seite "+ side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
					
					if (count >= listmax * side_number) {
						int id = (side_number-1) * listmax;
						for (int i = 0; i < listmax; i++) {
							if (!files[id].isDirectory()) {
								String name = files[id].getName().replaceAll("\\.schematic", "").replaceAll("\\.schem", "");
								String path = files[id].getAbsolutePath().replaceAll(Helper.getSchemPath(), "").replaceAll("\\.schematic", "").replaceAll("\\.schem", "");
								Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GOLD + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Lade " + ChatColor.GOLD + path + ChatColor.RED + " in dein Clipboard", "//schem load " + path, p);
							}
							id++;
						}
					}
					if (count < listmax * side_number) {
						int id = (side_number-1) * listmax;
						for (int i = 0; i < count - ((side_number-1) * listmax); i++) {
							if (!files[id].isDirectory()) {
								String name = files[id].getName().replaceAll("\\.schematic", "").replaceAll("\\.schem", "");
								String path = files[id].getAbsolutePath().replaceAll(Helper.getSchemPath(), "").replaceAll("\\.schematic", "").replaceAll("\\.schem", "");
								Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GOLD + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Lade " + ChatColor.GOLD + path + ChatColor.RED + " in dein Clipboard", "//schem load " + path, p);
							}
							id++;
						}
					}
					
					if (side > 1) {
						if (side_number > 1) {
							if (side_number < side) {
								Helper.sendScrollMessage("//schem search " + deep + args[3] + " " + (side_number + 1), "//schem search " + deep + args[3] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Seite " + (side_number + 1), ChatColor.DARK_PURPLE + "Seite " + (side_number - 1), p, ChatColor.DARK_AQUA);
								return true;
							}
							else {
								Helper.sendScrollMessage("//schem search " + deep + args[3] + " 1", "//schem search " + deep + args[3] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Seite 1", ChatColor.DARK_PURPLE + "Seite " + (side_number - 1), p, ChatColor.DARK_AQUA);
								return true;
							}
						}
						else {
							Helper.sendScrollMessage("//schem search " + deep + args[3] + " " + (side_number + 1), "//schem search " + deep + args[3] + " " + side, ChatColor.DARK_PURPLE + "Seite " + (side_number + 1), ChatColor.DARK_PURPLE + "Seite " + side, p, ChatColor.DARK_AQUA);
							return true;
						}
					}
					else {
						Helper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "Es existiert nur eine Seite Schematics in dieser Liste", ChatColor.DARK_PURPLE + "Es existiert nur eine Seite Schematics in dieser Liste", p, ChatColor.BLUE);
						return true;
					}
				}
			}
		}
		return false;
	}
}