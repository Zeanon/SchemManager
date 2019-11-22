package de.zeanon.schemmanager.worldeditmode.commands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.global.utils.MessageUtils;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Help {

	public static void onHelp(final @NotNull Player p, final String slash, final String schemAlias) {
		if (ConfigUtils.getBoolean("Space Lists")) {
			p.sendMessage("");
		}

		p.sendMessage(ChatColor.AQUA + "=== SchemManager | Version "
					  + SchemManager.getInstance().getDescription().getVersion()
					  + " ===");
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Get some help: ",
										ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "help",
										ChatColor.LIGHT_PURPLE + ""
										+ ChatColor.UNDERLINE + ""
										+ ChatColor.ITALIC + ""
										+ ChatColor.BOLD + "OMG PLS HELP ME",
										slash + schemAlias + " help", p);
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Show Available formats: ",
										ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "formats",
										ChatColor.BLUE + ""
										+ ChatColor.UNDERLINE + ""
										+ ChatColor.ITALIC + ""
										+ ChatColor.BOLD + "There are different formats? :O",
										slash + schemAlias + " formats", p);
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Load a schematic: ",
										ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "load "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GOLD + "filename"
										+ ChatColor.YELLOW + "> ["
										+ ChatColor.DARK_PURPLE + "format"
										+ ChatColor.YELLOW + "]",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "load "
										+ ChatColor.GOLD + "example "
										+ ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "format"
										+ ChatColor.YELLOW + "]",
										slash + schemAlias + " load ", p);
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Save a schematic: ",
										ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "save "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GOLD + "filename"
										+ ChatColor.YELLOW + ">",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "save "
										+ ChatColor.GOLD + "example",
										slash + schemAlias + " save ", p);
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Rename a schematic: ",
										ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "rename "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GOLD + "filename"
										+ ChatColor.YELLOW + "> <"
										+ ChatColor.GOLD + "newname"
										+ ChatColor.YELLOW + ">",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "rename "
										+ ChatColor.GOLD + "example newname",
										slash + schemAlias + " rename ", p);
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Rename a folder: ",
										ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "renamefolder "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GREEN + "filename"
										+ ChatColor.YELLOW + "> <"
										+ ChatColor.GREEN + "newname"
										+ ChatColor.YELLOW + ">",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "renamefolder "
										+ ChatColor.GREEN + "example newname",
										slash + schemAlias + " renamefolder ", p);
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Copy a schematic: ",
										ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "copy "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GOLD + "filename"
										+ ChatColor.YELLOW + "> <"
										+ ChatColor.GOLD + "newname"
										+ ChatColor.YELLOW + ">",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias
										+ " " + ChatColor.AQUA + "copy "
										+ ChatColor.GOLD + "example newname",
										slash + schemAlias + " copy ", p);
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Copy a folder: ",
										ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "copyfolder "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GREEN + "filename"
										+ ChatColor.YELLOW + "> <"
										+ ChatColor.GREEN + "newname"
										+ ChatColor.YELLOW + ">",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "copyfolder "
										+ ChatColor.GREEN + "example newname",
										slash + schemAlias + " copyfolder ", p);
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Delete a schematic: ",
										ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "delete "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GOLD + "filename"
										+ ChatColor.YELLOW + ">",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "delete "
										+ ChatColor.GOLD + "example",
										slash + schemAlias + " delete ", p);
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Delete a folder: ",
										ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "deletefolder "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GREEN + "filename"
										+ ChatColor.YELLOW + ">",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "deletefolder "
										+ ChatColor.GREEN + "example",
										slash + schemAlias + " deletefolder ", p);
		MessageUtils.sendSuggestMessage(ChatColor.RED + "List schematics: ",
										ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "list "
										+ ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "-d"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.GREEN + "folder"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.DARK_PURPLE + "page"
										+ ChatColor.YELLOW + "]",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "list "
										+ ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "-d"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.GREEN + "folder"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.DARK_PURPLE + "page"
										+ ChatColor.YELLOW + "]",
										slash + schemAlias + " list ", p);
		MessageUtils.sendSuggestMessage(ChatColor.RED + "List folders: ",
										ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "listfolder "
										+ ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "-d"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.GREEN + "folder"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.DARK_PURPLE + "page"
										+ ChatColor.YELLOW + "]",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "listfolder "
										+ ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "-d"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.GREEN + "folder"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.DARK_PURPLE + "page"
										+ ChatColor.YELLOW + "]",
										slash + schemAlias + " listfolder ", p);
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Search for a schematic: ",
										ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "search "
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
										+ ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "search "
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
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Search for a folder: ",
										ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "searchfolder "
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
										+ ChatColor.GRAY + slash + schemAlias + " "
										+ ChatColor.AQUA + "searchfolder "
										+ ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "-d"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.GREEN + "folder"
										+ ChatColor.YELLOW + "] "
										+ ChatColor.GOLD + "example"
										+ ChatColor.YELLOW + " ["
										+ ChatColor.DARK_PURPLE + "page"
										+ ChatColor.YELLOW + "]",
										slash + schemAlias + " searchfolder ", p);
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Update the plugin: ",
										ChatColor.GRAY + "/schemmanager "
										+ ChatColor.AQUA + "update",
										ChatColor.DARK_GREEN + ""
										+ ChatColor.UNDERLINE + ""
										+ ChatColor.ITALIC + ""
										+ ChatColor.BOLD + "!!UPDATE BABY!!",
										"/schemmanager update", p);
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Disable the plugin: ",
										ChatColor.GRAY + "/schemmanager "
										+ ChatColor.AQUA + "disable",
										ChatColor.DARK_RED + ""
										+ ChatColor.UNDERLINE + ""
										+ ChatColor.ITALIC + ""
										+ ChatColor.BOLD + "PLS DON'T D;",
										"/schemmanager disable", p);
	}

	public static void onFormats(final @NotNull Player p, final boolean suppressBlankLine) {
		if (ConfigUtils.getBoolean("Space Lists") && !suppressBlankLine) {
			p.sendMessage("");
		}
		p.sendMessage(ChatColor.RED + "Available formats:");
		if (!Objects.notNull(ConfigUtils.getStringList("File Extensions")).isEmpty()) {
			@NotNull String[] formats = Objects.notNull(ConfigUtils.getStringList("File Extensions")).toArray(new String[0]);
			@NotNull StringBuilder pathBuilder = new StringBuilder("&d" + formats[0] + "&b, ");
			for (byte i = 1; i < formats.length - 1; i++) {
				pathBuilder.append("&d").append(formats[i]).append("&b").append(", ");
			}
			pathBuilder.append("&d").append(formats[formats.length - 1]);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', pathBuilder.toString()));
		} else {
			p.sendMessage(ChatColor.LIGHT_PURPLE + "schem"
						  + ChatColor.AQUA + ", "
						  + ChatColor.LIGHT_PURPLE + "schematic");
		}
	}
}