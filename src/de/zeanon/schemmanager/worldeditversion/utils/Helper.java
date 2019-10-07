package de.zeanon.schemmanager.worldeditversion.utils;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import de.zeanon.schemmanager.worldeditversion.WorldEditVersionMain;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;

public class Helper {

    public static WorldEditPlugin we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
    private static File schemFolder;
    private static Path schemFolderPath;
    private static HashMap<String, String> deleteRequests = new HashMap<>();
    private static HashMap<String, String> deleteFolderRequests = new HashMap<>();
    private static HashMap<String, String> renameRequests = new HashMap<>();
    private static HashMap<String, String> renameFolderRequests = new HashMap<>();
    private static HashMap<String, String> overwriteRequests = new HashMap<>();


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


    public static Path getSchemPath() {
        if (WorldEditVersionMain.weConfig.hasNotChanged()) {
            return schemFolderPath;
        } else {
            initSchemPath();
            return schemFolderPath;
        }
    }

    public static File getSchemFolder() {
        return schemFolder;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void initSchemPath() {
        Path tempPath = Paths.get(WorldEditVersionMain.weConfig.getString("saving.dir"));
        if (tempPath.isAbsolute()) {
            schemFolderPath = tempPath.normalize();
            schemFolder = schemFolderPath.toFile();
            if (!schemFolder.exists()) {
                schemFolder.mkdirs();
            }
        } else {
            schemFolderPath = Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("WorldEdit")).getDataFolder().toPath().resolve(tempPath).normalize();
            schemFolder = schemFolderPath.toFile();
            if (!schemFolder.exists()) {
                schemFolder.mkdirs();
            }
        }
    }
}