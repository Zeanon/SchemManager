package de.zeanon.schemmanager.worldeditversion.listener;

import de.zeanon.schemmanager.worldeditversion.commands.DeleteFolder;
import de.zeanon.schemmanager.worldeditversion.commands.Folder;
import de.zeanon.schemmanager.worldeditversion.commands.Rename;
import de.zeanon.schemmanager.worldeditversion.commands.Save;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import de.zeanon.schemmanager.worldeditversion.commands.Delete;
import de.zeanon.schemmanager.worldeditversion.commands.Help;
import de.zeanon.schemmanager.worldeditversion.commands.List;
import de.zeanon.schemmanager.worldeditversion.commands.RenameFolder;
import de.zeanon.schemmanager.worldeditversion.commands.Search;
import de.zeanon.schemmanager.worldeditversion.commands.SearchFolder;
import de.zeanon.schemmanager.worldeditversion.helper.Helper;
import de.zeanon.schemmanager.worldeditversion.WorldEditVersionMain;
import net.md_5.bungee.api.ChatColor;

import java.util.Objects;

public class CommandListener implements Listener {

	private boolean worldguardEnabled = false;
	private Plugin plugin;

	public CommandListener(Plugin plugin) {
		this.plugin = plugin;
		if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null && Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("WorldGuard")).isEnabled()) {
			this.worldguardEnabled = true;
		}
	}

	@SuppressWarnings("Duplicates")
    @EventHandler(priority = EventPriority.HIGHEST)
	public boolean onCommand(PlayerCommandPreprocessEvent event) {
		String message = event.getMessage().replaceAll("worldedit:", "/");
		Player p;
		String[] args;

		if (message.toLowerCase().startsWith("/schem") || message.toLowerCase().startsWith("/schematic")
				|| message.toLowerCase().startsWith("//schem") || message.toLowerCase().startsWith("//schematic")) {
			p = event.getPlayer();
			args = event.getMessage().split(" ");
			String slash;
			if (!message.toLowerCase().startsWith("/schem") && !message.toLowerCase().startsWith("/schematic")) {
				slash = "//";
			} else {
				slash = "/";
			}


			if (args.length == 1) {
				event.setCancelled(true);
				return Help.onHelp(p, slash);
			}

			if (!p.hasPermission("worldedit.schematic.delete")) {
				return false;
			} else if ((args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("del"))) {
				event.setCancelled(true);
				if (args.length <= 4) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD
								+ "filename" + ChatColor.YELLOW + ">");
                        return deleteUsage(p, slash);
                    } else if (!Helper.checkDeleteRequest(p, args[2])
							&& !args[3].equalsIgnoreCase("confirm") && !args[3].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
                        return deleteUsage(p, slash);
                    } else {
						return Delete.onDelete(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
                    return deleteUsage(p, slash);
                }
			} else if ((args[1].equalsIgnoreCase("deletefolder") || args[1].equalsIgnoreCase("delfolder")) && p.hasPermission("worldedit.schematic.delete")) {
				event.setCancelled(true);
				if (args.length <= 4) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GREEN
								+ "filename" + ChatColor.YELLOW + ">");
                        return deleteFolderUsage(p, slash);
                    } else if (args.length == 4 && !Helper.checkDeleteFolderRequest(p, args[2])
							&& !args[3].equalsIgnoreCase("confirm") && !args[3].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
                        return deleteFolderUsage(p, slash);
                    } else {
						return DeleteFolder.onDeleteFolder(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
                    return deleteFolderUsage(p, slash);
                }
			} else if (args[1].equalsIgnoreCase("rename") && p.hasPermission("worldedit.schematic.save")) {
				event.setCancelled(true);
				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD
								+ "filename" + ChatColor.YELLOW + ">");
                        return renameUsage(p, slash);
                    } else if (args.length == 5 && !Helper.checkRenameRequest(p, args[2])
							&& !args[3].equalsIgnoreCase("confirm") && !args[3].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
                        return renameUsage(p, slash);
                    } else {
						return Rename.onRename(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
                    return renameUsage(p, slash);
                }
			} else if (args[1].equalsIgnoreCase("renamefolder") && p.hasPermission("worldedit.schematic.save")) {
				event.setCancelled(true);
				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GREEN
								+ "filename" + ChatColor.YELLOW + ">");
                        return renameFolderUsage(p, slash);
                    } else if (args.length == 5 && !args[3].equalsIgnoreCase("confirm") && !args[3].equalsIgnoreCase("deny") && !Helper.checkRenameFolderRequest(p, args[2])) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
                        return renameFolderUsage(p, slash);
                    } else {
						return RenameFolder.onRenameFolder(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
                    return renameFolderUsage(p, slash);
                }
			} else if (args[1].equalsIgnoreCase("load") && p.hasPermission("worldedit.schematic.load")) {
				if (args.length < 3) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD
							+ "filename" + ChatColor.YELLOW + ">");
                    return loadUsage(p, slash);
                } else if (args.length > 3) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "Too many arguments.");
                    return loadUsage(p, slash);
                } else {
					return true;
				}
			} else if (args[1].equalsIgnoreCase("save") && p.hasPermission("worldedit.schematic.save")) {
				if (!Helper.getBoolean("Save Function Override")) {
					//TODO
					return true;
				} else if (args.length > 2 && args.length < 5 && args[2].equals("-f")) {
					if (args.length == 3) {
						event.setCancelled(true);
						p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD
								+ "filename" + ChatColor.YELLOW + ">");
                        return saveUsage(p, slash);
                    } else {
						return true;
					}
				} else {
					event.setCancelled(true);
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD
								+ "filename" + ChatColor.YELLOW + ">");
                        return saveUsage(p, slash);
                    } else if (args.length > 4) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
                        return saveUsage(p, slash);
                    } else {
						if (!Helper.checkDeleteRequest(p, args[2]) && args.length == 4 && !args[3].equalsIgnoreCase("confirm") && !args[3].equalsIgnoreCase("deny")) {
							p.sendMessage(ChatColor.RED + "Too many arguments.");
                            return saveUsage(p, slash);
                        } else {
							return Save.onSave(p, args);
						}
					}
				}
			} else if (args[1].equalsIgnoreCase("list") && p.hasPermission("worldedit.schematic.list")) {
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
                        return listUsage(p, slash);
                    } else {
						return List.onList(p, args, deep);
					}
				} else {
                    return listUsage(p, slash);
                }
			} else if (args[1].equalsIgnoreCase("folder") && p.hasPermission("worldedit.schematic.list")) {
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
                        return folderUsage(p, slash);
                    } else {
						return Folder.onFolder(p, args, deep);
					}
				} else {
                    return folderUsage(p, slash);
                }
			} else if (args[1].equalsIgnoreCase("search") && p.hasPermission("worldedit.schematic.list")) {
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
						p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD
								+ "filename" + ChatColor.YELLOW + ">");
                        return searchUsage(p, slash);
                    } else if (args.length == 5 && (StringUtils.isNumeric(args[2]) || StringUtils.isNumeric(args[3]) || !StringUtils.isNumeric(args[4]))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
                        return searchUsage(p, slash);
                    } else {
						return Search.onSearch(p, args, deep);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
                    return searchUsage(p, slash);
                }
			} else if (args[1].equalsIgnoreCase("searchfolder") && p.hasPermission("worldedit.schematic.list")) {
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
						p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD
								+ "filename" + ChatColor.YELLOW + ">");
                        return searchFolderUsage(p, slash);
                    } else if (args.length == 5 && (StringUtils.isNumeric(args[2]) || StringUtils.isNumeric(args[3]) || !StringUtils.isNumeric(args[4]))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
                        return searchFolderUsage(p, slash);
                    } else {
						return SearchFolder.onSearchFolder(p, args, deep);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
                    return searchFolderUsage(p, slash);
                }
			} else if (args[1].equalsIgnoreCase("update") && p.hasPermission("schemmanager.update")) {
				event.setCancelled(true);
				if (args.length == 2) {
					Helper.sendBooleanMessage(ChatColor.RED + "Do you really want to update?", "//schem update confirm",
							"//schem update deny", p);
					Helper.addUpdateRequest(p);
					return true;
				} else if (args.length == 3 && (args[2].equalsIgnoreCase("confirm") || args[2].equalsIgnoreCase("deny"))) {
					if (args[2].equalsIgnoreCase("confirm") && Helper.checkUpdateRequest(p)) {
						Helper.removeUpdateRequest(p);
						return Helper.update(p);
					} else if (args[2].equalsIgnoreCase("deny") && Helper.checkUpdateRequest(p)) {
						Helper.removeUpdateRequest(p);
						p.sendMessage(ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " will not be updated.");
						return true;
					} else {
						return true;
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
							ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "update", ChatColor.DARK_GREEN + ""
									+ ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "!!UPDATE BABY!!",
							slash + "schem update", p);
					return true;
				}
			} else if (args[1].equalsIgnoreCase("help")) {
				event.setCancelled(true);
				if (args.length == 2) {
					return Help.onHelp(p, slash);
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
							ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "help", ChatColor.LIGHT_PURPLE + ""
									+ ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "PLS HELP ME",
							slash + "schem help", p);
					return true;
				}
			} else if (args[1].equalsIgnoreCase("disable") && p.hasPermission("schemmanager.disable")) {
				event.setCancelled(true);
				if (args.length == 2) {
					Helper.sendBooleanMessage(ChatColor.RED + "Do you really want to disable " + ChatColor.DARK_PURPLE
									+ "SchemManager" + ChatColor.RED + "? ", "//schem disable confirm", "//schem disable deny",
							p);
					Helper.addDisableRequest(p);
					return true;
				} else if (args.length == 3 && (args[2].equalsIgnoreCase("confirm") || args[2].equalsIgnoreCase("deny"))) {
					if (args[2].equalsIgnoreCase("confirm") && Helper.checkDisableRequest(p)) {
						p.sendMessage(ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " is being disabled.");
						WorldEditVersionMain.disable();
						return true;
					} else if (args[2].equalsIgnoreCase("deny") && Helper.checkDisableRequest(p)) {
						Helper.removeDisableRequest(p);
						p.sendMessage(
								ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " will not be disabled.");
						return true;
					} else {
						return true;
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
							ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "disable", ChatColor.DARK_RED + ""
									+ ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "PLS DON'T D;",
							slash + "schem disable", p);
					return true;
				}
			} else {
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
		} else if (message.toLowerCase().startsWith("/stoplag") && this.worldguardEnabled && Helper.getBoolean("Stoplag Override")) {
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

    @SuppressWarnings("Duplicates")
    private boolean searchFolderUsage(Player p, String slash) {
        Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "searchfolder " + ChatColor.YELLOW
                        + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] ["
                        + ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] <" + ChatColor.GOLD
                        + "filename" + ChatColor.YELLOW + "> [" + ChatColor.DARK_PURPLE + "page"
                        + ChatColor.YELLOW + "]",
                ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA
                        + "searchfolder " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d"
                        + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "folder" + ChatColor.YELLOW
                        + "] " + ChatColor.GOLD + "example" + ChatColor.YELLOW + " ["
                        + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
                slash + "schem searchfolder ", p);
        return true;
    }

    @SuppressWarnings("Duplicates")
    private boolean searchUsage(Player p, String slash) {
        Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "search " + ChatColor.YELLOW + "["
                        + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN
                        + "folder" + ChatColor.YELLOW + "] <" + ChatColor.GOLD + "filename"
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

    @SuppressWarnings("Duplicates")
    private boolean folderUsage(Player p, String slash) {
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

    @SuppressWarnings("Duplicates")
    private boolean listUsage(Player p, String slash) {
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

    private boolean saveUsage(Player p, String slash) {
        Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "save " + ChatColor.YELLOW + "<"
                        + ChatColor.GOLD + "filename" + ChatColor.YELLOW + ">",
                ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "save "
                        + ChatColor.GOLD + "example",
                slash + "schem save ", p);
        return true;
    }

    private boolean loadUsage(Player p, String slash) {
        Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "load " + ChatColor.YELLOW + "<"
                        + ChatColor.GOLD + "filename" + ChatColor.YELLOW + ">",
                ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "load "
                        + ChatColor.GOLD + "example",
                slash + "schem load ", p);
        return true;
    }

    private boolean renameFolderUsage(Player p, String slash) {
        Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "renamefolder " + ChatColor.YELLOW
                        + "<" + ChatColor.GREEN + "filename" + ChatColor.YELLOW + "> <" + ChatColor.GREEN + "newname" + ChatColor.YELLOW + ">",
                ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA
                        + "renamefolder " + ChatColor.GREEN + "example newname",
                slash + "schem renamefolder ", p);
        return true;
    }

    private boolean renameUsage(Player p, String slash) {
        Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "rename " + ChatColor.YELLOW + "<"
                        + ChatColor.GOLD + "filename" + ChatColor.YELLOW + "> <" + ChatColor.GOLD + "newname" + ChatColor.YELLOW + ">",
                ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "rename "
                        + ChatColor.GOLD + "example newname",
                slash + "schem rename ", p);
        return true;
    }

    private boolean deleteFolderUsage(Player p, String slash) {
        Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "deletefolder " + ChatColor.YELLOW
                        + "<" + ChatColor.GREEN + "filename" + ChatColor.YELLOW + ">",
                ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA
                        + "deletefolder " + ChatColor.GREEN + "example",
                slash + "schem deletefolder ", p);
        return true;
    }

    private boolean deleteUsage(Player p, String slash) {
        Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "delete " + ChatColor.YELLOW + "<"
                        + ChatColor.GOLD + "filename" + ChatColor.YELLOW + ">",
                ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + "schem " + ChatColor.AQUA + "delete "
                        + ChatColor.GOLD + "example",
                slash + "schem delete ", p);
        return true;
    }


    @EventHandler
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


	@EventHandler
	public void onPluginDisable(PluginDisableEvent e) {
		if (e.getPlugin() == Bukkit.getPluginManager().getPlugin("WorldEdit")) {
			if (Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit") != null && Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit")).isEnabled()) {
				WorldEditVersionMain.disable();
				Bukkit.getPluginManager().enablePlugin(this.plugin);
				return;
			} else {
				System.out.println("[" + this.plugin.getName() + "] >> disabling Plugin, it needs FastAsyncWorldEdit or WorldEdit to work");
				WorldEditVersionMain.disable();
				return;
			}
		}
		if (e.getPlugin() == Bukkit.getPluginManager().getPlugin("WorldGuard")) {
			this.worldguardEnabled = false;
		}
	}

	@EventHandler
	public void onPluginEnable(PluginEnableEvent e) {
		if (e.getPlugin() == Bukkit.getPluginManager().getPlugin("WorldGuard")) {
			this.worldguardEnabled = true;
		}
	}
}