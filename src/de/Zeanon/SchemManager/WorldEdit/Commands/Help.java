package de.Zeanon.SchemManager.WorldEdit.Commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.Zeanon.SchemManager.WorldEdit.Helper.Helper;
import net.md_5.bungee.api.ChatColor;

public class Help {

	public static boolean onHelp(Player p, String slash) {
		if (Helper.getBoolean("Space Lists")) {
			p.sendMessage(" ");
		}

		p.sendMessage(ChatColor.AQUA + "=== SchemManager | Version "
				+ Bukkit.getServer().getPluginManager().getPlugin("SchemManager").getDescription().getVersion()
				+ " ===");
		Helper.sendSuggestMessage(ChatColor.RED + "Get some help: ",
				ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "help", ChatColor.LIGHT_PURPLE + ""
						+ ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "OMG PLS HELP ME",
				slash + "schem help", p);
		Helper.sendSuggestMessage(ChatColor.RED + "Load a schematic: ",
				ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "load " + ChatColor.YELLOW + "<"
						+ ChatColor.GOLD + "filename" + ChatColor.YELLOW + ">",
				ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "load "
						+ ChatColor.GOLD + "example",
				slash + "schem load ", p);
		Helper.sendSuggestMessage(ChatColor.RED + "Save a schematic: ",
				ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "save " + ChatColor.YELLOW + "<"
						+ ChatColor.GOLD + "filename" + ChatColor.YELLOW + ">",
				ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "save "
						+ ChatColor.GOLD + "example",
				slash + "schem save ", p);
		Helper.sendSuggestMessage(ChatColor.RED + "Rename a schematic: ",
				ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "rename " + ChatColor.YELLOW + "<"
						+ ChatColor.GOLD + "filename" + ChatColor.YELLOW + "> <"
						+ ChatColor.GOLD + "newname" + ChatColor.YELLOW + ">",
				ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "rename "
						+ ChatColor.GOLD + "example newname",
				slash + "schem rename ", p);
		Helper.sendSuggestMessage(ChatColor.RED + "Rename a folder: ",
				ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "renamefolder " + ChatColor.YELLOW + "<"
						+ ChatColor.GREEN + "filename" + ChatColor.YELLOW + "> <"
						+ ChatColor.GREEN + "newname" + ChatColor.YELLOW + ">",
				ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "renamefolder "
						+ ChatColor.GREEN + "example newname",
				slash + "schem renamefolder ", p);
		Helper.sendSuggestMessage(ChatColor.RED + "Delete a schematic: ",
				ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "delete " + ChatColor.YELLOW + "<"
						+ ChatColor.GOLD + "filename" + ChatColor.YELLOW + ">",
				ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "delete "
						+ ChatColor.GOLD + "example",
				slash + "schem delete ", p);
		Helper.sendSuggestMessage(ChatColor.RED + "Delete a folder: ",
				ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder " + ChatColor.YELLOW + "<"
						+ ChatColor.GREEN + "filename" + ChatColor.YELLOW + ">",
				ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder "
						+ ChatColor.GREEN + "example",
				slash + "schem deletefolder ", p);
		Helper.sendSuggestMessage(ChatColor.RED + "List schematics: ",
				ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "list " + ChatColor.YELLOW + "["
						+ ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "folder"
						+ ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
				ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "list "
						+ ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] ["
						+ ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page"
						+ ChatColor.YELLOW + "]",
				slash + "schem list ", p);
		Helper.sendSuggestMessage(ChatColor.RED + "List folder: ",
				ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "folder " + ChatColor.YELLOW + "["
						+ ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "folder"
						+ ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
				ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "folder "
						+ ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] ["
						+ ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page"
						+ ChatColor.YELLOW + "]",
				slash + "schem folder ", p);
		Helper.sendSuggestMessage(ChatColor.RED + "Search for a schematic: ",
				ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "search " + ChatColor.YELLOW + "["
						+ ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "folder"
						+ ChatColor.YELLOW + "] <" + ChatColor.GOLD + "filename" + ChatColor.YELLOW + "> ["
						+ ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
				ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "search "
						+ ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] ["
						+ ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] " + ChatColor.GOLD + "example"
						+ ChatColor.YELLOW + " [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
				slash + "schem search ", p);
		Helper.sendSuggestMessage(ChatColor.RED + "Search for a folder: ",
				ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "searchfolder " + ChatColor.YELLOW + "["
						+ ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "folder"
						+ ChatColor.YELLOW + "] <" + ChatColor.GOLD + "filename" + ChatColor.YELLOW + "> ["
						+ ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
				ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "searchfolder "
						+ ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] ["
						+ ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] " + ChatColor.GOLD + "example"
						+ ChatColor.YELLOW + " [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
				slash + "schem searchfolder ", p);
		Helper.sendSuggestMessage(ChatColor.RED + "Update the plugin: ",
				ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "update", ChatColor.DARK_GREEN + ""
						+ ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "!!UPDATE BABY!!",
				slash + "schem update", p);
		Helper.sendSuggestMessage(ChatColor.RED + "Disable the plugin: ",
				ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "disable", ChatColor.DARK_RED + ""
						+ ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "PLS DON'T D;",
				slash + "schem disable", p);
		return true;
	}
}