package de.zeanon.schemmanager.worldeditmode.listener;

import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.global.utils.MessageUtils;
import de.zeanon.schemmanager.worldeditmode.commands.*;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeRequestUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldeditModeMessageUtils;
import de.zeanon.storage.internal.utils.basic.Objects;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;


public class CommandListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onCommand(@NotNull final PlayerCommandPreprocessEvent event) {
		Player p = event.getPlayer();
		String[] args = event.getMessage().replace("worldedit:", "/").split("\\s+");

		if (args[0].equalsIgnoreCase("/schem")
			|| args[0].equalsIgnoreCase("/schematic")
			|| args[0].equalsIgnoreCase("//schem")
			|| args[0].equalsIgnoreCase("//schematic")) {

			String slash = args[0].equalsIgnoreCase("//schem")
						   || args[0].equalsIgnoreCase("//schematic") ? "//" : "/";
			String schemAlias = args[0].equalsIgnoreCase("/schematic")
								|| args[0].equalsIgnoreCase("//schematic") ? "schematic" : "schem";

			if (args.length == 1) {
				event.setCancelled(true);
				Help.onHelp(p, slash, schemAlias);
			} else if ((args[1].equalsIgnoreCase("delete")
						|| args[1].equalsIgnoreCase("del")
						   && p.hasPermission("worldedit.schematic.delete"))) {
				event.setCancelled(true);
				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						deleteUsage(p, slash, schemAlias);
					} else if (args[2].contains("./")) {
						p.sendMessage(ChatColor.RED + "File \'" + args[2] + "\'resolution error: Path is not allowed.");
						deleteUsage(p, slash, schemAlias);
					} else if (args.length == 4 && !WorldEditModeRequestUtils.checkDeleteFolderRequest(p, args[2])
							   && !args[3].equalsIgnoreCase("confirm")
							   && !args[3].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						deleteUsage(p, slash, schemAlias);
					} else {
						Delete.onDelete(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					deleteUsage(p, slash, schemAlias);
				}
			} else if ((args[1].equalsIgnoreCase("deletefolder")
						|| args[1].equalsIgnoreCase("delfolder"))
					   && p.hasPermission("worldedit.schematic.delete")) {
				event.setCancelled(true);
				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GREEN + "filename"
									  + ChatColor.YELLOW + ">");
						deleteFolderUsage(p, slash, schemAlias);
					} else if (args[2].contains("./")) {
						p.sendMessage(ChatColor.RED + "File \'" + args[2] + "\'resolution error: Path is not allowed.");
						deleteFolderUsage(p, slash, schemAlias);
					} else if (args.length == 4
							   && !WorldEditModeRequestUtils.checkDeleteFolderRequest(p, args[2])
							   && !args[3].equalsIgnoreCase("confirm")
							   && !args[3].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						deleteFolderUsage(p, slash, schemAlias);
					} else {
						DeleteFolder.onDeleteFolder(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					deleteFolderUsage(p, slash, schemAlias);
				}
			} else if (args[1].equalsIgnoreCase("rename")
					   && p.hasPermission("worldedit.schematic.save")) {
				event.setCancelled(true);
				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						renameUsage(p, slash, schemAlias);
					} else if (args[2].contains("./") || args.length >= 4 && args[3].contains("./")) {
						String name = args[2].contains("./") ? args[2] : args[3];
						p.sendMessage(ChatColor.RED + "File \'" + name + "\'resolution error: Path is not allowed.");
						renameUsage(p, slash, schemAlias);
					} else if (args.length == 5 && !WorldEditModeRequestUtils.checkRenameRequest(p, args[2])
							   && !args[3].equalsIgnoreCase("confirm")
							   && !args[3].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						renameUsage(p, slash, schemAlias);
					} else {
						Rename.onRename(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					renameUsage(p, slash, schemAlias);
				}
			} else if (args[1].equalsIgnoreCase("renamefolder")
					   && p.hasPermission("worldedit.schematic.save")) {
				event.setCancelled(true);
				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GREEN + "filename"
									  + ChatColor.YELLOW + ">");
						renameFolderUsage(p, slash, schemAlias);
					} else if (args[2].contains("./") || args.length >= 4 && args[3].contains("./")) {
						String name = args[2].contains("./") ? args[2] : args[3];
						p.sendMessage(ChatColor.RED + "File \'" + name + "\'resolution error: Path is not allowed.");
						renameFolderUsage(p, slash, schemAlias);
					} else if (args.length == 5
							   && !args[4].equalsIgnoreCase("confirm")
							   && !args[4].equalsIgnoreCase("deny")
							   && !WorldEditModeRequestUtils.checkRenameFolderRequest(p, args[2])) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						renameFolderUsage(p, slash, schemAlias);
					} else {
						RenameFolder.onRenameFolder(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					renameFolderUsage(p, slash, schemAlias);
				}
			} else if (args[1].equalsIgnoreCase("copy")
					   && p.hasPermission("worldedit.schematic.save")) {
				event.setCancelled(true);
				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						copyUsage(p, slash, schemAlias);
					} else if (args[2].contains("./") || args.length >= 4 && args[3].contains("./")) {
						String name = args[2].contains("./") ? args[2] : args[3];
						p.sendMessage(ChatColor.RED + "File \'" + name + "\'resolution error: Path is not allowed.");
						copyUsage(p, slash, schemAlias);
					} else if (args.length == 5 && !WorldEditModeRequestUtils.checkRenameRequest(p, args[2])
							   && !args[3].equalsIgnoreCase("confirm")
							   && !args[3].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						copyUsage(p, slash, schemAlias);
					} else {
						Copy.onCopy(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					copyUsage(p, slash, schemAlias);
				}
			} else if (args[1].equalsIgnoreCase("copyfolder")
					   && p.hasPermission("worldedit.schematic.save")) {
				event.setCancelled(true);
				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GREEN + "filename"
									  + ChatColor.YELLOW + ">");
						copyFolderUsage(p, slash, schemAlias);
					} else if (args[2].contains("./") || args.length >= 4 && args[3].contains("./")) {
						String name = args[2].contains("./") ? args[2] : args[3];
						p.sendMessage(ChatColor.RED + "File \'" + name + "\'resolution error: Path is not allowed.");
						copyFolderUsage(p, slash, schemAlias);
					} else if (args.length == 5
							   && !args[4].equalsIgnoreCase("confirm")
							   && !args[4].equalsIgnoreCase("deny")
							   && !WorldEditModeRequestUtils.checkRenameFolderRequest(p, args[2])) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						copyFolderUsage(p, slash, schemAlias);
					} else {
						CopyFolder.onCopyFolder(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					copyFolderUsage(p, slash, schemAlias);
				}
			} else if (args[1].equalsIgnoreCase("load")
					   && p.hasPermission("worldedit.schematic.load")) {
				if (args.length < 3) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "Missing argument for "
								  + ChatColor.YELLOW + "<"
								  + ChatColor.GOLD + "filename"
								  + ChatColor.YELLOW + ">");
					loadUsage(p, slash, schemAlias);
				} else if (args[2].contains("./")) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "File \'" + args[2] + "\'resolution error: Path is not allowed.");
					loadUsage(p, slash, schemAlias);
				} else if (args.length > 4) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					loadUsage(p, slash, schemAlias);
				} else if (args.length > 3 && !Objects.notNull(ConfigUtils.getStringList("File Extensions")).contains(args[3])) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.LIGHT_PURPLE + args[3] + ChatColor.RED + " is no valid file format.");
					Help.onFormats(p, true);
				}
			} else if (args[1].equalsIgnoreCase("save")
					   && p.hasPermission("worldedit.schematic.save")) {
				if (!ConfigUtils.getBoolean("Save Function Override")) {
					event.setCancelled(true);
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						defaultSaveUsage(p, slash, schemAlias);
					} else if (args[2].contains("./")) {
						p.sendMessage(ChatColor.RED + "File \'" + args[2] + "\'resolution error: Path is not allowed.");
						defaultSaveUsage(p, slash, schemAlias);
					} else if (args.length > 4 && !args[2].equalsIgnoreCase("-f")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						defaultSaveUsage(p, slash, schemAlias);
					}
				} else if (args.length > 2 && args.length < 5 && args[2].equalsIgnoreCase("-f")) {
					if (args.length == 3) {
						event.setCancelled(true);
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						saveUsage(p, slash, schemAlias);
					}
				} else {
					event.setCancelled(true);
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						saveUsage(p, slash, schemAlias);
					} else if (args[2].contains("./")) {
						p.sendMessage(ChatColor.RED + "File \'" + args[2] + "\'resolution error: Path is not allowed.");
						saveUsage(p, slash, schemAlias);
					} else if (args.length > 4 || (args.length == 4
												   && !WorldEditModeRequestUtils.checkOverWriteRequest(p, args[2])
												   && !args[3].equalsIgnoreCase("confirm")
												   && !args[3].equalsIgnoreCase("deny"))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						saveUsage(p, slash, schemAlias);
					} else {
						Save.onSave(p, args);
					}
				}
			} else if (args[1].equalsIgnoreCase("list")
					   && p.hasPermission("worldedit.schematic.list")) {
				event.setCancelled(true);
				boolean deep = false;

				if (args.length > 2 && args[2].equalsIgnoreCase("-deep")) {
					deep = true;
					args = (String[]) ArrayUtils.removeElement(args, "-deep");
				}
				if (args.length > 2 && args[2].equalsIgnoreCase("-d")) {
					deep = true;
					args = (String[]) ArrayUtils.removeElement(args, "-d");
				}

				if (args.length <= 4) {
					if (args.length == 4 && (StringUtils.isNumeric(args[2]) || !StringUtils.isNumeric(args[3]))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						listUsage(p, slash, schemAlias);
					} else if (args.length >= 3 && args[2].contains("./")) {
						p.sendMessage(ChatColor.RED + "File \'" + args[2] + "\'resolution error: Path is not allowed.");
						listUsage(p, slash, schemAlias);
					} else {
						List.onList(p, args, deep);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					listUsage(p, slash, schemAlias);
				}
			} else if (args[1].equalsIgnoreCase("listfolder")
					   && p.hasPermission("worldedit.schematic.list")) {
				event.setCancelled(true);
				boolean deep = false;

				if (args.length > 2 && args[2].equalsIgnoreCase("-deep")) {
					deep = true;
					args = (String[]) ArrayUtils.removeElement(args, "-deep");
				}
				if (args.length > 2 && args[2].equalsIgnoreCase("-d")) {
					deep = true;
					args = (String[]) ArrayUtils.removeElement(args, "-d");
				}

				if (args.length <= 4) {
					if (args.length == 4 && (StringUtils.isNumeric(args[2]) || !StringUtils.isNumeric(args[3]))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						listFolderUsage(p, slash, schemAlias);
					} else if (args.length >= 3 && args[2].contains("./")) {
						p.sendMessage(ChatColor.RED + "File \'" + args[2] + "\'resolution error: Path is not allowed.");
						listFolderUsage(p, slash, schemAlias);
					} else {
						ListFolder.onListFolder(p, args, deep);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					listFolderUsage(p, slash, schemAlias);
				}
			} else if (args[1].equalsIgnoreCase("search")
					   && p.hasPermission("worldedit.schematic.list")) {
				event.setCancelled(true);
				boolean deep = false;

				if (args.length > 2 && args[2].equalsIgnoreCase("-deep")) {
					deep = true;
					args = (String[]) ArrayUtils.removeElement(args, "-deep");
				}
				if (args.length > 2 && args[2].equalsIgnoreCase("-d")) {
					deep = true;
					args = (String[]) ArrayUtils.removeElement(args, "-d");
				}

				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						searchUsage(p, slash, schemAlias);
					} else if (args[2].contains("./")) {
						p.sendMessage(ChatColor.RED + "File \'" + args[2] + "\'resolution error: Path is not allowed.");
						searchUsage(p, slash, schemAlias);
					} else if (args.length == 5
							   && (StringUtils.isNumeric(args[2])
								   || StringUtils.isNumeric(args[3])
								   || !StringUtils.isNumeric(args[4]))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						searchUsage(p, slash, schemAlias);
					} else {
						Search.onSearch(p, args, deep);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					searchUsage(p, slash, schemAlias);
				}
			} else if (args[1].equalsIgnoreCase("searchfolder")
					   && p.hasPermission("worldedit.schematic.list")) {
				event.setCancelled(true);
				boolean deep = false;

				if (args.length > 2 && args[2].equalsIgnoreCase("-deep")) {
					deep = true;
					args = (String[]) ArrayUtils.removeElement(args, "-deep");
				}
				if (args.length > 2 && args[2].equalsIgnoreCase("-d")) {
					deep = true;
					args = (String[]) ArrayUtils.removeElement(args, "-d");
				}

				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						searchFolderUsage(p, slash, schemAlias);
					} else if (args[2].contains("./")) {
						p.sendMessage(ChatColor.RED + "File \'" + args[2] + "\'resolution error: Path is not allowed.");
						searchFolderUsage(p, slash, schemAlias);
					} else if (args.length == 5
							   && (StringUtils.isNumeric(args[2])
								   || StringUtils.isNumeric(args[3])
								   || !StringUtils.isNumeric(args[4]))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						searchFolderUsage(p, slash, schemAlias);
					} else {
						SearchFolder.onSearchFolder(p, args, deep);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					searchFolderUsage(p, slash, schemAlias);
				}
			} else if (args[1].equalsIgnoreCase("help")) {
				event.setCancelled(true);
				if (args.length == 2) {
					Help.onHelp(p, slash, schemAlias);
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
													ChatColor.GRAY + slash + schemAlias
													+ ChatColor.AQUA + " help",
													ChatColor.LIGHT_PURPLE + ""
													+ ChatColor.UNDERLINE + ""
													+ ChatColor.ITALIC + ""
													+ ChatColor.BOLD + "PLS HELP ME",
													slash + schemAlias + " help", p);
				}
			} else if (args[1].equalsIgnoreCase("formats")) {
				event.setCancelled(true);
				if (args.length == 2) {
					Help.onFormats(p, false);
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
													ChatColor.GRAY + slash + schemAlias
													+ ChatColor.AQUA + " formats",
													ChatColor.DARK_BLUE + ""
													+ ChatColor.UNDERLINE + ""
													+ ChatColor.ITALIC + ""
													+ ChatColor.BOLD + "There are different formats? :O",
													slash + schemAlias + " formats", p);
				}
			} else {
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "Invalid sub-command '" + ChatColor.GOLD + "" + args[1] + ChatColor.RED
							  + "'. Options: " + ChatColor.GOLD + "help" + ChatColor.RED + ", " + ChatColor.GOLD
							  + "formats" + ChatColor.RED + ", " + ChatColor.GOLD
							  + "save" + ChatColor.RED + ", " + ChatColor.GOLD
							  + "load" + ChatColor.RED + ", " + ChatColor.GOLD
							  + "rename" + ChatColor.RED + ", " + ChatColor.GOLD
							  + "renamefolder" + ChatColor.RED + ", " + ChatColor.GOLD
							  + "copy" + ChatColor.RED + ", " + ChatColor.GOLD
							  + "copyfolder" + ChatColor.RED + ", " + ChatColor.GOLD
							  + "delete" + ChatColor.RED + ", " + ChatColor.GOLD
							  + "deletefolder" + ChatColor.RED + ", " + ChatColor.GOLD
							  + "list" + ChatColor.RED + ", " + ChatColor.GOLD
							  + "listfolder" + ChatColor.RED + ", " + ChatColor.GOLD
							  + "search" + ChatColor.RED + ", " + ChatColor.GOLD
							  + "searchfolder");
				WorldeditModeMessageUtils.sendInvalidSubCommand(p, slash, schemAlias);
			}
		} else if (args[0].equalsIgnoreCase("/stoplag")
				   && EventListener.worldguardEnabled
				   && ConfigUtils.getBoolean("Stoplag Override")
				   && (args.length == 1
					   || (!args[1].equalsIgnoreCase("confirm")
						   && !args[1].equalsIgnoreCase("-c")))) {
			event.setCancelled(true);
			p.performCommand("stoplag confirm");
		}
	}

	private void deleteUsage(@NotNull final Player p, final String slash, final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " delete "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GOLD + "filename"
										+ ChatColor.YELLOW + ">",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " delete "
										+ ChatColor.GOLD + "example",
										slash + schemAlias + " delete ", p);
	}

	private void deleteFolderUsage(@NotNull final Player p, final String slash, final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " deletefolder "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GREEN + "filename"
										+ ChatColor.YELLOW + ">",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " deletefolder "
										+ ChatColor.GREEN + "example",
										slash + schemAlias + " deletefolder ", p);
	}

	private void renameUsage(@NotNull final Player p, final String slash, final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " rename "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GOLD + "filename"
										+ ChatColor.YELLOW + "> <"
										+ ChatColor.GOLD + "newname"
										+ ChatColor.YELLOW + ">",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " rename "
										+ ChatColor.GOLD + "example newname",
										slash + schemAlias + " rename ", p);
	}

	private void renameFolderUsage(@NotNull final Player p, final String slash, final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " renamefolder "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GREEN + "filename"
										+ ChatColor.YELLOW + "> <"
										+ ChatColor.GREEN + "newname"
										+ ChatColor.YELLOW + ">",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " renamefolder "
										+ ChatColor.GREEN + "example newname",
										slash + schemAlias + " renamefolder ", p);
	}

	private void copyUsage(@NotNull final Player p, final String slash, final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " copy "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GOLD + "filename"
										+ ChatColor.YELLOW + "> <"
										+ ChatColor.GOLD + "newname"
										+ ChatColor.YELLOW + ">",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " copy "
										+ ChatColor.GOLD + "example newname",
										slash + schemAlias + " copy ", p);
	}

	private void copyFolderUsage(@NotNull final Player p, final String slash, final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " copyfolder "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GREEN + "filename"
										+ ChatColor.YELLOW + "> <"
										+ ChatColor.GREEN + "newname"
										+ ChatColor.YELLOW + ">",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " copyfolder "
										+ ChatColor.GREEN + "example newname",
										slash + schemAlias + " copyfolder ", p);
	}

	private void loadUsage(@NotNull final Player p, final String slash, final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " load "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GOLD + "filename"
										+ ChatColor.YELLOW + "> ["
										+ ChatColor.DARK_PURPLE + "format"
										+ ChatColor.YELLOW + "]",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " load "
										+ ChatColor.GOLD + "example "
										+ ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "format"
										+ ChatColor.YELLOW + "]",
										slash + schemAlias + " load ", p);
	}

	private void defaultSaveUsage(@NotNull final Player p, final String slash, final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " save "
										+ ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "-f"
										+ ChatColor.YELLOW + "] <"
										+ ChatColor.GOLD + "filename"
										+ ChatColor.YELLOW + ">",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " save "
										+ ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-f" + ChatColor.YELLOW + "]"
										+ ChatColor.GOLD + "example",
										slash + schemAlias + " save ", p);
	}

	private void saveUsage(@NotNull final Player p, final String slash, final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " save "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GOLD + "filename"
										+ ChatColor.YELLOW + ">",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " save "
										+ ChatColor.GOLD + "example",
										slash + schemAlias + " save ", p);
	}

	private void listUsage(@NotNull final Player p, final String slash, final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " list "
										+ ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "-d"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.GREEN + "folder"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.DARK_PURPLE + "page"
										+ ChatColor.YELLOW + "]",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " list "
										+ ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "-d"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.GREEN + "folder"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.DARK_PURPLE + "page"
										+ ChatColor.YELLOW + "]",
										slash + schemAlias + " list ", p);
	}

	private void listFolderUsage(@NotNull final Player p, final String slash, final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " listfolder "
										+ ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "-d"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.GREEN + "folder"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.DARK_PURPLE + "page"
										+ ChatColor.YELLOW + "]",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " listfolder "
										+ ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "-d"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.GREEN + "folder"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.DARK_PURPLE + "page"
										+ ChatColor.YELLOW + "]",
										slash + schemAlias + " listfolder ", p);
	}

	private void searchUsage(@NotNull final Player p, final String slash, final String schemAlias) {
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

	private void searchFolderUsage(@NotNull final Player p, final String slash, final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " searchfolder "
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
										+ ChatColor.AQUA + " searchfolder "
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
	}
}
