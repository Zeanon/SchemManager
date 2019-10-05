package de.zeanon.schemmanager.globalutils;

import com.rylinaux.plugman.util.PluginUtil;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.worldeditversion.WorldEditVersionMain;
import de.zeanon.schemmanager.worldeditversion.helper.Helper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

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

/**
 * a Global Helper Class
 */
public class DefaultHelper {

    private static final ArrayList<String> disableRequests = new ArrayList<>();
    private static final ArrayList<String> updateRequests = new ArrayList<>();
    private static String pluginFolderPath;
    public static String slash;

    /**
     * initiates the class
     */
    public static void initiate() {
        slash = SchemManager.getInstance().getDataFolder().getAbsolutePath().contains("\\") ? "\\\\" : "/";
        String[] parts = SchemManager.getInstance().getDataFolder().getAbsolutePath().split(slash);
        StringBuilder pathBuilder = new StringBuilder(parts[0] + slash);
        for (int i = 1; i < parts.length - 1; i++) {
            pathBuilder.append(parts[i]).append(slash);
        }
        pluginFolderPath = pathBuilder.toString();
    }


    /**
     * Sends a clickable message performing a command
     *
     * @param message        the non clickable part of the message
     * @param commandMessage the clickable part of the message
     * @param hoverMessage   the message to be shown when hovering over commandMessage.
     * @param command        the command to be executed when clicked
     * @param target         the player the message is sent to
     */
    public static void sendCommandMessage(String message, String commandMessage, String hoverMessage, String command, Player target) {
        new TextComponent();
        TextComponent localMessage = new TextComponent(TextComponent.fromLegacyText(message));
        TextComponent commandPart = new TextComponent(TextComponent.fromLegacyText(commandMessage));
        commandPart.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        commandPart.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMessage).create()));
        localMessage.addExtra(commandPart);
        target.spigot().sendMessage(localMessage);
    }

    /**
     * Sends a clickable message performing a command
     *
     * @param message        the non clickable part of the message
     * @param commandMessage the clickable part of the message
     * @param hoverMessage   the message to be shown when hovering over commandMessage.
     * @param command        the command to be executed when clicked
     * @param target         the player the message is sent to
     */
    public static void sendInvertedCommandMessage(String message, String commandMessage, String hoverMessage, String command, Player target) {
        new TextComponent();
        TextComponent localMessage = new TextComponent(TextComponent.fromLegacyText(message));
        TextComponent commandPart = new TextComponent(TextComponent.fromLegacyText(commandMessage));
        commandPart.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        commandPart.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMessage).create()));
        commandPart.addExtra(localMessage);
        target.spigot().sendMessage(commandPart);
    }

    /**
     * Sends a boolean type message to the player
     *
     * @param message    the message to be sent
     * @param commandYes the command to be executed when clicked on yes
     * @param commandNo  the command to be executed when clicked on no
     * @param target     the player the message is sent to
     */
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

    /**
     * Sends the player a message with scroll buttons
     *
     * @param commandForward  the command to be executed when clicking on forward
     * @param commandBackward the command to be executed when clicking on backwards
     * @param messageForward  the message to be shown when hovering over the forward button
     * @param messageBackward the message to be shown when hovering over the backwards button
     * @param target          the player the message is sent to
     * @param buttonColor     the color of the buttons
     */
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

    /**
     * sends the player a message with suggestCommand capabilities
     *
     * @param message        the non clickable part of the message
     * @param suggestMessage the clickable part of the message
     * @param hoverMessage   the message to sho when hovering over suggestMessage.
     * @param command        the command to be suggested when clicked
     * @param target         the player the message is sent to
     */
    @SuppressWarnings("Duplicates")
    public static void sendSuggestMessage(String message, String suggestMessage, String hoverMessage, String command, Player target) {
        TextComponent localMessage = new TextComponent(TextComponent.fromLegacyText(message));
        TextComponent suggestPart = new TextComponent(TextComponent.fromLegacyText(suggestMessage));
        suggestPart.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        suggestPart.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(hoverMessage))).create()));
        localMessage.addExtra(suggestPart);
        target.spigot().sendMessage(localMessage);
    }

    /**
     * @param message1     the first part of the message (no hovermessage)
     * @param message2     the second part of the message (hovermessage)
     * @param message3     the third part of the message (no hovermessage)
     * @param hoverMessage the message to be shown when hovering over message2
     * @param target       the player the message is sent to
     */
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


    static void addDisableRequest(Player p) {
        disableRequests.add(p.getUniqueId().toString());
    }

    public static void removeDisableRequest(Player p) {
        disableRequests.remove(p.getUniqueId().toString());
    }

    static boolean checkDisableRequest(Player p) {
        return disableRequests.contains(p.getUniqueId().toString());
    }


    static void addUpdateRequest(Player p) {
        updateRequests.add(p.getUniqueId().toString());
    }

    public static void removeUpdateRequest(Player p) {
        updateRequests.remove(p.getUniqueId().toString());
    }

    static boolean checkUpdateRequest(Player p) {
        return updateRequests.contains(p.getUniqueId().toString());
    }

    /**
     * @param folder thefolder to look into
     * @param deep   deepSearch
     * @return the files of the folder that are directorys
     */
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

    /**
     * get an int from the config
     *
     * @param path the yaml path
     * @return value
     */
    public static int getInt(String path) {
        if (SchemManager.config.contains(path)) {
            return SchemManager.config.getInt(path);
        } else {
            updateConfig(true);
            return (int) getDefaultValue(path);
        }
    }

    /**
     * get a boolean from the config
     *
     * @param path the yaml path
     * @return value
     */
    public static boolean getBoolean(String path) {
        if (SchemManager.config.contains(path)) {
            return SchemManager.config.getBoolean(path);
        } else {
            updateConfig(true);
            return (boolean) getDefaultValue(path);
        }
    }

    /**
     * get a StringList from the config
     *
     * @param path the yaml path
     * @return value
     */
    @SuppressWarnings("unchecked")
    public static List<String> getStringList(String path) {
        if (SchemManager.config.contains(path)) {
            return SchemManager.config.getStringList(path);
        } else {
            updateConfig(true);
            return (List<String>) getDefaultValue(path);
        }
    }

    private static Object getDefaultValue(String path) {
        switch (path) {
            case "Space Lists":
                return true;
            case "Delete empty Folders":
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
                return Arrays.asList("schem", "schematic");
            case "Plugin Version":
                return SchemManager.getInstance().getDescription().getVersion();
            default:
                return null;
        }
    }


    public static ArrayList<File> getExistingFiles(String path) {
        ArrayList<File> tempFiles = new ArrayList<>();
        if (getStringList("File Extensions").stream().anyMatch(Objects.requireNonNull(getExtension(path))::equalsIgnoreCase)) {
            File file = new File(path);
            if (file.exists() && !file.isDirectory()) {
                return new ArrayList<>(Collections.singletonList(file));
            }
        }
        getStringList("File Extensions").iterator().forEachRemaining(extension -> tempFiles.add(new File(path + "." + extension)));
        ArrayList<File> files = new ArrayList<>();
        for (File file : tempFiles) {
            if (file.exists() && !file.isDirectory()) {
                files.add(file);
            }
        }
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
        try {
            if (writeToFile(new File(pluginFolderPath + fileName), new BufferedInputStream(new URL("https://github.com/Zeanon/SchemManager/releases/latest/download/SchemManager.jar").openStream()))) {
                p.sendMessage(ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " was updated successfully.");
                return updateReload();
            } else {
                p.sendMessage(ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " could not be updated.");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            p.sendMessage(ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " could not be updated.");
            return false;
        }
    }


    public static boolean update() {
        System.out.println("SchemManager is updating...");
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
            System.out.println("SchemManager could not be updated.");
            return false;
        }
        try {
            if (writeToFile(new File(pluginFolderPath + fileName), new BufferedInputStream(new URL("https://github.com/Zeanon/SchemManager/releases/latest/download/SchemManager.jar").openStream()))) {
                System.out.println("SchemManager was updated successfully.");
                return updateReload();
            } else {
                System.out.println("SchemManager could not be updated.");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("SchemManager could not be updated.");
            return false;
        }
    }


    private static boolean updateReload() {
        if (getBoolean("Automatic Reload")) {
            PluginManager pm = Bukkit.getPluginManager();
            if (pm.getPlugin("PlugMan") != null && pm.isPluginEnabled(pm.getPlugin("PlugMan"))) {
                PluginUtil.reload(SchemManager.getInstance());
            } else {
                Bukkit.getServer().reload();
            }
        }
        return true;
    }


    public static boolean updateConfig(boolean force) {
        if (force || (!SchemManager.config.contains("WorldEdit Schematic-Path") || !SchemManager.config.contains("Listmax") || !SchemManager.config.contains("Space Lists") || !SchemManager.config.contains("Save Function Override") || !SchemManager.config.contains("Automatic Reload") || !SchemManager.config.contains("Plugin Version") || !SchemManager.config.getString("Plugin Version").equals(SchemManager.getInstance().getDescription().getVersion()))) {
            List<String> fileExtensions = SchemManager.config.contains("File Extensions") ? SchemManager.config.getStringList("File Extensions") : Arrays.asList("schem", "schematic");
            int listmax = SchemManager.config.contains("Listmax") ? SchemManager.config.getInt("Listmax") : 10;
            boolean spaceLists = !SchemManager.config.contains("Space Lists") || SchemManager.config.getBoolean("Space Lists");
            boolean saveOverride = !SchemManager.config.contains("Save Function Override") || SchemManager.config.getBoolean("Save Function Override");
            boolean stoplagOverride = !SchemManager.config.contains("Stoplag Override") || SchemManager.config.getBoolean("Stoplag Override");
            boolean autoReload = !SchemManager.config.contains("Automatic Reload") || SchemManager.config.getBoolean("Automatic Reload");
            boolean deleteEmptyFolders = !SchemManager.config.contains("Delete empty Folders") || SchemManager.config.getBoolean("Delete empty Folders");

            if (writeToFile(new File(SchemManager.getInstance().getDataFolder(), "config.yml"), new BufferedInputStream(Objects.requireNonNull(Helper.class.getClassLoader().getResourceAsStream("config.yml"))))) {
                SchemManager.config.update();

                SchemManager.config.set("Plugin Version", SchemManager.getInstance().getDescription().getVersion());
                SchemManager.config.set("File Extensions", fileExtensions);
                SchemManager.config.set("Listmax", listmax);
                SchemManager.config.set("Space Lists", spaceLists);
                SchemManager.config.set("Delete empty Folders", deleteEmptyFolders);
                SchemManager.config.set("Save Function Override", saveOverride);
                SchemManager.config.set("Stoplag Override", stoplagOverride);
                SchemManager.config.set("Automatic Reload", autoReload);

                System.out.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> " + SchemManager.config.getFile().getName() + " updated");
                return true;
            } else {
                System.out.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> " + SchemManager.config.getFile().getName() + " could not be updated");
                return false;
            }
        }
        return true;
    }


    private static boolean writeToFile(File file, BufferedInputStream inputStream) {
        try {
            FileOutputStream outputStream = null;
            try {
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
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void disable() {
        Bukkit.getPluginManager().disablePlugin(SchemManager.getInstance());
    }

    public static String removeExtension(String path) {
        return path.replaceFirst("[.][^.]+$", "");
    }

    public static String getExtension(String path) {
        return path.lastIndexOf(".") > 0 ? path.substring(path.lastIndexOf(".") + 1) : null;
    }

    public static String deleteParent(File file) {
        if (file.getParentFile().delete()) {
            return deleteParent(file.getParentFile());
        }
        return null;
    }
}