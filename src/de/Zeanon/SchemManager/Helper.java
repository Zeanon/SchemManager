package de.Zeanon.SchemManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Helper {
	
	private static Plugin plugin;
	private static String schemPath;
	private static String pluginFolder;
	private static Boolean spacer = false;
	private static ArrayList<String> disableRequests = new ArrayList<String>();
	private static ArrayList<String> updateRequests = new ArrayList<String>();
	private static HashMap<String, String> deleteRequests = new HashMap<String, String>();
	private static HashMap<String, String> deleteFolderRequests = new HashMap<String, String>();
	private static HashMap<String, String> renameRequests = new HashMap<String, String>();
	private static HashMap<String, String> renameFolderRequests = new HashMap<String, String>();
	private static HashMap<String, String> overwriteRequests = new HashMap<String, String>();
	
	public static WorldEditPlugin we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
	
	
	@SuppressWarnings("static-access")
	public Helper (Plugin plugin) {
		this.plugin = plugin;
		if (Main.config.getBoolean("Space Lists")) {
			spacer = true;
		}
		String slash = null;
		if (plugin.getDataFolder().getAbsolutePath().contains("/")) {
			slash = "/";
		}
		if (plugin.getDataFolder().getAbsolutePath().contains("\\")) {
			slash = "\\\\";
		}
		if (Main.config.getString("WorldEdit Schematic-Path").equals("Default Schematic Path")) {
			String[] parts = plugin.getDataFolder().getAbsolutePath().split(slash);
			String path = parts[0] + slash;
			for (int i = 1; i < parts.length - 1; i++) {
				path = path + parts[i] + slash;
			}
			this.pluginFolder = path;
			this.schemPath = path + "WorldEdit" + slash + "schematics" + slash;
		}
		else {
			if (Main.config.getString("WorldEdit Schematic-Path").endsWith(slash)) {
				if (Main.config.getString("WorldEdit Schematic-Path").startsWith(slash)) {
					this.schemPath = Main.config.getString("WorldEdit Schematic-Path");
				}
				else {
					this.schemPath =  slash + Main.config.getString("WorldEdit Schematic-Path");
				}
			}
			else {
				if (Main.config.getString("WorldEdit Schematic-Path").startsWith(slash)) {
					this.schemPath = Main.config.getString("WorldEdit Schematic-Path") + slash;
				}
				else {
					this.schemPath =  slash + Main.config.getString("WorldEdit Schematic-Path") + slash;
				}
			}
		}
	}
	
	public static void sendCommandMessage(String message, String commandMessage, String hoverMessage, String command, Player target){
		TextComponent localMessage = new TextComponent(message);
		TextComponent commandPart = new TextComponent(commandMessage);
		commandPart.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		commandPart.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMessage).create()));
		localMessage.addExtra(commandPart);
		target.spigot().sendMessage(localMessage);
	}
	
	public static void sendBooleanMessage(String message, String commandYes, String commandNo, Player target){
		TextComponent localMessage = new TextComponent(message);
		TextComponent seperator = new TextComponent(ChatColor.BLACK + " " + ChatColor.BOLD + "| ");
		TextComponent commandPartYes = new TextComponent(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "[J]");
		TextComponent commandPartNo = new TextComponent(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[N]");
		commandPartYes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandYes));
		commandPartYes.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "[JA]").create()));
		commandPartNo.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandNo));
		commandPartNo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[NEIN]").create()));
		localMessage.addExtra(" ");
		localMessage.addExtra(commandPartYes);
		localMessage.addExtra(seperator);
		localMessage.addExtra(commandPartNo);
		target.spigot().sendMessage(localMessage);
	}
	
	public static void sendScrollMessage(String commandForward, String commandBackward, String messageForward, String messageBackward, Player target, ChatColor buttonColor) {
		TextComponent localMessage = new TextComponent(ChatColor.AQUA + "=== ");
		TextComponent commandPartBackward = new TextComponent(buttonColor + "[<<<]");
		TextComponent commandPartForward = new TextComponent(buttonColor + "[>>>]");
		commandPartBackward.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandBackward));
		commandPartBackward.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(messageBackward).create()));
		commandPartForward.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandForward));
		commandPartForward.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(messageForward).create()));
		localMessage.addExtra(commandPartBackward);
		localMessage.addExtra(ChatColor.AQUA + " " + ChatColor.BOLD + "| ");
		localMessage.addExtra(commandPartForward);
		localMessage.addExtra(ChatColor.AQUA + " ===");
		target.spigot().sendMessage(localMessage);
	}
	
	public static void sendSuggestMessage(String message, String suggestMessage, String hoverMessage, String command, Player target){
		TextComponent localMessage = new TextComponent(message);
		TextComponent suggestPart = new TextComponent(suggestMessage);
		suggestPart.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
		suggestPart.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMessage).create()));
		localMessage.addExtra(suggestPart);
		target.spigot().sendMessage(localMessage);
	}
	
	public static void sendHoverMessage(String message1, String message2, String message3, String hoverMessage, Player target) {
		TextComponent localMessage1 = new TextComponent(message1);
		TextComponent hoverPart = new TextComponent(message2);
		TextComponent localMessage2 = new TextComponent(message3);
		hoverPart.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMessage).create()));
		localMessage1.addExtra(hoverPart);
		localMessage1.addExtra(localMessage2);
		target.spigot().sendMessage(localMessage1);
	}
	
	public static void sendInvalidSubCommand(Player target, String slash) {
		TextComponent base = new TextComponent(ChatColor.RED + "Usage: ");
		TextComponent schem = new TextComponent(ChatColor.GRAY + slash + "schem");
		TextComponent load = new TextComponent(ChatColor.AQUA + "load");
		TextComponent save = new TextComponent(ChatColor.AQUA + "save");
		TextComponent delete = new TextComponent(ChatColor.AQUA + "delete");
		TextComponent deletefolder = new TextComponent(ChatColor.AQUA + "deletefolder");
		TextComponent list = new TextComponent(ChatColor.AQUA + "list");
		TextComponent folder = new TextComponent(ChatColor.AQUA + "folder");
		TextComponent search = new TextComponent(ChatColor.AQUA + "search");
		TextComponent searchfolder = new TextComponent(ChatColor.AQUA + "searchfolder");
		TextComponent update = new TextComponent(ChatColor.AQUA + "update");
		schem.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + "schem "));
		schem.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "z.B. " + ChatColor.GRAY + "" + slash + "schem").create()));
		load.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + "schem load "));
		load.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "z.B. " + ChatColor.GRAY + "" + slash + "schem " + ChatColor.AQUA + "load" + ChatColor.GOLD + " Beispiel").create()));
		list.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + "schem list "));
		list.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "z.B. " + ChatColor.GRAY + "" + slash + "schem " + ChatColor.AQUA + "list " + ChatColor.YELLOW + "[" + ChatColor.GREEN + "Bsp" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]").create()));
		delete.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + "schem delete "));
		delete.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "z.B. " + ChatColor.GRAY + "" + slash + "schem " + ChatColor.AQUA + "delete" + ChatColor.GOLD + " Beispiel").create()));
		deletefolder.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + "schem deletefolder "));
		deletefolder.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "z.B. " + ChatColor.GRAY + "" + slash + "schem " + ChatColor.AQUA + "deletefolder" + ChatColor.GREEN + " Beispiel").create()));		
		save.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + "schem save "));
		save.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "z.B. " + ChatColor.GRAY + "" + slash + "schem " + ChatColor.AQUA + "save" + ChatColor.GOLD + " Beispiel").create()));
		search.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + "schem search "));
		search.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "z.B. " + ChatColor.GRAY + "" + slash + "schem " + ChatColor.AQUA + "search " + ChatColor.YELLOW + "[" + ChatColor.GREEN + "Bsp" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]").create()));
		folder.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + "schem folder "));
		folder.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "z.B. " + ChatColor.GRAY + "" + slash + "schem " + ChatColor.AQUA + "folder " + ChatColor.YELLOW + "[" + ChatColor.GREEN + "Bsp" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]").create()));
		searchfolder.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + "schem searchfolder "));
		searchfolder.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "z.B. " + ChatColor.GRAY + "" + slash + "schem " + ChatColor.AQUA + "searchfolder " + ChatColor.YELLOW + "[" + ChatColor.GREEN + "Bsp" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]").create()));
		update.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + "schem update"));
		update.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "!!UPDATE BABY!!").create()));
		base.addExtra(schem);
		base.addExtra(ChatColor.YELLOW + " <");
		base.addExtra(load);
		base.addExtra(ChatColor.YELLOW + "|");
		base.addExtra(save);
		base.addExtra(ChatColor.YELLOW + "|");
		base.addExtra(delete);
		base.addExtra(ChatColor.YELLOW + "|");
		base.addExtra(deletefolder);
		base.addExtra(ChatColor.YELLOW + "|");		
		base.addExtra(list);
		base.addExtra(ChatColor.YELLOW + "|");
		base.addExtra(folder);
		base.addExtra(ChatColor.YELLOW + "|");
		base.addExtra(search);
		base.addExtra(ChatColor.YELLOW + "|");
		base.addExtra(searchfolder);
		base.addExtra(ChatColor.YELLOW + "|");
		base.addExtra(update);
		base.addExtra(ChatColor.YELLOW + ">");
		target.spigot().sendMessage(base);
	}
	
	public static void addDisableRequest(Player p) {
		if (!disableRequests.contains(p.getUniqueId().toString())) {
			disableRequests.add(p.getUniqueId().toString());
		}
	}
	
	public static void removeDisableRequest(Player p) {
		if (disableRequests.contains(p.getUniqueId().toString())) {
			disableRequests.remove(p.getUniqueId().toString());
		}
	}
	
	public static boolean checkDisableRequest(Player p) {
		return disableRequests.contains(p.getUniqueId().toString());
	}
	
	public static void addUpdateRequest(Player p) {
		if (!updateRequests.contains(p.getUniqueId().toString())) {
			updateRequests.add(p.getUniqueId().toString());
		}
	}
	
	public static void removeUpdateRequest(Player p) {
		if (updateRequests.contains(p.getUniqueId().toString())) {
			updateRequests.remove(p.getUniqueId().toString());
		}
	}
	
	public static boolean checkUpdateRequest(Player p) {
		return updateRequests.contains(p.getUniqueId().toString());
	}
	
	public static void addDeleteRequest(Player p, String name) {
		deleteRequests.put(p.getUniqueId().toString(), name);
	}
	
	public static void removeDeleteRequest(Player p) {
		if (deleteRequests.containsKey(p.getUniqueId().toString())) {
			deleteRequests.remove(p.getUniqueId().toString());
		}
	}
	
	public static boolean checkDeleteRequest(Player p, String name) {
		if (deleteRequests.containsKey(p.getUniqueId().toString())) {
			return deleteRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
		}
		return false;
	}
	
	public static void addDeleteFolderRequest(Player p, String name) {
		deleteFolderRequests.put(p.getUniqueId().toString(), name);
	}
	
	public static void removeDeleteFolderRequest(Player p) {
		if (deleteFolderRequests.containsKey(p.getUniqueId().toString())) {
			deleteFolderRequests.remove(p.getUniqueId().toString());
		}
	}
	
	public static boolean checkDeleteFolderRequest(Player p, String name) {
		if (deleteFolderRequests.containsKey(p.getUniqueId().toString())) {
			return deleteFolderRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
		}
		return false;
	}
	
	public static void addRenameRequest(Player p, String name) {
		renameRequests.put(p.getUniqueId().toString(), name);
	}
	
	public static void removeRenameRequest(Player p) {
		if (renameRequests.containsKey(p.getUniqueId().toString())) {
			renameRequests.remove(p.getUniqueId().toString());
		}
	}
	
	public static boolean checkRenameRequest(Player p, String name) {
		if (renameRequests.containsKey(p.getUniqueId().toString())) {
			return renameRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
		}
		return false;
	}
	
	public static void addRenameFolderRequest(Player p, String name) {
		renameFolderRequests.put(p.getUniqueId().toString(), name);
	}
	
	public static void removeRenameFolderRequest(Player p) {
		if (renameFolderRequests.containsKey(p.getUniqueId().toString())) {
			renameFolderRequests.remove(p.getUniqueId().toString());
		}
	}
	
	public static boolean checkRenameFolderRequest(Player p, String name) {
		if (renameFolderRequests.containsKey(p.getUniqueId().toString())) {
			return renameFolderRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
		}
		return false;
	}
	
	public static void addOverwriteRequest(Player p, String name) {
		overwriteRequests.put(p.getUniqueId().toString(), name);
	}
	
	public static void removeOverWriteRequest(Player p) {
		if (overwriteRequests.containsKey(p.getUniqueId().toString())) {
			overwriteRequests.remove(p.getUniqueId().toString());
		}
	}
	
	public static boolean checkOverWriteRequest(Player p, String name) {
		if (overwriteRequests.containsKey(p.getUniqueId().toString())) {
			return overwriteRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
		}
		return false;
	}
	
	public static void clearLists() {
		disableRequests.clear();
		updateRequests.clear();
		deleteRequests.clear();
		deleteFolderRequests.clear();
		overwriteRequests.clear();
	}
	
	public static ArrayList<File> getFolders(File folder, Boolean deep) {
		ArrayList<File> files = new ArrayList<File>();
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				files.add(file);
				if (deep) {
					files.addAll(getFolders(file, deep));
				}
			}
		}
		return files;
	}
	
	public static String getSchemPath() {
		return schemPath;
	}
	
	public static Boolean getSpacer() {
		return spacer;
	}
	
	@SuppressWarnings("finally")
	public static boolean update(Player p) {
		String fileName = null;
		try {
			fileName = new File(Main.class.getProtectionDomain()
					.getCodeSource()
					.getLocation()
					.toURI()
					.getPath())
				.getName();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		
		try {
			File file = new File(pluginFolder + fileName);
			BufferedInputStream inputStream = null;
			FileOutputStream outputStream = null;
			try {
				inputStream = new BufferedInputStream(new URL("https://github.com/Zeanon/SchemManager/releases/latest/download/SchemManager.jar").openStream());
				if (!file.exists()) {
					Files.copy(inputStream, Paths.get(pluginFolder + fileName), StandardCopyOption.REPLACE_EXISTING);
				}
				else {
					outputStream = new FileOutputStream(pluginFolder + fileName);
					final byte data[] = new byte[1024];
					int count;
					while ((count = inputStream.read(data, 0, 1024)) != -1) {
							outputStream.write(data, 0, count);
					}
				}
				p.sendMessage(ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " wurde geupdatet.");
				if (Main.config.getBoolean("Automatic Reload")) {
					Bukkit.getServer().reload();
				}
			} catch (IOException e) {
				e.printStackTrace();
				p.sendMessage(ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " konnte nicht geupdatet werden.");
				return false;
			}
			finally {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void disable() {
		Bukkit.getPluginManager().disablePlugin(plugin);
	}
}