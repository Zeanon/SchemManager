package de.zeanon.schemmanager.worldeditversion.commands;

import de.zeanon.schemmanager.worldeditversion.WorldEditVersionMain;
import de.zeanon.schemmanager.worldeditversion.helper.Helper;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class Help {

	public static boolean onHelp(Player p, String slash, String schemAlias) {
		if (Helper.getBoolean("Space Lists")) {
			p.sendMessage(" ");
		}

		p.sendMessage(ChatColor.AQUA + "=== SchemManager | Version "
				+ WorldEditVersionMain.plugin.getDescription().getVersion()
				+ " ===");
		Helper.sendSuggestMessage(ChatColor.RED + "Get some help: ",
				ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "help", ChatColor.LIGHT_PURPLE + ""
						+ ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "OMG PLS HELP ME",
				slash + schemAlias + " help", p);
		Helper.sendSuggestMessage(ChatColor.RED + "Load a schematic: ",
				ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "load " + ChatColor.YELLOW + "<"
						+ ChatColor.GOLD + "filename" + ChatColor.YELLOW + "> ["
						+ ChatColor.DARK_PURPLE + "format" + ChatColor.YELLOW + "]",
				ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "load "
						+ ChatColor.GOLD + "example " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "format" + ChatColor.YELLOW
						+ "]",
				slash + schemAlias + " load ", p);
		Helper.sendSuggestMessage(ChatColor.RED + "Show Available formats: ",
				ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "formats", ChatColor.BLUE + ""
						+ ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "There are different formats? :O",
				slash + schemAlias + " formats", p);
		Helper.sendSuggestMessage(ChatColor.RED + "Save a schematic: ",
				ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "save " + ChatColor.YELLOW + "<"
						+ ChatColor.GOLD + "filename" + ChatColor.YELLOW + ">",
				ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "save "
						+ ChatColor.GOLD + "example",
				slash + schemAlias + " save ", p);
		Helper.sendSuggestMessage(ChatColor.RED + "Rename a schematic: ",
				ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "rename " + ChatColor.YELLOW + "<"
						+ ChatColor.GOLD + "filename" + ChatColor.YELLOW + "> <"
						+ ChatColor.GOLD + "newname" + ChatColor.YELLOW + ">",
				ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "rename "
						+ ChatColor.GOLD + "example newname",
				slash + schemAlias + " rename ", p);
		Helper.sendSuggestMessage(ChatColor.RED + "Rename a folder: ",
				ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "renamefolder " + ChatColor.YELLOW + "<"
						+ ChatColor.GREEN + "filename" + ChatColor.YELLOW + "> <"
						+ ChatColor.GREEN + "newname" + ChatColor.YELLOW + ">",
				ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "renamefolder "
						+ ChatColor.GREEN + "example newname",
				slash + schemAlias + " renamefolder ", p);
		Helper.sendSuggestMessage(ChatColor.RED + "Delete a schematic: ",
				ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "delete " + ChatColor.YELLOW + "<"
						+ ChatColor.GOLD + "filename" + ChatColor.YELLOW + ">",
				ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "delete "
						+ ChatColor.GOLD + "example",
				slash + schemAlias + " delete ", p);
		Helper.sendSuggestMessage(ChatColor.RED + "Delete a folder: ",
				ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "deletefolder " + ChatColor.YELLOW + "<"
						+ ChatColor.GREEN + "filename" + ChatColor.YELLOW + ">",
				ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "deletefolder "
						+ ChatColor.GREEN + "example",
				slash + schemAlias + " deletefolder ", p);
		Helper.sendSuggestMessage(ChatColor.RED + "List schematics: ",
				ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "list " + ChatColor.YELLOW + "["
						+ ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "folder"
						+ ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
				ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "list "
						+ ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] ["
						+ ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page"
						+ ChatColor.YELLOW + "]",
				slash + schemAlias + " list ", p);
		Helper.sendSuggestMessage(ChatColor.RED + "List folder: ",
				ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "folder " + ChatColor.YELLOW + "["
						+ ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "folder"
						+ ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
				ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "folder "
						+ ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] ["
						+ ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page"
						+ ChatColor.YELLOW + "]",
				slash + schemAlias + " folder ", p);
		Helper.sendSuggestMessage(ChatColor.RED + "Search for a schematic: ",
				ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "search " + ChatColor.YELLOW + "["
						+ ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "folder"
						+ ChatColor.YELLOW + "] <" + ChatColor.GOLD + "filename" + ChatColor.YELLOW + "> ["
						+ ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
				ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "search "
						+ ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] ["
						+ ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] " + ChatColor.GOLD + "example"
						+ ChatColor.YELLOW + " [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
				slash + schemAlias + " search ", p);
		Helper.sendSuggestMessage(ChatColor.RED + "Search for a folder: ",
				ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "searchfolder " + ChatColor.YELLOW + "["
						+ ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "folder"
						+ ChatColor.YELLOW + "] <" + ChatColor.GOLD + "filename" + ChatColor.YELLOW + "> ["
						+ ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
				ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + " " + ChatColor.AQUA + "searchfolder "
						+ ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] ["
						+ ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] " + ChatColor.GOLD + "example"
						+ ChatColor.YELLOW + " [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
				slash + schemAlias + " searchfolder ", p);
		return true;
	}

	public static boolean onFormats(Player p, boolean suppressBlankLine) {
		if (Helper.getBoolean("Space Lists") && !suppressBlankLine) {
			p.sendMessage(" ");
		}
		p.sendMessage(ChatColor.RED + "Available formats:");
		if (Helper.getStringList("File Extensions").size() > 0) {
			String[] formats = Helper.getStringList("File Extensions").toArray(new String[0]);
			StringBuilder pathBuilder = new StringBuilder("&d" + formats[0] + "&b, ");
			for (int i = 1; i < formats.length - 1; i++) {
				pathBuilder.append("&d").append(formats[i]).append("&b").append(", ");
			}
			pathBuilder.append("&d").append(formats[formats.length - 1]);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', pathBuilder.toString()));
		} else {
			p.sendMessage(ChatColor.LIGHT_PURPLE + "schematic" + ChatColor.AQUA + ", " + ChatColor.LIGHT_PURPLE + "schem");
		}
		return true;
	}
}