package de.Zeanon.SchemManager;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.md_5.bungee.api.ChatColor;

public class MyListener implements Listener {
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public boolean onCommand(PlayerCommandPreprocessEvent event) {
		String message = event.getMessage().replaceAll("worldedit:", "/");
		
		if (message.toLowerCase().startsWith("/schem") || message.toLowerCase().startsWith("/schematic") || message.toLowerCase().startsWith("//schem") || message.toLowerCase().startsWith("//schematic")) {
			Player p = event.getPlayer();
			String[] args = event.getMessage().split(" ");
			String slash = null;
			if (message.toLowerCase().startsWith("/schem") || message.toLowerCase().startsWith("/schematic")) {
				slash = "/";
			}
			else {
				slash = "//";
			}
			
			if (args.length == 1) {
				event.setCancelled(true);
				if (Helper.getSpacer()) {
					p.sendMessage(" ");
				}
				Helper.sendSuggestMessage(ChatColor.RED + "Lade eine Schematic: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "load " + ChatColor.YELLOW + "<" + ChatColor.GOLD + "name" + ChatColor.YELLOW + ">", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "load " + ChatColor.GOLD + "Beispiel", slash + "schem load ", p);
				Helper.sendSuggestMessage(ChatColor.RED + "Speichere eine Schematic: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "save " + ChatColor.YELLOW + "<" + ChatColor.GOLD + "name" + ChatColor.YELLOW + ">", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "save " + ChatColor.GOLD + "Beispiel", slash + "schem save ", p);
				Helper.sendSuggestMessage(ChatColor.RED + "L�sche eine Schematic: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "delete " + ChatColor.YELLOW + "<" + ChatColor.GOLD + "name" + ChatColor.YELLOW + ">", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "delete " + ChatColor.GOLD + "Beispiel", slash + "schem delete ", p);
				Helper.sendSuggestMessage(ChatColor.RED + "L�sche einen Ordner: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder " + ChatColor.YELLOW + "<" + ChatColor.GREEN + "name" + ChatColor.YELLOW + ">", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder " + ChatColor.GREEN + "Beispiel", slash + "schem deletefolder ", p);				
				Helper.sendSuggestMessage(ChatColor.RED + "Liste Schematics: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "list " + ChatColor.YELLOW + "[" + ChatColor.GREEN + "Ordner" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "list " + ChatColor.YELLOW + "[" + ChatColor.GREEN + "Bsp" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", slash + "schem list ", p);
				Helper.sendSuggestMessage(ChatColor.RED + "Liste Ordner: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "folder " + ChatColor.YELLOW + "[" + ChatColor.GREEN + "Ordner" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "folder " + ChatColor.YELLOW + "[" + ChatColor.GREEN + "Bsp" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", slash + "schem folder ", p);
				Helper.sendSuggestMessage(ChatColor.RED + "Suche eine Schematic: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "search " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "Ordner" + ChatColor.YELLOW + "] <" + ChatColor.GOLD + "name" + ChatColor.YELLOW + "> [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "search " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "Bsp" + ChatColor.YELLOW + "] " + ChatColor.GOLD + "Beispiel" + ChatColor.YELLOW + " [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", slash + "schem search ", p);
				Helper.sendSuggestMessage(ChatColor.RED + "Suche einen Ordner: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "searchfolder " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "Ordner" + ChatColor.YELLOW + "] <" + ChatColor.GOLD + "name" + ChatColor.YELLOW + "> [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "searchfolder " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "Bsp" + ChatColor.YELLOW + "] " + ChatColor.GOLD + "Beispiel" + ChatColor.YELLOW + " [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", slash + "schem searchfolder ", p);
				Helper.sendSuggestMessage(ChatColor.RED + "Update das Plugin: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "update", ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "!!UPDATE BABY!!", slash + "schem update", p);
				p.sendMessage("HI 7");
				return true;
			}
			
			if (args.length > 1 && !args[1].equalsIgnoreCase("load")
					 && !args[1].equalsIgnoreCase("save")
					 && !args[1].equalsIgnoreCase("delete")
					 && !args[1].equalsIgnoreCase("del")
					 && !args[1].equalsIgnoreCase("deletefolder")
					 && !args[1].equalsIgnoreCase("delfolder")
					 && !args[1].equalsIgnoreCase("rename")
					 && !args[1].equalsIgnoreCase("list")
					 && !args[1].equalsIgnoreCase("folder")
					 && !args[1].equalsIgnoreCase("search")
					 && !args[1].equalsIgnoreCase("searchfolder")
					 && !args[1].equalsIgnoreCase("update")
					 && !args[1].equalsIgnoreCase("disable")) {
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "Invalid sub-command \'" + ChatColor.GOLD + "" + args[1] + ChatColor.RED + "\'. Options: " + ChatColor.GOLD + "list" + ChatColor.RED + ", " + ChatColor.GOLD + "load" + ChatColor.RED + ", " + ChatColor.GOLD + "delete" + ChatColor.RED + ", " + ChatColor.GOLD + "deletefolder" + ChatColor.RED + ", " + ChatColor.GOLD + "save");
				Helper.sendInvalidSubCommand(p, slash);
			}
			
			if (args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("del")) {
				event.setCancelled(true);
				if (args.length <= 4) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD + "filename" + ChatColor.YELLOW + ">");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "delete " + ChatColor.YELLOW + "<" + ChatColor.GOLD + "name" + ChatColor.YELLOW + ">", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "delete " + ChatColor.GOLD + "Beispiel", slash + "schem delete ", p);
						return true;
					}
					if (args.length == 4 && !Helper.checkDeleteRequest(p, args[2]) && !args[3].equalsIgnoreCase("confirm") && !args[3].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "delete " + ChatColor.YELLOW + "<" + ChatColor.GOLD + "name" + ChatColor.YELLOW + ">", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "delete " + ChatColor.GOLD + "Beispiel", slash + "schem delete ", p);
						return true;
					}
					Command_Delete.onDelete(p, args);
					return true;
				}
				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "delete " + ChatColor.YELLOW + "<" + ChatColor.GOLD + "name" + ChatColor.YELLOW + ">", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "delete " + ChatColor.GOLD + "Beispiel", slash + "schem delete ", p);
					return true;
				}
			}
			
			if (args[1].equalsIgnoreCase("deletefolder") || args[1].equalsIgnoreCase("delfolder")) {
				event.setCancelled(true);
				if (args.length <= 4) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GREEN + "filename" + ChatColor.YELLOW + ">");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder " + ChatColor.YELLOW + "<" + ChatColor.GREEN + "name" + ChatColor.YELLOW + ">", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder " + ChatColor.GREEN + "Beispiel", slash + "schem deletefolder ", p);
						return true;
					}
					if (args.length == 4 && !Helper.checkDeleteFolderRequest(p, args[2]) && !args[3].equalsIgnoreCase("confirm") && !args[3].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder " + ChatColor.YELLOW + "<" + ChatColor.GREEN + "name" + ChatColor.YELLOW + ">", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder " + ChatColor.GREEN + "Beispiel", slash + "schem deletefolder ", p);
						return true;
					}
					Command_DeleteFolder.onDeleteFolder(p, args);
					return true;
				}
				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder " + ChatColor.YELLOW + "<" + ChatColor.GREEN + "name" + ChatColor.YELLOW + ">", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder " + ChatColor.GREEN + "Beispiel", slash + "schem deletefolder ", p);
					return true;
				}
			}
			
			if (args[1].equalsIgnoreCase("rename")) {
				event.setCancelled(true);
				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GREEN + "filename" + ChatColor.YELLOW + ">");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder " + ChatColor.YELLOW + "<" + ChatColor.GREEN + "name" + ChatColor.YELLOW + ">", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder " + ChatColor.GREEN + "Beispiel", slash + "schem deletefolder ", p);
						return true;
					}
					if (args.length == 5 && !Helper.checkRenameRequest(p, args[2]) && !args[3].equalsIgnoreCase("confirm") && !args[3].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder " + ChatColor.YELLOW + "<" + ChatColor.GREEN + "name" + ChatColor.YELLOW + ">", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder " + ChatColor.GREEN + "Beispiel", slash + "schem deletefolder ", p);
						return true;
					}
					Command_Rename.onRename(p, args);
					return true;
				}
				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder " + ChatColor.YELLOW + "<" + ChatColor.GREEN + "name" + ChatColor.YELLOW + ">", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder " + ChatColor.GREEN + "Beispiel", slash + "schem deletefolder ", p);
					return true;
				}
			}
			
			if (args[1].equalsIgnoreCase("load")) {
				if (args.length < 3) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD + "filename" + ChatColor.YELLOW + ">");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "load " + ChatColor.YELLOW + "<" + ChatColor.GOLD + "name" + ChatColor.YELLOW + ">", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "load " + ChatColor.GOLD + "Beispiel", slash + "schem load ", p);
					return true;
				}
				if (args.length > 3) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "load " + ChatColor.YELLOW + "<" + ChatColor.GOLD + "name" + ChatColor.YELLOW + ">", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "load " + ChatColor.GOLD + "Beispiel", slash + "schem load ", p);
					return true;
				}
			}
			
			if (args[1].equalsIgnoreCase("save")) {
				if (args.length < 3) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD + "filename" + ChatColor.YELLOW + ">");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "save " + ChatColor.YELLOW + "<" + ChatColor.GOLD + "name" + ChatColor.YELLOW + ">", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "save " + ChatColor.GOLD + "Beispiel", slash + "schem save ", p);
					return true;
				}
				
				if (args.length > 5 || (Main.config.getBoolean("Save Function Override") && (args.length > 4 || (args.length == 4 && !Helper.checkOverWriteRequest(p, args[2]) && !args[3].equalsIgnoreCase("confirm") && !args[3].equalsIgnoreCase("deny"))))) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "save " + ChatColor.YELLOW + "<" + ChatColor.GOLD + "name" + ChatColor.YELLOW + ">", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "save " + ChatColor.GOLD + "Beispiel", slash + "schem save ", p);
					return true;
				}
				
				if (Main.config.getBoolean("Save Function Override")  && !args[2].equals("-f")) {
					event.setCancelled(true);
					if (args.length == 3 || (args.length == 4 && (args[3].equalsIgnoreCase("confirm") || args[3].equalsIgnoreCase("deny")))) {
						Command_Save.onSave(p, args);
						return true;
					}
					else {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "save " + ChatColor.YELLOW + "<" + ChatColor.GOLD + "name" + ChatColor.YELLOW + ">", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "save " + ChatColor.GOLD + "Beispiel", slash + "schem save ", p);
						return true;
					}
				}
			}
			
			if (args[1].equalsIgnoreCase("list")) {
				event.setCancelled(true);
				if (args.length <= 4) {
					if (args.length == 4 && !StringUtils.isNumeric(args[3])) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "list " + ChatColor.YELLOW + "[" + ChatColor.GREEN + "Ordner" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "list " + ChatColor.YELLOW + "[" + ChatColor.GREEN + "Bsp" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", slash + "schem list ", p);
						return true;
					}
					Command_List.onList(p, args);
					return true;
				}
				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "list " + ChatColor.YELLOW + "[" + ChatColor.GREEN + "Ordner" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "list " + ChatColor.YELLOW + "[" + ChatColor.GREEN + "Bsp" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", slash + "schem list ", p);
					return true;
				}
			}
			
			if (args[1].equalsIgnoreCase("folder")) {
				event.setCancelled(true);
				if (args.length <= 4) {
					if (args.length == 4 && !StringUtils.isNumeric(args[3])) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "folder " + ChatColor.YELLOW + "[" + ChatColor.GREEN + "Ordner" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "folder " + ChatColor.YELLOW + "[" + ChatColor.GREEN + "Bsp" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", slash + "schem folder ", p);
						return true;
					}
					Command_Folder.onFolder(p, args);
					return true;
				}
				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "folder " + ChatColor.YELLOW + "[" + ChatColor.GREEN + "Ordner" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "folder " + ChatColor.YELLOW + "[" + ChatColor.GREEN + "Bsp" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", slash + "schem folder ", p);
					return true;
				}
			}
			
			if (args[1].equalsIgnoreCase("search")) {
				event.setCancelled(true);
				Boolean deep = false;
				if (args[2].equalsIgnoreCase("-deep")) {
					deep = true;
					args = (String[]) ArrayUtils.removeElement(args, "-deep");
				}
				if (args[2].equalsIgnoreCase("-d")) {
					deep = true;
					args = (String[]) ArrayUtils.removeElement(args, "-d");
				}
				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD + "name" + ChatColor.YELLOW + ">");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "search " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "Ordner" + ChatColor.YELLOW + "] <" + ChatColor.GOLD + "name" + ChatColor.YELLOW + "> [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "search " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "Bsp" + ChatColor.YELLOW + "] " + ChatColor.GOLD + "Beispiel" + ChatColor.YELLOW + " [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", slash + "schem search ", p);
						return true;
					}
					if (args.length == 5 && !StringUtils.isNumeric(args[4])) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "search " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "Ordner" + ChatColor.YELLOW + "] <" + ChatColor.GOLD + "name" + ChatColor.YELLOW + "> [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "search " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "Bsp" + ChatColor.YELLOW + "] " + ChatColor.GOLD + "Beispiel" + ChatColor.YELLOW + " [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", slash + "schem search ", p);
						return true;
					}
					Command_Search.onSearch(p, args, deep);
					return true;
				}
				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "search " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "Ordner" + ChatColor.YELLOW + "] <" + ChatColor.GOLD + "name" + ChatColor.YELLOW + "> [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "search " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "Bsp" + ChatColor.YELLOW + "] " + ChatColor.GOLD + "Beispiel" + ChatColor.YELLOW + " [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", slash + "schem search ", p);
					return true;
				}
			}
			
			if (args[1].equalsIgnoreCase("searchfolder")) {
				event.setCancelled(true);
				Boolean deep = false;
				if (args[2].equalsIgnoreCase("-deep")) {
					deep = true;
					args = (String[]) ArrayUtils.removeElement(args, "-deep");
				}
				if (args[2].equalsIgnoreCase("-d")) {
					deep = true;
					args = (String[]) ArrayUtils.removeElement(args, "-d");
				}
				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD + "name" + ChatColor.YELLOW + ">");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "searchfolder " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "Ordner" + ChatColor.YELLOW + "] <" + ChatColor.GOLD + "name" + ChatColor.YELLOW + "> [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "searchfolder " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "Bsp" + ChatColor.YELLOW + "] " + ChatColor.GOLD + "Beispiel" + ChatColor.YELLOW + " [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", slash + "schem searchfolder ", p);
						return true;
					}
					if (args.length == 5 && !StringUtils.isNumeric(args[4])) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "searchfolder " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "Ordner" + ChatColor.YELLOW + "] <" + ChatColor.GOLD + "name" + ChatColor.YELLOW + "> [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "searchfolder " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "Bsp" + ChatColor.YELLOW + "] " + ChatColor.GOLD + "Beispiel" + ChatColor.YELLOW + " [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", slash + "schem searchfolder ", p);
						return true;
					}
					Command_SearchFolder.onSearchFolder(p, args, deep);
					return true;
				}
				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "searchfolder " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "Ordner" + ChatColor.YELLOW + "] <" + ChatColor.GOLD + "name" + ChatColor.YELLOW + "> [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", ChatColor.RED + "z.B. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "searchfolder " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "Bsp" + ChatColor.YELLOW + "] " + ChatColor.GOLD + "Beispiel" + ChatColor.YELLOW + " [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]", slash + "schem searchfolder ", p);
					return true;
				}
			}
			
			if (args[1].equalsIgnoreCase("reload") || args[1].equalsIgnoreCase("rl")) {
				event.setCancelled(true);
				if (args.length == 2) {
					Main.config.reload();
					Helper.clearLists();
					p.sendMessage(ChatColor.RED + "Die config wurde reloaded.");
					return true;
				}
				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "reload", ChatColor.RED + "Oder " + ChatColor.AQUA + slash + "schem rl", slash + "schem reload", p);
					return true;
				}
			}
			
			if (args[1].equalsIgnoreCase("update")) {
				event.setCancelled(true);
				if (args.length == 2) {
					Helper.sendBooleanMessage(ChatColor.RED + "Willst du wirklich updaten?", "//schem update confirm", "//schem update deny", p);
					Helper.addUpdateRequest(p);
					return true;
				}
				if (args.length == 3 && (args[2].equalsIgnoreCase("confirm") || args[2].equalsIgnoreCase("deny"))) {
					if (args[2].equalsIgnoreCase("confirm") && Helper.checkUpdateRequest(p)) {
						return Helper.update(p);
					}
					if (args[2].equalsIgnoreCase("deny") && Helper.checkUpdateRequest(p)) {
						Helper.removeUpdateRequest(p);
						p.sendMessage(ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " wird nicht geupdatet.");
						return true;
					}
					return true;
				}
				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "update", ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "!!UPDATE BABY!!", slash + "schem update", p);
					return true;
				}
			}
			
			if (args[1].equalsIgnoreCase("disable")) {
				event.setCancelled(true);
				if (args.length == 2) {
					Helper.sendBooleanMessage(ChatColor.RED + "Willst du " + ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " wirklich deaktivieren? ", "//schem disable confirm", "//schem disable deny", p);
					Helper.addDisableRequest(p);
					return true;
				}
				if (args.length == 3 && (args[2].equalsIgnoreCase("confirm") || args[2].equalsIgnoreCase("deny"))) {
					if (args[2].equalsIgnoreCase("confirm") && Helper.checkDisableRequest(p)) {
						p.sendMessage(ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " wird deaktiviert.");
						Helper.disable();
						return true;
					}
					if (args[2].equalsIgnoreCase("deny") && Helper.checkDisableRequest(p)) {
						Helper.removeDisableRequest(p);
						p.sendMessage(ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " wird nicht deaktiviert.");
						return true;
					}
					return true;
				}
				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ", ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "disable", ChatColor.RED + "" + ChatColor.BOLD + "ACHTUNG", slash + "schem disable", p);
					return true;
				}
			}
		}
		
		if (message.toLowerCase().startsWith("/stoplag")) {
			Player p = event.getPlayer();
			String[] args = event.getMessage().split(" ");
			if (args.length == 1) {
				event.setCancelled(true);
				p.performCommand("stoplag confirm");
				return true;
			}
			return true;
		}
		return false;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		Helper.removeDisableRequest(p);
		Helper.removeUpdateRequest(p);
		Helper.removeDeleteRequest(p);
		Helper.removeDeleteFolderRequest(p);
		Helper.removeOverWriteRequest(p);
	}
}