package de.zeanon.schemmanager.worldeditmode.listener;

import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.global.utils.MessageUtils;
import de.zeanon.schemmanager.worldeditmode.commands.*;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeRequestUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldeditModeMessageUtils;
import de.zeanon.storagemanager.internal.utility.basic.Objects;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("DuplicatedCode")
public class CommandListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onCommand(final @NotNull PlayerCommandPreprocessEvent event) {
		@NotNull Player p = event.getPlayer();
		@NotNull String[] args = event.getMessage().replace("worldedit:", "/").split("\\s+");

		if (args[0].equalsIgnoreCase("/schem")
			|| args[0].equalsIgnoreCase("/schematic")
			|| args[0].equalsIgnoreCase("//schem")
			|| args[0].equalsIgnoreCase("//schematic")) {

			@NotNull String slash = args[0].equalsIgnoreCase("//schem")
									|| args[0].equalsIgnoreCase("//schematic") ? "//" : "/";
			@NotNull String schemAlias = args[0].equalsIgnoreCase("/schematic")
										 || args[0].equalsIgnoreCase("//schematic") ? "schematic" : "schem";

			// <Help>
			if (args.length == 1) {
				event.setCancelled(true);
				Help.onHelp(p, slash, schemAlias);
				// </Help>

				// <Delete>
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
						this.deleteUsage(p, slash, schemAlias);
					} else if (args[2].contains("./")) {
						p.sendMessage(ChatColor.RED + "File '" + args[2] + "'resolution error: Path is not allowed.");
						this.deleteUsage(p, slash, schemAlias);
					} else if (args.length == 4 && !WorldEditModeRequestUtils.checkDeleteFolderRequest(p, args[2])
							   && !args[3].equalsIgnoreCase("confirm")
							   && !args[3].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						this.deleteUsage(p, slash, schemAlias);
					} else {
						Delete.onDelete(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					this.deleteUsage(p, slash, schemAlias);
				}
				// </Delete>

				// <DeleteFolder>
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
						this.deleteFolderUsage(p, slash, schemAlias);
					} else if (args[2].contains("./")) {
						p.sendMessage(ChatColor.RED + "File '" + args[2] + "'resolution error: Path is not allowed.");
						this.deleteFolderUsage(p, slash, schemAlias);
					} else if (args.length == 4
							   && !WorldEditModeRequestUtils.checkDeleteFolderRequest(p, args[2])
							   && !args[3].equalsIgnoreCase("confirm")
							   && !args[3].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						this.deleteFolderUsage(p, slash, schemAlias);
					} else {
						DeleteFolder.onDeleteFolder(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					this.deleteFolderUsage(p, slash, schemAlias);
				}
				// </DeleteFolder>

				// <Rename>
			} else if (args[1].equalsIgnoreCase("rename")
					   && p.hasPermission("worldedit.schematic.save")) {
				event.setCancelled(true);
				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						this.renameUsage(p, slash, schemAlias);
					} else if (args[2].contains("./") || args.length >= 4 && args[3].contains("./")) {
						String name = args[2].contains("./") ? args[2] : args[3];
						p.sendMessage(ChatColor.RED + "File '" + name + "'resolution error: Path is not allowed.");
						this.renameUsage(p, slash, schemAlias);
					} else if (args.length == 5 && !WorldEditModeRequestUtils.checkRenameRequest(p, args[2])
							   && !args[3].equalsIgnoreCase("confirm")
							   && !args[3].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						this.renameUsage(p, slash, schemAlias);
					} else {
						Rename.onRename(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					this.renameUsage(p, slash, schemAlias);
				}
				// </Rename>

				// <RenameFolder>
			} else if (args[1].equalsIgnoreCase("renamefolder")
					   && p.hasPermission("worldedit.schematic.save")) {
				event.setCancelled(true);
				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GREEN + "filename"
									  + ChatColor.YELLOW + ">");
						this.renameFolderUsage(p, slash, schemAlias);
					} else if (args[2].contains("./") || args.length >= 4 && args[3].contains("./")) {
						String name = args[2].contains("./") ? args[2] : args[3];
						p.sendMessage(ChatColor.RED + "File '" + name + "'resolution error: Path is not allowed.");
						this.renameFolderUsage(p, slash, schemAlias);
					} else if (args.length == 5
							   && !args[4].equalsIgnoreCase("confirm")
							   && !args[4].equalsIgnoreCase("deny")
							   && !WorldEditModeRequestUtils.checkRenameFolderRequest(p, args[2])) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						this.renameFolderUsage(p, slash, schemAlias);
					} else {
						RenameFolder.onRenameFolder(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					this.renameFolderUsage(p, slash, schemAlias);
				}
				// </RenameFolder>

				// <Copy>
			} else if (args[1].equalsIgnoreCase("copy")
					   && p.hasPermission("worldedit.schematic.save")) {
				event.setCancelled(true);
				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						this.copyUsage(p, slash, schemAlias);
					} else if (args[2].contains("./") || args.length >= 4 && args[3].contains("./")) {
						String name = args[2].contains("./") ? args[2] : args[3];
						p.sendMessage(ChatColor.RED + "File '" + name + "'resolution error: Path is not allowed.");
						this.copyUsage(p, slash, schemAlias);
					} else if (args.length == 5 && !WorldEditModeRequestUtils.checkRenameRequest(p, args[2])
							   && !args[3].equalsIgnoreCase("confirm")
							   && !args[3].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						this.copyUsage(p, slash, schemAlias);
					} else {
						Copy.onCopy(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					this.copyUsage(p, slash, schemAlias);
				}
				// </Copy>

				// <CopyFolder>
			} else if (args[1].equalsIgnoreCase("copyfolder")
					   && p.hasPermission("worldedit.schematic.save")) {
				event.setCancelled(true);
				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GREEN + "filename"
									  + ChatColor.YELLOW + ">");
						this.copyFolderUsage(p, slash, schemAlias);
					} else if (args[2].contains("./") || args.length >= 4 && args[3].contains("./")) {
						String name = args[2].contains("./") ? args[2] : args[3];
						p.sendMessage(ChatColor.RED + "File '" + name + "'resolution error: Path is not allowed.");
						this.copyFolderUsage(p, slash, schemAlias);
					} else if (args.length == 5
							   && !args[4].equalsIgnoreCase("confirm")
							   && !args[4].equalsIgnoreCase("deny")
							   && !WorldEditModeRequestUtils.checkRenameFolderRequest(p, args[2])) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						this.copyFolderUsage(p, slash, schemAlias);
					} else {
						CopyFolder.onCopyFolder(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					this.copyFolderUsage(p, slash, schemAlias);
				}
				// </CopyFolder>

				// <Load>
			} else if (args[1].equalsIgnoreCase("load")
					   && p.hasPermission("worldedit.schematic.load")) {
				if (args.length < 3) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "Missing argument for "
								  + ChatColor.YELLOW + "<"
								  + ChatColor.GOLD + "filename"
								  + ChatColor.YELLOW + ">");
					this.loadUsage(p, slash, schemAlias);
				} else if (args[2].contains("./")) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "File '" + args[2] + "'resolution error: Path is not allowed.");
					this.loadUsage(p, slash, schemAlias);
				} else if (args.length > 4) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					this.loadUsage(p, slash, schemAlias);
				} else if (args.length > 3 && !Objects.notNull(ConfigUtils.getStringList("File Extensions")).contains(args[3])) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.LIGHT_PURPLE + args[3] + ChatColor.RED + " is no valid file format.");
					Help.onFormats(p, true);
				}
				// </Load>

				// <Save>
			} else if (args[1].equalsIgnoreCase("save")
					   && p.hasPermission("worldedit.schematic.save")) {
				if (!ConfigUtils.getBoolean("Save Function Override")) {
					event.setCancelled(true);
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						this.defaultSaveUsage(p, slash, schemAlias);
					} else if (args[2].contains("./")) {
						p.sendMessage(ChatColor.RED + "File '" + args[2] + "'resolution error: Path is not allowed.");
						this.defaultSaveUsage(p, slash, schemAlias);
					} else if (args.length > 4 && !args[2].equalsIgnoreCase("-f")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						this.defaultSaveUsage(p, slash, schemAlias);
					}
				} else if (args.length > 2 && args.length < 5 && args[2].equalsIgnoreCase("-f")) {
					if (args.length == 3) {
						event.setCancelled(true);
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						this.saveUsage(p, slash, schemAlias);
					}
				} else {
					event.setCancelled(true);
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						this.saveUsage(p, slash, schemAlias);
					} else if (args[2].contains("./")) {
						p.sendMessage(ChatColor.RED + "File '" + args[2] + "'resolution error: Path is not allowed.");
						this.saveUsage(p, slash, schemAlias);
					} else if (args.length > 4 || (args.length == 4
												   && !WorldEditModeRequestUtils.checkOverWriteRequest(p, args[2])
												   && !args[3].equalsIgnoreCase("confirm")
												   && !args[3].equalsIgnoreCase("deny"))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						this.saveUsage(p, slash, schemAlias);
					} else {
						Save.onSave(p, args);
					}
				}
				// </Save>

				// <List>
			} else if (args[1].equalsIgnoreCase("list")
					   && p.hasPermission("worldedit.schematic.list")) {
				event.setCancelled(true);

				final boolean deep;
				final int modifierCount;

				if (args.length > 2 && (args[2].equalsIgnoreCase("-deep") || args[2].equalsIgnoreCase("-d"))) {
					deep = true;
					modifierCount = 1;
				} else {
					deep = false;
					modifierCount = 0;
				}

				if (args.length <= 4 + modifierCount) {
					if (args.length == 4 + modifierCount && (StringUtils.isNumeric(args[2 + modifierCount]) || !StringUtils.isNumeric(args[3 + modifierCount]))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						this.listUsage(p, slash, schemAlias);
					} else if (args.length >= 3 + modifierCount && args[2 + modifierCount].contains("./")) {
						p.sendMessage(ChatColor.RED + "File '" + args[2 + modifierCount] + "'resolution error: Path is not allowed.");
						this.listUsage(p, slash, schemAlias);
					} else {
						List.onList(p, args, deep, modifierCount);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					this.listUsage(p, slash, schemAlias);
				}
				// </List>

				// <ListFolders>
			} else if (args[1].equalsIgnoreCase("listfolders")
					   && p.hasPermission("worldedit.schematic.list")) {
				event.setCancelled(true);

				final boolean deep;
				final int modifierCount;

				if (args.length > 2 && (args[2].equalsIgnoreCase("-deep") || args[2].equalsIgnoreCase("-d"))) {
					deep = true;
					modifierCount = 1;
				} else {
					deep = false;
					modifierCount = 0;
				}

				if (args.length <= 4 + modifierCount) {
					if (args.length == 4 + modifierCount && (StringUtils.isNumeric(args[2 + modifierCount]) || !StringUtils.isNumeric(args[3 + modifierCount]))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						this.listFolderUsage(p, slash, schemAlias);
					} else if (args.length >= 3 + modifierCount && args[2 + modifierCount].contains("./")) {
						p.sendMessage(ChatColor.RED + "File '" + args[2 + modifierCount] + "'resolution error: Path is not allowed.");
						this.listFolderUsage(p, slash, schemAlias);
					} else {
						ListFolders.onListFolder(p, args, deep, modifierCount);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					this.listFolderUsage(p, slash, schemAlias);
				}
				// </ListFolders>

				// <Search>
			} else if (args[1].equalsIgnoreCase("search")
					   && p.hasPermission("worldedit.schematic.list")) {
				event.setCancelled(true);

				final int modifierCount;
				final boolean deep;
				final boolean caseSensitive;

				if (args.length > 2 && (args[2].equalsIgnoreCase("-deep") || args[2].equalsIgnoreCase("-d"))) {
					deep = true;

					if (args.length > 3 && (args[3].equalsIgnoreCase("-casesensitive") || args[3].equalsIgnoreCase("-c"))) {
						modifierCount = 2;
						caseSensitive = true;
					} else {
						modifierCount = 1;
						caseSensitive = false;
					}
				} else if (args.length > 2 && (args[2].equalsIgnoreCase("-casesensitive") || args[2].equalsIgnoreCase("-c"))) {
					caseSensitive = true;

					if (args.length > 3 && (args[3].equalsIgnoreCase("-deep") || args[3].equalsIgnoreCase("-d"))) {
						modifierCount = 2;
						deep = true;
					} else {
						modifierCount = 1;
						deep = false;
					}
				} else {
					modifierCount = 0;
					deep = false;
					caseSensitive = false;
				}

				if (args.length <= 5 + modifierCount) {
					if (args.length < 3 + modifierCount) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						this.searchUsage(p, slash, schemAlias);
					} else if (args[2 + modifierCount].contains("./")) {
						p.sendMessage(ChatColor.RED + "File '" + args[2 + modifierCount] + "'resolution error: Path is not allowed.");
						this.searchUsage(p, slash, schemAlias);
					} else if (args.length == 5 + modifierCount
							   && (StringUtils.isNumeric(args[2 + modifierCount])
								   || StringUtils.isNumeric(args[3 + modifierCount])
								   || !StringUtils.isNumeric(args[4 + modifierCount]))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						this.searchUsage(p, slash, schemAlias);
					} else {
						Search.onSearch(p, args, deep, caseSensitive, modifierCount);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					this.searchUsage(p, slash, schemAlias);
				}
				// </Search>

				// <SearchFolder>
			} else if (args[1].equalsIgnoreCase("searchfolder")
					   && p.hasPermission("worldedit.schematic.list")) {
				event.setCancelled(true);

				final int modifierCount;
				final boolean deep;
				final boolean caseSensitive;

				if (args.length > 2 && (args[2].equalsIgnoreCase("-deep") || args[2].equalsIgnoreCase("-d"))) {
					deep = true;

					if (args.length > 3 && (args[3].equalsIgnoreCase("-casesensitive") || args[3].equalsIgnoreCase("-c"))) {
						modifierCount = 2;
						caseSensitive = true;
					} else {
						modifierCount = 1;
						caseSensitive = false;
					}
				} else if (args.length > 2 && (args[2].equalsIgnoreCase("-casesensitive") || args[2].equalsIgnoreCase("-c"))) {
					caseSensitive = true;

					if (args.length > 3 && (args[3].equalsIgnoreCase("-deep") || args[3].equalsIgnoreCase("-d"))) {
						modifierCount = 2;
						deep = true;
					} else {
						modifierCount = 1;
						deep = false;
					}
				} else {
					modifierCount = 0;
					deep = false;
					caseSensitive = false;
				}

				if (args.length <= 5 + modifierCount) {
					if (args.length < 3 + modifierCount) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						this.searchFolderUsage(p, slash, schemAlias);
					} else if (args[2 + modifierCount].contains("./")) {
						p.sendMessage(ChatColor.RED + "File '" + args[2 + modifierCount] + "'resolution error: Path is not allowed.");
						this.searchFolderUsage(p, slash, schemAlias);
					} else if (args.length == 5 + modifierCount
							   && (StringUtils.isNumeric(args[2 + modifierCount])
								   || StringUtils.isNumeric(args[3 + modifierCount])
								   || !StringUtils.isNumeric(args[4 + modifierCount]))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						this.searchFolderUsage(p, slash, schemAlias);
					} else {
						SearchFolder.onSearchFolder(p, args, deep, caseSensitive, modifierCount);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					this.searchFolderUsage(p, slash, schemAlias);
				}
				// </SearchFolder>

				// <Help>
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
				// </Help>

				// <Formats>
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
				// </Formats>

				// <Invalid Command>
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
							  + "listfolders" + ChatColor.RED + ", " + ChatColor.GOLD
							  + "search" + ChatColor.RED + ", " + ChatColor.GOLD
							  + "searchfolder");
				WorldeditModeMessageUtils.sendInvalidSubCommand(p, slash, schemAlias);
			}
			// </Invalid Command>
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

	private void deleteUsage(final @NotNull Player p, final String slash, final String schemAlias) {
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

	private void deleteFolderUsage(final @NotNull Player p, final String slash, final String schemAlias) {
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

	private void renameUsage(final @NotNull Player p, final String slash, final String schemAlias) {
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

	private void renameFolderUsage(final @NotNull Player p, final String slash, final String schemAlias) {
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

	private void copyUsage(final @NotNull Player p, final String slash, final String schemAlias) {
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

	private void copyFolderUsage(final @NotNull Player p, final String slash, final String schemAlias) {
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

	private void loadUsage(final @NotNull Player p, final String slash, final String schemAlias) {
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

	private void defaultSaveUsage(final @NotNull Player p, final String slash, final String schemAlias) {
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

	private void saveUsage(final @NotNull Player p, final String slash, final String schemAlias) {
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

	private void listUsage(final @NotNull Player p, final String slash, final String schemAlias) {
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

	private void listFolderUsage(final @NotNull Player p, final String slash, final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " listfolders "
										+ ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "-d"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.GREEN + "folder"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.DARK_PURPLE + "page"
										+ ChatColor.YELLOW + "]",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " listfolders "
										+ ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "-d"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.GREEN + "folder"
										+ ChatColor.YELLOW + "] ["
										+ ChatColor.DARK_PURPLE + "page"
										+ ChatColor.YELLOW + "]",
										slash + schemAlias + " listfolders ", p);
	}

	private void searchUsage(final @NotNull Player p, final String slash, final String schemAlias) {
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

	private void searchFolderUsage(final @NotNull Player p, final String slash, final String schemAlias) {
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
