package de.Zeanon.SchemManager.Listener;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.Zeanon.SchemManager.Commands.Command_Delete;
import de.Zeanon.SchemManager.Commands.Command_DeleteFolder;
import de.Zeanon.SchemManager.Commands.Command_Folder;
import de.Zeanon.SchemManager.Commands.Command_Help;
import de.Zeanon.SchemManager.Commands.Command_List;
import de.Zeanon.SchemManager.Commands.Command_Rename;
import de.Zeanon.SchemManager.Commands.Command_RenameFolder;
import de.Zeanon.SchemManager.Commands.Command_Save;
import de.Zeanon.SchemManager.Commands.Command_Search;
import de.Zeanon.SchemManager.Commands.Command_SearchFolder;
import de.Zeanon.SchemManager.Helper.Helper;
import net.md_5.bungee.api.ChatColor;

public class MyListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public boolean onCommand(PlayerCommandPreprocessEvent event) {
		String message = event.getMessage().replaceAll("worldedit:", "/");
		Player p;
		String[] args;
		
		if (message.toLowerCase().startsWith("/schem") || message.toLowerCase().startsWith("/schematic")
				|| message.toLowerCase().startsWith("//schem") || message.toLowerCase().startsWith("//schematic")) {
			p = event.getPlayer();
			args = event.getMessage().split(" ");
			String slash = null;
			if (!message.toLowerCase().startsWith("/schem") && !message.toLowerCase().startsWith("/schematic")) {
				slash = "//";
			} else {
				slash = "/";
			}
			

			if (args.length == 1) {
				event.setCancelled(true);
				return Command_Help.onHelp(p, slash);
			}
			

			else if (args.length > 1 && !args[1].equalsIgnoreCase("load") && !args[1].equalsIgnoreCase("save")
					&& !args[1].equalsIgnoreCase("delete") && !args[1].equalsIgnoreCase("del")
					&& !args[1].equalsIgnoreCase("deletefolder") && !args[1].equalsIgnoreCase("delfolder")
					&& !args[1].equalsIgnoreCase("rename") && !args[1].equalsIgnoreCase("renamefolder")
					&& !args[1].equalsIgnoreCase("list") && !args[1].equalsIgnoreCase("folder")
					&& !args[1].equalsIgnoreCase("search") && !args[1].equalsIgnoreCase("searchfolder")
					&& !args[1].equalsIgnoreCase("update") && !args[1].equalsIgnoreCase("disable")
					&& !args[1].equalsIgnoreCase("help")) {
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "Invalid sub-command '" + ChatColor.GOLD + "" + args[1] + ChatColor.RED
						+ "'. Options: " + ChatColor.GOLD + "help" + ChatColor.RED + ", " + ChatColor.GOLD + "load" + ChatColor.RED + ", " + ChatColor.GOLD + "save"
						+ ChatColor.RED + ", " + ChatColor.GOLD + "rename" + ChatColor.RED + ", " + ChatColor.GOLD
						+ "renamefolder" + ChatColor.RED + ", " + ChatColor.GOLD + "delete" + ChatColor.RED + ", "
						+ ChatColor.GOLD + "deletefolder" + ChatColor.RED + ", " + ChatColor.GOLD + "list"
						+ ChatColor.RED + ", " + ChatColor.GOLD + "folder" + ChatColor.RED + ", " + ChatColor.GOLD
						+ "search" + ChatColor.RED + ", " + ChatColor.GOLD + "searchfolder" + ChatColor.RED + ", "
						+ ChatColor.GOLD + "update" + ChatColor.RED + ", " + ChatColor.GOLD + "disable");
				Helper.sendInvalidSubCommand(p, slash);
				return true;
			}
			

			else if ((args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("del")) && p.hasPermission("worldedit.schematic.delete")) {
				event.setCancelled(true);
				if (args.length <= 4) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD
								+ "filename" + ChatColor.YELLOW + ">");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
								ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "delete " + ChatColor.YELLOW + "<"
										+ ChatColor.GOLD + "name" + ChatColor.YELLOW + ">",
								ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "delete "
										+ ChatColor.GOLD + "example",
								slash + "schem delete ", p);
						return true;
					}

					else if (!Helper.checkDeleteRequest(p, args[2])
							&& !args[3].equalsIgnoreCase("confirm") && !args[3].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
								ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "delete " + ChatColor.YELLOW + "<"
										+ ChatColor.GOLD + "name" + ChatColor.YELLOW + ">",
								ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "delete "
										+ ChatColor.GOLD + "example",
								slash + "schem delete ", p);
						return true;
					}
					
					else {
						return Command_Delete.onDelete(p, args);
					}
				}
				
				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
							ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "delete " + ChatColor.YELLOW + "<"
									+ ChatColor.GOLD + "name" + ChatColor.YELLOW + ">",
							ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "delete "
									+ ChatColor.GOLD + "example",
							slash + "schem delete ", p);
					return true;
				}
			}
			

			else if ((args[1].equalsIgnoreCase("deletefolder") || args[1].equalsIgnoreCase("delfolder")) && p.hasPermission("worldedit.schematic.delete")) {
				event.setCancelled(true);
				if (args.length <= 4) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GREEN
								+ "filename" + ChatColor.YELLOW + ">");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
								ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder " + ChatColor.YELLOW
										+ "<" + ChatColor.GREEN + "name" + ChatColor.YELLOW + ">",
								ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA
										+ "deletefolder " + ChatColor.GREEN + "example",
								slash + "schem deletefolder ", p);
						return true;
					}

					else if (args.length == 4 && !Helper.checkDeleteFolderRequest(p, args[2])
							&& !args[3].equalsIgnoreCase("confirm") && !args[3].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
								ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder " + ChatColor.YELLOW
										+ "<" + ChatColor.GREEN + "name" + ChatColor.YELLOW + ">",
								ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA
										+ "deletefolder " + ChatColor.GREEN + "example",
								slash + "schem deletefolder ", p);
						return true;
					}
					
					else {
						return Command_DeleteFolder.onDeleteFolder(p, args);
					}
				}
				
				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
							ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder " + ChatColor.YELLOW + "<"
									+ ChatColor.GREEN + "name" + ChatColor.YELLOW + ">",
							ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder "
									+ ChatColor.GREEN + "example",
							slash + "schem deletefolder ", p);
					return true;
				}
			}
			

			else if (args[1].equalsIgnoreCase("rename") && p.hasPermission("worldedit.schematic.save")) {
				event.setCancelled(true);
				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD
								+ "filename" + ChatColor.YELLOW + ">");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
								ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "rename " + ChatColor.YELLOW + "<"
										+ ChatColor.GOLD + "name" + ChatColor.YELLOW + ">",
								ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "rename "
										+ ChatColor.GOLD + "example",
								slash + "schem rename ", p);
						return true;
					}

					else if (args.length == 5 && !Helper.checkRenameRequest(p, args[2])
							&& !args[3].equalsIgnoreCase("confirm") && !args[3].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
								ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "rename " + ChatColor.YELLOW + "<"
										+ ChatColor.GOLD + "name" + ChatColor.YELLOW + ">",
								ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "rename "
										+ ChatColor.GOLD + "example",
								slash + "schem rename ", p);
						return true;
					}
					
					else {
						return Command_Rename.onRename(p, args);
					}
				}
				
				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
							ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "rename " + ChatColor.YELLOW + "<"
									+ ChatColor.GOLD + "name" + ChatColor.YELLOW + ">",
							ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "rename "
									+ ChatColor.GOLD + "example",
							slash + "schem rename ", p);
					return true;
				}
			}
			

			else if (args[1].equalsIgnoreCase("renamefolder") && p.hasPermission("worldedit.schematic.save")) {
				event.setCancelled(true);
				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GREEN
								+ "filename" + ChatColor.YELLOW + ">");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
								ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "renamefolder " + ChatColor.YELLOW
										+ "<" + ChatColor.GREEN + "name" + ChatColor.YELLOW + ">",
								ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA
										+ "renamefolder " + ChatColor.GREEN + "example",
								slash + "schem renamefolder ", p);
						return true;
					}

					else if (args.length == 5 && !Helper.checkRenameFolderRequest(p, args[2])
							&& !args[3].equalsIgnoreCase("confirm") && !args[3].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
								ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "renamefolder " + ChatColor.YELLOW
										+ "<" + ChatColor.GREEN + "name" + ChatColor.YELLOW + ">",
								ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA
										+ "renamefolder " + ChatColor.GREEN + "example",
								slash + "schem renamefolder ", p);
						return true;
					}
					
					else {
						return Command_RenameFolder.onRenameFolder(p, args);
					}
				}

				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
							ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "renamefolder " + ChatColor.YELLOW + "<"
									+ ChatColor.GREEN + "name" + ChatColor.YELLOW + ">",
							ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "renamefolder "
									+ ChatColor.GREEN + "example",
							slash + "schem renamefolder ", p);
					return true;
				}
			}
			

			else if (args[1].equalsIgnoreCase("load") && p.hasPermission("worldedit.schematic.load")) {
				if (args.length < 3) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD
							+ "filename" + ChatColor.YELLOW + ">");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
							ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "load " + ChatColor.YELLOW + "<"
									+ ChatColor.GOLD + "name" + ChatColor.YELLOW + ">",
							ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "load "
									+ ChatColor.GOLD + "example",
							slash + "schem load ", p);
					return true;
				}

				else if (args.length > 3) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
							ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "load " + ChatColor.YELLOW + "<"
									+ ChatColor.GOLD + "name" + ChatColor.YELLOW + ">",
							ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "load "
									+ ChatColor.GOLD + "example",
							slash + "schem load ", p);
					return true;
				} else {
					return true;
				}
			}
			

			else if (args[1].equalsIgnoreCase("save") && p.hasPermission("worldedit.schematic.save")) {
				if (Helper.getBoolean("Save Function Override") && (args.length < 3 || args.length > 4 || !args[2].equals("-f"))) {
					event.setCancelled(true);
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD
								+ "filename" + ChatColor.YELLOW + ">");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
								ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "save " + ChatColor.YELLOW + "<"
										+ ChatColor.GOLD + "name" + ChatColor.YELLOW + ">",
								ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "save "
										+ ChatColor.GOLD + "example",
								slash + "schem save ", p);
						return true;
					}
					
					else if (args.length > 4) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
								ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "save " + ChatColor.YELLOW + "<"
										+ ChatColor.GOLD + "name" + ChatColor.YELLOW + ">",
								ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "save "
										+ ChatColor.GOLD + "example",
								slash + "schem save ", p);
						return true;
					}
					
					else {
						if (!Helper.checkDeleteRequest(p, args[2]) && !args[3].equalsIgnoreCase("confirm") && !args[3].equalsIgnoreCase("deny")) {
							p.sendMessage(ChatColor.RED + "Too many arguments.");
							Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
									ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "save " + ChatColor.YELLOW + "<"
											+ ChatColor.GOLD + "name" + ChatColor.YELLOW + ">",
									ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "save "
											+ ChatColor.GOLD + "example",
									slash + "schem save ", p);
							return true;
						}
	
						else {
							return Command_Save.onSave(p, args);
						}
					}
				}
				else if (!Helper.getBoolean("Save Function Override")){
					//TODO
					return true;
				}
				else {
					return true;
				}
			}
			

			else if (args[1].equalsIgnoreCase("list") && p.hasPermission("worldedit.schematic.list")) {
				event.setCancelled(true);
				boolean deep = false;
				if (args.length > 2 && args[2].equalsIgnoreCase("-deep")) {
					deep = true;
					args = (String[]) ((String[]) ArrayUtils.removeElement(args, "-deep"));
				}
				if (args.length > 2 && args[2].equalsIgnoreCase("-d")) {
					deep = true;
					args = (String[]) ((String[]) ArrayUtils.removeElement(args, "-d"));
				}
				
				if (args.length <= 4) {
					if (args.length == 4 && (StringUtils.isNumeric(args[2]) || !StringUtils.isNumeric(args[4]))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
								ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "list " + ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN
										+ "folder" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page"
										+ ChatColor.YELLOW + "]",
								ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "list "
										+ ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW
										+ "] [" + ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] ["
										+ ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
								slash + "schem list ", p);
						return true;
					}

					else {
						return Command_List.onList(p, args, deep);
					}
				}

				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
							ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "list " + ChatColor.YELLOW + "["
									+ ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "folder"
									+ ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
							ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "list "
									+ ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] ["
									+ ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page"
									+ ChatColor.YELLOW + "]",
							slash + "schem list ", p);
					return true;
				}
			}
			

			else if (args[1].equalsIgnoreCase("folder") && p.hasPermission("worldedit.schematic.list")) {
				event.setCancelled(true);
				boolean deep = false;
				
				if (args.length > 2 && args[2].equalsIgnoreCase("-deep")) {
					deep = true;
					args = (String[]) ((String[]) ArrayUtils.removeElement(args, "-deep"));
				}
				if (args.length > 2 && args[2].equalsIgnoreCase("-d")) {
					deep = true;
					args = (String[]) ((String[]) ArrayUtils.removeElement(args, "-d"));
				}
				
				if (args.length <= 4) {
					if (args.length == 4 && (StringUtils.isNumeric(args[2]) || !StringUtils.isNumeric(args[3]))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
								ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "folder " + ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN
										+ "folder" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page"
										+ ChatColor.YELLOW + "]",
								ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "folder "
										+ ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW
										+ "] [" + ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] ["
										+ ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
								slash + "schem folder ", p);
						return true;
					}
					
					else {
						return Command_Folder.onFolder(p, args, deep);
					}
				}

				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
							ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "folder " + ChatColor.YELLOW + "["
									+ ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "folder"
									+ ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
							ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "folder "
									+ ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] ["
									+ ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page"
									+ ChatColor.YELLOW + "]",
							slash + "schem folder ", p);
					return true;
				}
			}
			

			else if (args[1].equalsIgnoreCase("search") && p.hasPermission("worldedit.schematic.list")) {
				event.setCancelled(true);
				boolean deep = false;
				if (args.length > 2 && args[2].equalsIgnoreCase("-deep")) {
					deep = true;
					args = (String[]) ((String[]) ArrayUtils.removeElement(args, "-deep"));
				}
				if (args.length > 2 && args[2].equalsIgnoreCase("-d")) {
					deep = true;
					args = (String[]) ((String[]) ArrayUtils.removeElement(args, "-d"));
				}

				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD
								+ "name" + ChatColor.YELLOW + ">");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
								ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "search " + ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN
										+ "folder" + ChatColor.YELLOW + "] <" + ChatColor.GOLD + "name"
										+ ChatColor.YELLOW + "> [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW
										+ "]",
								ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "search "
										+ ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW
										+ "] [" + ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] " + ChatColor.GOLD
										+ "example" + ChatColor.YELLOW + " [" + ChatColor.DARK_PURPLE + "page"
										+ ChatColor.YELLOW + "]",
								slash + "schem search ", p);
						return true;
					}

					else if (args.length == 5 && (StringUtils.isNumeric(args[2]) || StringUtils.isNumeric(args[3]) || !StringUtils.isNumeric(args[4]))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
								ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "search " + ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN
										+ "folder" + ChatColor.YELLOW + "] <" + ChatColor.GOLD + "name"
										+ ChatColor.YELLOW + "> [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW
										+ "]",
								ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "search "
										+ ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW
										+ "] [" + ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] " + ChatColor.GOLD
										+ "example" + ChatColor.YELLOW + " [" + ChatColor.DARK_PURPLE + "page"
										+ ChatColor.YELLOW + "]",
								slash + "schem search ", p);
						return true;
					}

					else {
						return Command_Search.onSearch(p, args, deep);
					}
				}

				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
							ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "search " + ChatColor.YELLOW + "["
									+ ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "folder"
									+ ChatColor.YELLOW + "] <" + ChatColor.GOLD + "name" + ChatColor.YELLOW + "> ["
									+ ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
							ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "search "
									+ ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] ["
									+ ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] " + ChatColor.GOLD + "example"
									+ ChatColor.YELLOW + " [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
							slash + "schem search ", p);
					return true;
				}
			}
			

			else if (args[1].equalsIgnoreCase("searchfolder") && p.hasPermission("worldedit.schematic.list")) {
				event.setCancelled(true);
				boolean deep = false;
				if (args.length > 2 && args[2].equalsIgnoreCase("-deep")) {
					deep = true;
					args = (String[]) ((String[]) ArrayUtils.removeElement(args, "-deep"));
				}
				if (args.length > 2 && args[2].equalsIgnoreCase("-d")) {
					deep = true;
					args = (String[]) ((String[]) ArrayUtils.removeElement(args, "-d"));
				}

				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD
								+ "name" + ChatColor.YELLOW + ">");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
								ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "searchfolder " + ChatColor.YELLOW
										+ "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] ["
										+ ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] <" + ChatColor.GOLD
										+ "name" + ChatColor.YELLOW + "> [" + ChatColor.DARK_PURPLE + "page"
										+ ChatColor.YELLOW + "]",
								ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA
										+ "searchfolder " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d"
										+ ChatColor.YELLOW + "] [" + ChatColor.GREEN + "folder" + ChatColor.YELLOW
										+ "] " + ChatColor.GOLD + "example" + ChatColor.YELLOW + " ["
										+ ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
								slash + "schem searchfolder ", p);
						return true;
					}

					else if (args.length == 5 && (StringUtils.isNumeric(args[2]) || StringUtils.isNumeric(args[3]) || !StringUtils.isNumeric(args[4]))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
								ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "searchfolder " + ChatColor.YELLOW
										+ "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] ["
										+ ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] <" + ChatColor.GOLD
										+ "name" + ChatColor.YELLOW + "> [" + ChatColor.DARK_PURPLE + "page"
										+ ChatColor.YELLOW + "]",
								ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA
										+ "searchfolder " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d"
										+ ChatColor.YELLOW + "] [" + ChatColor.GREEN + "folder" + ChatColor.YELLOW
										+ "] " + ChatColor.GOLD + "example" + ChatColor.YELLOW + " ["
										+ ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
								slash + "schem searchfolder ", p);
						return true;
					}

					else {
						return Command_SearchFolder.onSearchFolder(p, args, deep);
					}
				}

				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
							ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "searchfolder " + ChatColor.YELLOW + "["
									+ ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "folder"
									+ ChatColor.YELLOW + "] <" + ChatColor.GOLD + "name" + ChatColor.YELLOW + "> ["
									+ ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
							ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "searchfolder "
									+ ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] ["
									+ ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] " + ChatColor.GOLD + "example"
									+ ChatColor.YELLOW + " [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
							slash + "schem searchfolder ", p);
					return true;
				}
			}
			

			else if (args[1].equalsIgnoreCase("update") && p.hasPermission("schemmanager.update")) {
				event.setCancelled(true);
				if (args.length == 2) {
					Helper.sendBooleanMessage(ChatColor.RED + "Do you really want to update?", "//schem update confirm",
							"//schem update deny", p);
					Helper.addUpdateRequest(p);
					return true;
				}

				else if (args.length == 3 && (args[2].equalsIgnoreCase("confirm") || args[2].equalsIgnoreCase("deny"))) {
					if (args[2].equalsIgnoreCase("confirm") && Helper.checkUpdateRequest(p)) {
						Helper.removeUpdateRequest(p);
						return Helper.update(p);
					}

					else if (args[2].equalsIgnoreCase("deny") && Helper.checkUpdateRequest(p)) {
						Helper.removeUpdateRequest(p);
						p.sendMessage(ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " will not be updated.");
						return true;
					}
					else {
						return true;
					}
				}

				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
							ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "update", ChatColor.DARK_GREEN + ""
									+ ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "!!UPDATE BABY!!",
							slash + "schem update", p);
					return true;
				}
			}
			
			
			else if (args[1].equalsIgnoreCase("help")) {
				event.setCancelled(true);
				if (args.length == 2) {
					return Command_Help.onHelp(p, slash);
				}
				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
							ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "help", ChatColor.LIGHT_PURPLE + ""
									+ ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "PLS HELP ME",
							slash + "schem help", p);
					return true;
				}
			}
			

			else if (args[1].equalsIgnoreCase("disable") && p.hasPermission("schemmanager.disable")) {
				event.setCancelled(true);
				if (args.length == 2) {
					Helper.sendBooleanMessage(ChatColor.RED + "Do you really want to disable " + ChatColor.DARK_PURPLE
							+ "SchemManager" + ChatColor.RED + "? ", "//schem disable confirm", "//schem disable deny",
							p);
					Helper.addDisableRequest(p);
					return true;
				}

				else if (args.length == 3 && (args[2].equalsIgnoreCase("confirm") || args[2].equalsIgnoreCase("deny"))) {
					if (args[2].equalsIgnoreCase("confirm") && Helper.checkDisableRequest(p)) {
						p.sendMessage(ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " is being disabled.");
						Helper.disable();
						return true;
					}

					else if (args[2].equalsIgnoreCase("deny") && Helper.checkDisableRequest(p)) {
						Helper.removeDisableRequest(p);
						p.sendMessage(
								ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " will not be disabled.");
						return true;
					} else {
						return true;
					}
				}
				
				else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
							ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "disable", ChatColor.DARK_RED + ""
									+ ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "PLS DON'T D;",
							slash + "schem disable", p);
					return true;
				}
			} else {
				return false;
			}
		}
		
		
		else if (message.toLowerCase().startsWith("/stoplag")) {
			p = event.getPlayer();
			args = event.getMessage().split(" ");
			if (p.hasPermission("worldguard.halt-activity") && args.length == 1) {
				event.setCancelled(true);
				p.performCommand("stoplag confirm");
				return true;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		Helper.removeDisableRequest(p);
		Helper.removeUpdateRequest(p);
		Helper.removeDeleteRequest(p);
		Helper.removeDeleteFolderRequest(p);
		Helper.removeRenameRequest(p);
		Helper.removeRenameFolderRequest(p);
		Helper.removeOverWriteRequest(p);
	}
}