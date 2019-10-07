package de.zeanon.schemmanager.worldeditversion.listener;

import de.zeanon.schemmanager.globalutils.ConfigUtils;
import de.zeanon.schemmanager.globalutils.MessageUtils;
import de.zeanon.schemmanager.worldeditversion.commands.*;
import de.zeanon.schemmanager.worldeditversion.utils.WorldEditVersionRequestUtils;
import de.zeanon.schemmanager.worldeditversion.utils.WorldeditVersionMessageUtils;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {

    @SuppressWarnings("Duplicates")
    @EventHandler(priority = EventPriority.HIGH)
    public boolean onCommand(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        String[] args = event.getMessage().replaceAll("worldedit:", "/").split(" ");

        if (args[0].equalsIgnoreCase("/schem") || args[0].equalsIgnoreCase("/schematic")
                || args[0].equalsIgnoreCase("//schem") || args[0].equalsIgnoreCase("//schematic")) {

            String slash = args[0].equalsIgnoreCase("//schem") || args[0].equalsIgnoreCase("//schematic") ? "//" : "/";
            String schemAlias = args[0].equalsIgnoreCase("/schematic") || args[0].equalsIgnoreCase("//schematic") ? "schematic" : "schem";


            if (args.length == 1) {
                event.setCancelled(true);
                return Help.onHelp(p, slash, schemAlias);
            } else if ((args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("del") && p.hasPermission("worldedit.schematic.delete"))) {
                event.setCancelled(true);
                if (args.length <= 5) {
                    if (args.length < 3) {
                        p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD
                                + "filename" + ChatColor.YELLOW + ">");
                        return deleteUsage(p, slash, schemAlias);
                    } else if (args[2].contains("./")) {
                        p.sendMessage(ChatColor.RED + "File \'" + args[2] + "\'resolution error: Path is not allowed.");
                        return deleteUsage(p, slash, schemAlias);
                    } else if (args.length == 4 && !WorldEditVersionRequestUtils.checkDeleteFolderRequest(p, args[2])
                            && !args[3].equalsIgnoreCase("confirm") && !args[3].equalsIgnoreCase("deny")) {
                        p.sendMessage(ChatColor.RED + "Too many arguments.");
                        return deleteUsage(p, slash, schemAlias);
                    } else {
                        return Delete.onDelete(p, args);
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Too many arguments.");
                    return deleteUsage(p, slash, schemAlias);
                }
            } else if ((args[1].equalsIgnoreCase("deletefolder") || args[1].equalsIgnoreCase("delfolder")) && p.hasPermission("worldedit.schematic.delete")) {
                event.setCancelled(true);
                if (args.length <= 5) {
                    if (args.length < 3) {
                        p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GREEN
                                + "filename" + ChatColor.YELLOW + ">");
                        return deleteFolderUsage(p, slash, schemAlias);
                    } else if (args[2].contains("./")) {
                        p.sendMessage(ChatColor.RED + "File \'" + args[2] + "\'resolution error: Path is not allowed.");
                        return deleteFolderUsage(p, slash, schemAlias);
                    } else if (args.length == 4 && !WorldEditVersionRequestUtils.checkDeleteFolderRequest(p, args[2])
                            && !args[3].equalsIgnoreCase("confirm") && !args[3].equalsIgnoreCase("deny")) {
                        p.sendMessage(ChatColor.RED + "Too many arguments.");
                        return deleteFolderUsage(p, slash, schemAlias);
                    } else {
                        return DeleteFolder.onDeleteFolder(p, args);
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Too many arguments.");
                    return deleteFolderUsage(p, slash, schemAlias);
                }
            } else if (args[1].equalsIgnoreCase("rename") && p.hasPermission("worldedit.schematic.save")) {
                event.setCancelled(true);
                if (args.length <= 5) {
                    if (args.length < 3) {
                        p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD
                                + "filename" + ChatColor.YELLOW + ">");
                        return renameUsage(p, slash, schemAlias);
                    } else if (args[2].contains("./") || args.length >= 4 && args[3].contains("./")) {
                        String name = args[2].contains("./") ? args[2] : args[3];
                        p.sendMessage(ChatColor.RED + "File \'" + name + "\'resolution error: Path is not allowed.");
                        return renameUsage(p, slash, schemAlias);
                    } else if (args.length == 5 && !WorldEditVersionRequestUtils.checkRenameRequest(p, args[2])
                            && !args[3].equalsIgnoreCase("confirm") && !args[3].equalsIgnoreCase("deny")) {
                        p.sendMessage(ChatColor.RED + "Too many arguments.");
                        return renameUsage(p, slash, schemAlias);
                    } else {
                        return Rename.onRename(p, args);
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Too many arguments.");
                    return renameUsage(p, slash, schemAlias);
                }
            } else if (args[1].equalsIgnoreCase("renamefolder") && p.hasPermission("worldedit.schematic.save")) {
                event.setCancelled(true);
                if (args.length <= 5) {
                    if (args.length < 3) {
                        p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GREEN
                                + "filename" + ChatColor.YELLOW + ">");
                        return renameFolderUsage(p, slash, schemAlias);
                    } else if (args[2].contains("./") || args.length >= 4 && args[3].contains("./")) {
                        String name = args[2].contains("./") ? args[2] : args[3];
                        p.sendMessage(ChatColor.RED + "File \'" + name + "\'resolution error: Path is not allowed.");
                        return renameFolderUsage(p, slash, schemAlias);
                    } else if (args.length == 5 && !args[4].equalsIgnoreCase("confirm") && !args[4].equalsIgnoreCase("deny") && !WorldEditVersionRequestUtils.checkRenameFolderRequest(p, args[2])) {
                        p.sendMessage(ChatColor.RED + "Too many arguments.");
                        return renameFolderUsage(p, slash, schemAlias);
                    } else {
                        return RenameFolder.onRenameFolder(p, args);
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Too many arguments.");
                    return renameFolderUsage(p, slash, schemAlias);
                }
            } else if (args[1].equalsIgnoreCase("load") && p.hasPermission("worldedit.schematic.load")) {
                if (args.length < 3) {
                    event.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD
                            + "filename" + ChatColor.YELLOW + ">");
                    return loadUsage(p, slash, schemAlias);
                } else if (args[2].contains("./")) {
                    event.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "File \'" + args[2] + "\'resolution error: Path is not allowed.");
                    return loadUsage(p, slash, schemAlias);
                } else if (args.length > 4) {
                    event.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "Too many arguments.");
                    return loadUsage(p, slash, schemAlias);
                } else if (args.length > 3 && !ConfigUtils.getStringList("File Extensions").contains(args[3])) {
                    event.setCancelled(true);
                    p.sendMessage(ChatColor.LIGHT_PURPLE + args[3] + ChatColor.RED + " is no valid file format.");
                    return Help.onFormats(p, true);
                } else {
                    return true;
                }
            } else if (args[1].equalsIgnoreCase("save") && p.hasPermission("worldedit.schematic.save")) {
                if (!ConfigUtils.getBoolean("Save Function Override")) {
                    event.setCancelled(true);
                    if (args.length < 3) {
                        p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD
                                + "filename" + ChatColor.YELLOW + ">");
                        return defaultSaveUsage(p, slash, schemAlias);
                    } else if (args[2].contains("./")) {
                        p.sendMessage(ChatColor.RED + "File \'" + args[2] + "\'resolution error: Path is not allowed.");
                        return defaultSaveUsage(p, slash, schemAlias);
                    } else if (args.length > 4 && !args[2].equalsIgnoreCase("-f")) {
                        p.sendMessage(ChatColor.RED + "Too many arguments.");
                        return defaultSaveUsage(p, slash, schemAlias);
                    } else {
                        return true;
                    }
                } else if (args.length > 2 && args.length < 5 && args[2].equalsIgnoreCase("-f")) {
                    if (args.length == 3) {
                        event.setCancelled(true);
                        p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD
                                + "filename" + ChatColor.YELLOW + ">");
                        return saveUsage(p, slash, schemAlias);
                    } else {
                        return true;
                    }
                } else {
                    event.setCancelled(true);
                    if (args.length < 3) {
                        p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD
                                + "filename" + ChatColor.YELLOW + ">");
                        return saveUsage(p, slash, schemAlias);
                    } else if (args[2].contains("./")) {
                        p.sendMessage(ChatColor.RED + "File \'" + args[2] + "\'resolution error: Path is not allowed.");
                        return saveUsage(p, slash, schemAlias);
                    } else if (args.length > 4) {
                        p.sendMessage(ChatColor.RED + "Too many arguments.");
                        return saveUsage(p, slash, schemAlias);
                    } else if (args.length == 4 && !WorldEditVersionRequestUtils.checkOverWriteRequest(p, args[2]) && !args[3].equalsIgnoreCase("confirm") && !args[3].equalsIgnoreCase("deny")) {
                        p.sendMessage(ChatColor.RED + "Too many arguments.");
                        return saveUsage(p, slash, schemAlias);
                    } else {
                        return Save.onSave(p, args);
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
                        p.sendMessage(ChatColor.RED + "Too many arguments.");
                        return listUsage(p, slash, schemAlias);
                    } else if (args.length >= 3 && args[2].contains("./")) {
                        p.sendMessage(ChatColor.RED + "File \'" + args[2] + "\'resolution error: Path is not allowed.");
                        return listUsage(p, slash, schemAlias);
                    } else {
                        return List.onList(p, args, deep);
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Too many arguments.");
                    return listUsage(p, slash, schemAlias);
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
                        p.sendMessage(ChatColor.RED + "Too many arguments.");
                        return folderUsage(p, slash, schemAlias);
                    } else if (args.length >= 3 && args[2].contains("./")) {
                        p.sendMessage(ChatColor.RED + "File \'" + args[2] + "\'resolution error: Path is not allowed.");
                        return folderUsage(p, slash, schemAlias);
                    } else {
                        return Folder.onFolder(p, args, deep);
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Too many arguments.");
                    return folderUsage(p, slash, schemAlias);
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
                        return searchUsage(p, slash, schemAlias);
                    } else if (args[2].contains("./")) {
                        p.sendMessage(ChatColor.RED + "File \'" + args[2] + "\'resolution error: Path is not allowed.");
                        return searchUsage(p, slash, schemAlias);
                    } else if (args.length == 5 && (StringUtils.isNumeric(args[2]) || StringUtils.isNumeric(args[3]) || !StringUtils.isNumeric(args[4]))) {
                        p.sendMessage(ChatColor.RED + "Too many arguments.");
                        return searchUsage(p, slash, schemAlias);
                    } else {
                        return Search.onSearch(p, args, deep);
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Too many arguments.");
                    return searchUsage(p, slash, schemAlias);
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
                        return searchFolderUsage(p, slash, schemAlias);
                    } else if (args[2].contains("./")) {
                        p.sendMessage(ChatColor.RED + "File \'" + args[2] + "\'resolution error: Path is not allowed.");
                        return searchUsage(p, slash, schemAlias);
                    } else if (args.length == 5 && (StringUtils.isNumeric(args[2]) || StringUtils.isNumeric(args[3]) || !StringUtils.isNumeric(args[4]))) {
                        p.sendMessage(ChatColor.RED + "Too many arguments.");
                        return searchFolderUsage(p, slash, schemAlias);
                    } else {
                        return SearchFolder.onSearchFolder(p, args, deep);
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Too many arguments.");
                    return searchFolderUsage(p, slash, schemAlias);
                }
            } else if (args[1].equalsIgnoreCase("help")) {
                event.setCancelled(true);
                if (args.length == 2) {
                    return Help.onHelp(p, slash, schemAlias);
                } else {
                    p.sendMessage(ChatColor.RED + "Too many arguments.");
                    MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
                            ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " help", ChatColor.LIGHT_PURPLE + ""
                                    + ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "PLS HELP ME",
                            slash + schemAlias + " help", p);
                    return true;
                }
            } else if (args[1].equalsIgnoreCase("formats")) {
                event.setCancelled(true);
                if (args.length == 2) {
                    return Help.onFormats(p, false);
                } else {
                    p.sendMessage(ChatColor.RED + "Too many arguments.");
                    MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
                            ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " formats", ChatColor.DARK_BLUE + ""
                                    + ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "There are different formats? :O",
                            slash + schemAlias + " formats", p);
                    return true;
                }
            } else {
                event.setCancelled(true);
                p.sendMessage(ChatColor.RED + "Invalid sub-command '" + ChatColor.GOLD + "" + args[1] + ChatColor.RED
                        + "'. Options: " + ChatColor.GOLD + "help" + ChatColor.RED + ", " + ChatColor.GOLD + "load"
                        + ChatColor.RED + ", " + ChatColor.GOLD + "formats" + ChatColor.RED + ", " + ChatColor.GOLD + "save"
                        + ChatColor.RED + ", " + ChatColor.GOLD + "rename" + ChatColor.RED + ", " + ChatColor.GOLD
                        + "renamefolder" + ChatColor.RED + ", " + ChatColor.GOLD + "delete" + ChatColor.RED + ", "
                        + ChatColor.GOLD + "deletefolder" + ChatColor.RED + ", " + ChatColor.GOLD + "list"
                        + ChatColor.RED + ", " + ChatColor.GOLD + "folder" + ChatColor.RED + ", " + ChatColor.GOLD
                        + "search" + ChatColor.RED + ", " + ChatColor.GOLD + "searchfolder");
                WorldeditVersionMessageUtils.sendInvalidSubCommand(p, slash, schemAlias);
                return true;
            }
        } else if (args[0].equalsIgnoreCase("/stoplag") && EventListener.worldguardEnabled && ConfigUtils.getBoolean("Stoplag Override")) {
            if (args.length == 1 || (!args[1].equalsIgnoreCase("confirm") && !args[1].equalsIgnoreCase("-c"))) {
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


    private boolean searchFolderUsage(Player p, String slash, String schemAlias) {
        MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " searchfolder " + ChatColor.YELLOW
                        + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] ["
                        + ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] <" + ChatColor.GOLD
                        + "filename" + ChatColor.YELLOW + "> [" + ChatColor.DARK_PURPLE + "page"
                        + ChatColor.YELLOW + "]",
                ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA
                        + " searchfolder " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d"
                        + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "folder" + ChatColor.YELLOW
                        + "] " + ChatColor.GOLD + "example" + ChatColor.YELLOW + " ["
                        + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
                slash + schemAlias + " searchfolder ", p);
        return true;
    }

    private boolean searchUsage(Player p, String slash, String schemAlias) {
        MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " search " + ChatColor.YELLOW + "["
                        + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN
                        + "folder" + ChatColor.YELLOW + "] <" + ChatColor.GOLD + "filename"
                        + ChatColor.YELLOW + "> [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW
                        + "]",
                ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " search "
                        + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW
                        + "] [" + ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] " + ChatColor.GOLD
                        + "example" + ChatColor.YELLOW + " [" + ChatColor.DARK_PURPLE + "page"
                        + ChatColor.YELLOW + "]",
                slash + schemAlias + " search ", p);
        return true;
    }

    private boolean folderUsage(Player p, String slash, String schemAlias) {
        MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " folder " + ChatColor.YELLOW + "["
                        + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN
                        + "folder" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page"
                        + ChatColor.YELLOW + "]",
                ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " folder "
                        + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW
                        + "] [" + ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] ["
                        + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
                slash + schemAlias + " folder ", p);
        return true;
    }

    private boolean listUsage(Player p, String slash, String schemAlias) {
        MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " list " + ChatColor.YELLOW + "["
                        + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN
                        + "folder" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page"
                        + ChatColor.YELLOW + "]",
                ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " list "
                        + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW
                        + "] [" + ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] ["
                        + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]",
                slash + schemAlias + " list ", p);
        return true;
    }

    private boolean saveUsage(Player p, String slash, String schemAlias) {
        MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " save " + ChatColor.YELLOW + "<"
                        + ChatColor.GOLD + "filename" + ChatColor.YELLOW + ">",
                ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " save "
                        + ChatColor.GOLD + "example",
                slash + schemAlias + " save ", p);
        return true;
    }

    private boolean defaultSaveUsage(Player p, String slash, String schemAlias) {
        MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " save "
                        + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-f" + ChatColor.YELLOW + "] <"
                        + ChatColor.GOLD + "filename" + ChatColor.YELLOW + ">",
                ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " save "
                        + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-f" + ChatColor.YELLOW + "]"
                        + ChatColor.GOLD + "example",
                slash + schemAlias + " save ", p);
        return true;
    }

    private boolean loadUsage(Player p, String slash, String schemAlias) {
        MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " load " + ChatColor.YELLOW + "<"
                        + ChatColor.GOLD + "filename" + ChatColor.YELLOW + "> ["
                        + ChatColor.DARK_PURPLE + "format" + ChatColor.YELLOW + "]",
                ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " load "
                        + ChatColor.GOLD + "example " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "format" + ChatColor.YELLOW
                        + "]",
                slash + schemAlias + " load ", p);
        return true;
    }

    private boolean renameFolderUsage(Player p, String slash, String schemAlias) {
        MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " renamefolder " + ChatColor.YELLOW
                        + "<" + ChatColor.GREEN + "filename" + ChatColor.YELLOW + "> <" + ChatColor.GREEN + "newname" + ChatColor.YELLOW + ">",
                ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA
                        + " renamefolder " + ChatColor.GREEN + "example newname",
                slash + schemAlias + " renamefolder ", p);
        return true;
    }

    private boolean renameUsage(Player p, String slash, String schemAlias) {
        MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " rename " + ChatColor.YELLOW + "<"
                        + ChatColor.GOLD + "filename" + ChatColor.YELLOW + "> <" + ChatColor.GOLD + "newname" + ChatColor.YELLOW + ">",
                ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " rename "
                        + ChatColor.GOLD + "example newname",
                slash + schemAlias + " rename ", p);
        return true;
    }

    private boolean deleteFolderUsage(Player p, String slash, String schemAlias) {
        MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " deletefolder " + ChatColor.YELLOW
                        + "<" + ChatColor.GREEN + "filename" + ChatColor.YELLOW + ">",
                ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA
                        + " deletefolder " + ChatColor.GREEN + "example",
                slash + schemAlias + " deletefolder ", p);
        return true;
    }

    private boolean deleteUsage(Player p, String slash, String schemAlias) {
        MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " delete " + ChatColor.YELLOW + "<"
                        + ChatColor.GOLD + "filename" + ChatColor.YELLOW + ">",
                ChatColor.RED + "e.g. " + ChatColor.GRAY + slash + schemAlias + ChatColor.AQUA + " delete "
                        + ChatColor.GOLD + "example",
                slash + schemAlias + " delete ", p);
        return true;
    }
}