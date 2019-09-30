package de.zeanon.schemmanager.worldeditversion.helper;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import de.zeanon.schemmanager.worldeditversion.WorldEditVersionMain;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class Helper {

    public static WorldEditPlugin we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
    private static Plugin plugin;
    private static String pluginFolderPath;
    private static String slash = "/";
    private static String schemFolderPath;
    private static ArrayList<String> disableRequests = new ArrayList<>();
    private static ArrayList<String> updateRequests = new ArrayList<>();
    private static HashMap<String, String> deleteRequests = new HashMap<>();
    private static HashMap<String, String> deleteFolderRequests = new HashMap<>();
    private static HashMap<String, String> renameRequests = new HashMap<>();
    private static HashMap<String, String> renameFolderRequests = new HashMap<>();
    private static HashMap<String, String> overwriteRequests = new HashMap<>();


    public Helper(Plugin plugin) {
        Helper.plugin = plugin;
        if (plugin.getDataFolder().getAbsolutePath().contains("/")) {
            slash = "/";
        }
        if (plugin.getDataFolder().getAbsolutePath().contains("\\")) {
            slash = "\\\\";
        }
        String[] parts = plugin.getDataFolder().getAbsolutePath().split(slash);
        StringBuilder pathBuilder = new StringBuilder(parts[0] + slash);
        for (int i = 1; i < parts.length - 1; i++) {
            pathBuilder.append(parts[i]).append(slash);
        }
        pluginFolderPath = pathBuilder.toString();
        schemFolderPath = getInitialSchemPath();
    }


    public static void sendCommandMessage(String message, String commandMessage, String hoverMessage, String command, Player target) {
        new TextComponent();
        TextComponent localMessage = new TextComponent(TextComponent.fromLegacyText(message));
        TextComponent commandPart = new TextComponent(TextComponent.fromLegacyText(commandMessage));
        commandPart.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        commandPart.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMessage).create()));
        localMessage.addExtra(commandPart);
        target.spigot().sendMessage(localMessage);
    }


    public static void sendBooleanMessage(String message, String commandYes, String commandNo, Player target) {
        new TextComponent();
        TextComponent localMessage = new TextComponent(TextComponent.fromLegacyText(message));
        TextComponent seperator = new TextComponent(TextComponent.fromLegacyText(ChatColor.BLACK + " " + ChatColor.BOLD + "| "));
        TextComponent commandPartYes = new TextComponent(TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "[Y]"));
        TextComponent commandPartNo = new TextComponent(TextComponent.fromLegacyText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[N]"));
        commandPartYes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandYes));
        commandPartYes.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "[YES]"))).create()));
        commandPartNo.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandNo));
        commandPartNo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[NO]"))).create()));
        localMessage.addExtra(" ");
        localMessage.addExtra(commandPartYes);
        localMessage.addExtra(seperator);
        localMessage.addExtra(commandPartNo);
        target.spigot().sendMessage(localMessage);
    }


    public static void sendScrollMessage(String commandForward, String commandBackward, String messageForward, String messageBackward, Player target, ChatColor buttonColor) {
        TextComponent localMessage = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "=== "));
        TextComponent commandPartBackward = new TextComponent(TextComponent.fromLegacyText(buttonColor + "[<<<]"));
        TextComponent commandPartForward = new TextComponent(TextComponent.fromLegacyText(buttonColor + "[>>>]"));
        commandPartBackward.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandBackward));
        commandPartBackward.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(messageBackward))).create()));
        commandPartForward.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandForward));
        commandPartForward.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(messageForward))).create()));
        localMessage.addExtra(commandPartBackward);
        localMessage.addExtra(ChatColor.AQUA + " " + ChatColor.BOLD + "| ");
        localMessage.addExtra(commandPartForward);
        localMessage.addExtra(ChatColor.AQUA + " ===");
        target.spigot().sendMessage(localMessage);
    }


    @SuppressWarnings("Duplicates")
    public static void sendSuggestMessage(String message, String suggestMessage, String hoverMessage, String command, Player target) {
        TextComponent localMessage = new TextComponent(TextComponent.fromLegacyText(message));
        TextComponent suggestPart = new TextComponent(TextComponent.fromLegacyText(suggestMessage));
        suggestPart.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        suggestPart.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(hoverMessage))).create()));
        localMessage.addExtra(suggestPart);
        target.spigot().sendMessage(localMessage);
    }


    @SuppressWarnings("Duplicates")
    public static void sendHoverMessage(String message1, String message2, String message3, String hoverMessage, Player target) {
        TextComponent localMessage1 = new TextComponent(TextComponent.fromLegacyText(message1));
        TextComponent hoverPart = new TextComponent(TextComponent.fromLegacyText(message2));
        TextComponent localMessage2 = new TextComponent(TextComponent.fromLegacyText(message3));
        hoverPart.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(hoverMessage))).create()));
        localMessage1.addExtra(hoverPart);
        localMessage1.addExtra(localMessage2);
        target.spigot().sendMessage(localMessage1);
    }


    @SuppressWarnings("Duplicates")
    public static void sendInvalidSubCommand(Player target, String slash, String schemAlias) {
        TextComponent base = new TextComponent(TextComponent.fromLegacyText(ChatColor.RED + "Usage: "));
        TextComponent schem = new TextComponent(TextComponent.fromLegacyText(ChatColor.GRAY + slash + "schem"));
        TextComponent help = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "help"));
        TextComponent load = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "load"));
        TextComponent formats = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "formats"));
        TextComponent save = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "save"));
        TextComponent rename = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "rename"));
        TextComponent renamefolder = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "renamefolder"));
        TextComponent delete = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "delete"));
        TextComponent deletefolder = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "deletefolder"));
        TextComponent list = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "list"));
        TextComponent folder = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "folder"));
        TextComponent search = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "search"));
        TextComponent searchfolder = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "searchfolder"));
        schem.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " "));
        schem.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(ChatColor.RED + "e.g. " + ChatColor.GRAY + "" + slash + "schem"))).create()));
        help.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " help"));
        help.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(ChatColor.LIGHT_PURPLE + "" + ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "OMG PLS HELP ME"))).create()));
        load.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " load "));
        load.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(ChatColor.RED + "e.g. " + ChatColor.GRAY + "" + slash + schemAlias + " " + ChatColor.AQUA + "load" + ChatColor.GOLD + " example " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "format" + ChatColor.YELLOW + "]"))).create()));
        formats.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " formats"));
        formats.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "There are different formats? :O"))).create()));
        save.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " save "));
        save.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(ChatColor.RED + "e.g. " + ChatColor.GRAY + "" + slash + schemAlias + " " + ChatColor.AQUA + "save" + ChatColor.GOLD + " example"))).create()));
        rename.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " rename "));
        rename.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(ChatColor.RED + "e.g. " + ChatColor.GRAY + "" + slash + schemAlias + " " + ChatColor.AQUA + "rename" + ChatColor.GOLD + " example newname"))).create()));
        renamefolder.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " renamefolder "));
        renamefolder.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(ChatColor.RED + "e.g. " + ChatColor.GRAY + "" + slash + schemAlias + " " + ChatColor.AQUA + "renamefolder" + ChatColor.GREEN + " example newname"))).create()));
        delete.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " delete "));
        delete.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(ChatColor.RED + "e.g. " + ChatColor.GRAY + "" + slash + schemAlias + " " + ChatColor.AQUA + "delete" + ChatColor.GOLD + " example"))).create()));
        deletefolder.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " deletefolder "));
        deletefolder.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(ChatColor.RED + "e.g. " + ChatColor.GRAY + "" + slash + schemAlias + " " + ChatColor.AQUA + "deletefolder" + ChatColor.GREEN + " example"))).create()));
        list.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " list "));
        list.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(ChatColor.RED + "e.g. " + ChatColor.GRAY + "" + slash + schemAlias + " " + ChatColor.AQUA + "list " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]"))).create()));
        folder.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " folder "));
        folder.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(ChatColor.RED + "e.g. " + ChatColor.GRAY + "" + slash + schemAlias + " " + ChatColor.AQUA + "folder " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]"))).create()));
        search.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " search "));
        search.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(ChatColor.RED + "e.g. " + ChatColor.GRAY + "" + slash + schemAlias + " " + ChatColor.AQUA + "search " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]"))).create()));
        searchfolder.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " searchfolder "));
        searchfolder.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(ChatColor.RED + "e.g. " + ChatColor.GRAY + "" + slash + schemAlias + " " + ChatColor.AQUA + "searchfolder " + ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-d" + ChatColor.YELLOW + "] [" + ChatColor.GREEN + "folder" + ChatColor.YELLOW + "] [" + ChatColor.DARK_PURPLE + "page" + ChatColor.YELLOW + "]"))).create()));
        base.addExtra(schem);
        base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + " <")));
        base.addExtra(help);
        base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
        base.addExtra(load);
        base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
        base.addExtra(formats);
        base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
        base.addExtra(save);
        base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
        base.addExtra(rename);
        base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
        base.addExtra(renamefolder);
        base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
        base.addExtra(delete);
        base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
        base.addExtra(deletefolder);
        base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
        base.addExtra(list);
        base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
        base.addExtra(folder);
        base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
        base.addExtra(search);
        base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
        base.addExtra(searchfolder);
        base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + ">")));
        target.spigot().sendMessage(base);
    }


    public static void addDisableRequest(Player p) {
        disableRequests.add(p.getUniqueId().toString());
    }

    public static void removeDisableRequest(Player p) {
        disableRequests.remove(p.getUniqueId().toString());
    }

    public static boolean checkDisableRequest(Player p) {
        return disableRequests.contains(p.getUniqueId().toString());
    }


    public static void addUpdateRequest(Player p) {
        updateRequests.add(p.getUniqueId().toString());
    }

    public static void removeUpdateRequest(Player p) {
        updateRequests.remove(p.getUniqueId().toString());
    }

    public static boolean checkUpdateRequest(Player p) {
        return updateRequests.contains(p.getUniqueId().toString());
    }


    public static void addDeleteRequest(Player p, String name) {
        deleteRequests.put(p.getUniqueId().toString(), name);
    }

    public static void removeDeleteRequest(Player p) {
        deleteRequests.remove(p.getUniqueId().toString());
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
        deleteFolderRequests.remove(p.getUniqueId().toString());
    }

    public static boolean checkDeleteFolderRequest(Player p, String name) {
        if (deleteFolderRequests.containsKey(p.getUniqueId().toString())) {
            return deleteFolderRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
        } else {
            return false;
        }
    }


    public static void addRenameRequest(Player p, String name) {
        renameRequests.put(p.getUniqueId().toString(), name);
    }

    public static void removeRenameRequest(Player p) {
        renameRequests.remove(p.getUniqueId().toString());
    }

    public static boolean checkRenameRequest(Player p, String name) {
        if (renameRequests.containsKey(p.getUniqueId().toString())) {
            return renameRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
        } else {
            return false;
        }
    }


    public static void addRenameFolderRequest(Player p, String name) {
        renameFolderRequests.put(p.getUniqueId().toString(), name);
    }

    public static void removeRenameFolderRequest(Player p) {
        renameFolderRequests.remove(p.getUniqueId().toString());
    }

    public static boolean checkRenameFolderRequest(Player p, String name) {
        if (renameFolderRequests.containsKey(p.getUniqueId().toString())) {
            return renameFolderRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
        } else {
            return false;
        }
    }


    public static void addOverwriteRequest(Player p, String name) {
        overwriteRequests.put(p.getUniqueId().toString(), name);
    }

    public static void removeOverWriteRequest(Player p) {
        overwriteRequests.remove(p.getUniqueId().toString());
    }

    public static boolean checkOverWriteRequest(Player p, String name) {
        if (overwriteRequests.containsKey(p.getUniqueId().toString())) {
            return overwriteRequests.get(p.getUniqueId().toString()).equalsIgnoreCase(name);
        } else {
            return false;
        }
    }


    public static ArrayList<File> getFolders(File folder, Boolean deep) {
        ArrayList<File> files = new ArrayList<>();
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isDirectory()) {
                files.add(file);
                if (deep) {
                    files.addAll(getFolders(file, true));
                }
            }
        }
        return files;
    }


    public static String getSchemPath() {
        if (WorldEditVersionMain.config.hasNotChanged()) {
            return schemFolderPath;
        } else {
            return getInitialSchemPath();
        }
    }

    private static String getInitialSchemPath() {
        if (getString("WorldEdit Schematic-Path").equals("Default Schematic Path")) {
            schemFolderPath = pluginFolderPath + "WorldEdit" + slash + "schematics" + slash;
            return pluginFolderPath + "WorldEdit" + slash + "schematics" + slash;
        } else {
            if (slash.equals("\\\\")) {
                if (getString("WorldEdit Schematic-Path").endsWith(slash)) {
                    schemFolderPath = getString("WorldEdit Schematic-Path");
                    return getString("WorldEdit Schematic-Path");
                } else {
                    schemFolderPath = getString("WorldEdit Schematic-Path") + slash;
                    return getString("WorldEdit Schematic-Path") + slash;
                }
            } else {
                if (getString("WorldEdit Schematic-Path").endsWith(slash)) {
                    if (getString("WorldEdit Schematic-Path").startsWith(slash)) {
                        schemFolderPath = getString("WorldEdit Schematic-Path");
                        return getString("WorldEdit Schematic-Path");
                    } else {
                        schemFolderPath = slash + getString("WorldEdit Schematic-Path");
                        return slash + getString("WorldEdit Schematic-Path");
                    }
                } else {
                    if (getString("WorldEdit Schematic-Path").startsWith(slash)) {
                        schemFolderPath = getString("WorldEdit Schematic-Path") + slash;
                        return getString("WorldEdit Schematic-Path") + slash;
                    } else {
                        schemFolderPath = slash + getString("WorldEdit Schematic-Path") + slash;
                        return slash + getString("WorldEdit Schematic-Path") + slash;
                    }
                }
            }
        }
    }


    @SuppressWarnings("WeakerAccess")
    public static String getString(String path) {
        if (WorldEditVersionMain.config.contains(path)) {
            return WorldEditVersionMain.config.getString(path);
        } else {
            updateConfig(true);
            return (String) getDefaultValue(path);
        }
    }

    public static int getInt(String path) {
        if (WorldEditVersionMain.config.contains(path)) {
            return WorldEditVersionMain.config.getInt(path);
        } else {
            updateConfig(true);
            return (int) getDefaultValue(path);
        }
    }

    public static boolean getBoolean(String path) {
        if (WorldEditVersionMain.config.contains(path)) {
            return WorldEditVersionMain.config.getBoolean(path);
        } else {
            updateConfig(true);
            return (boolean) getDefaultValue(path);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<String> getStringList(String path) {
        if (WorldEditVersionMain.config.contains(path)) {
            return WorldEditVersionMain.config.getStringList(path);
        } else {
            updateConfig(true);
            return (List<String>) getDefaultValue(path);
        }
    }

    private static Object getDefaultValue(String path) {
        switch (path) {
            case "Space Lists":
                return true;
            case "Listmax":
                return 10;
            case "Save Function Override":
                return true;
            case "Stoplag Override":
                return true;
            case "Automatic Reload":
                return true;
            case "File Extensions":
                return Arrays.asList("schematic", "schem");
            case "WorldEdit Schematic-Path":
                return "Default Schematic Path";
            case "Plugin Version":
                return plugin.getDescription().getVersion();
            default:
                return null;
        }
    }


    public static ArrayList<File> getExistingFiles(String path) {
        ArrayList<File> files = new ArrayList<>();
        getStringList("File Extensions").iterator().forEachRemaining(extension -> files.add(new File(path + "." + extension)));
        files.iterator().forEachRemaining(file -> {
            if (!file.exists() || file.isDirectory()) files.remove(file);
        });
        return files;
    }


    public static boolean update(Player p) {
        p.sendMessage(ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " is updating...");
        String fileName;
        try {
            fileName = new File(WorldEditVersionMain.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath())
                    .getName();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            p.sendMessage(ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " could not be updated.");
            return false;
        }

        File file = new File(pluginFolderPath + fileName);
        if (writeToFile(file, "https://github.com/Zeanon/SchemManager/releases/latest/download/SchemManager.jar")) {
            p.sendMessage(ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " was updated successfully.");
            if (getBoolean("Automatic Reload")) {
                Bukkit.getServer().reload();
            }
            return true;
        } else {
            p.sendMessage(ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " could not be updated.");
            return false;
        }
    }


    public static boolean updateConfig(boolean force) {
        if (force || (!WorldEditVersionMain.config.contains("WorldEdit Schematic-Path") || !WorldEditVersionMain.config.contains("Listmax") || !WorldEditVersionMain.config.contains("Space Lists") || !WorldEditVersionMain.config.contains("Save Function Override") || !WorldEditVersionMain.config.contains("Automatic Reload") || !WorldEditVersionMain.config.contains("Plugin Version") || !WorldEditVersionMain.config.getString("Plugin Version").equals(plugin.getDescription().getVersion()))) {
            String schemPath = WorldEditVersionMain.config.contains("WorldEdit Schematic-Path") ? WorldEditVersionMain.config.getString("WorldEdit Schematic-Path") : "Default Schematic Path";
            List<String> fileExtensions = WorldEditVersionMain.config.contains("File Extensions") ? WorldEditVersionMain.config.getStringList("File Extensions") : Arrays.asList("schematic", "schem");
            int listmax = WorldEditVersionMain.config.contains("Listmax") ? WorldEditVersionMain.config.getInt("Listmax") : 10;
            boolean spaceLists = !WorldEditVersionMain.config.contains("Space Lists") || WorldEditVersionMain.config.getBoolean("Space Lists");
            boolean saveOverride = !WorldEditVersionMain.config.contains("Save Function Override") || WorldEditVersionMain.config.getBoolean("Save Function Override");
            boolean stoplagOverride = !WorldEditVersionMain.config.contains("Stoplag Override") || WorldEditVersionMain.config.getBoolean("Stoplag Override");
            boolean autoReload = !WorldEditVersionMain.config.contains("Automatic Reload") || WorldEditVersionMain.config.getBoolean("Automatic Reload");

            File file = new File(plugin.getDataFolder(), "config.yml");
            if (writeToFile(file, "https://github.com/Zeanon/SchemManager/releases/latest/download/config.yml")) {
                WorldEditVersionMain.config.update();

                WorldEditVersionMain.config.set("Plugin Version", plugin.getDescription().getVersion());
                WorldEditVersionMain.config.set("WorldEdit Schematic-Path", schemPath);
                WorldEditVersionMain.config.set("File Extensions", fileExtensions);
                WorldEditVersionMain.config.set("Listmax", listmax);
                WorldEditVersionMain.config.set("Space Lists", spaceLists);
                WorldEditVersionMain.config.set("Save Function Override", saveOverride);
                WorldEditVersionMain.config.set("Stoplag Override", stoplagOverride);
                WorldEditVersionMain.config.set("Automatic Reload", autoReload);

                System.out.println("[" + plugin.getName() + "] >> [Configs] >> " + WorldEditVersionMain.config.getFile().getName() + " updated");
                return true;
            } else {
                System.out.println("[" + plugin.getName() + "] >> [Configs] >> " + WorldEditVersionMain.config.getFile().getName() + " could not be updated");
                return false;
            }
        }
        return true;
    }

    private static boolean writeToFile(File file, String url) {
        try {
            BufferedInputStream inputStream = null;
            FileOutputStream outputStream = null;
            try {
                inputStream = new BufferedInputStream(new URL(url).openStream());
                if (!file.exists()) {
                    Files.copy(inputStream, Paths.get(file.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    outputStream = new FileOutputStream(file.getAbsolutePath());
                    final byte[] data = new byte[1024];
                    int count;
                    while ((count = inputStream.read(data, 0, 1024)) != -1) {
                        outputStream.write(data, 0, count);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String removeExtension(String path) {
        return path.replaceFirst("[.][^.]+$", "");
    }

    public static String getExtension(String path) {
        return path.lastIndexOf(".") > 0 ? path.substring(path.lastIndexOf(".") + 1) : "";
    }
}