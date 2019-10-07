package de.zeanon.schemmanager.globalutils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.worldeditversion.WorldEditVersionMain;
import de.zeanon.schemmanager.worldeditversion.utils.WorldEditVersionRequestUtils;
import net.md_5.bungee.api.ChatColor;
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
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Update {

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
            if (writeToFile(new File(InternalFileUtils.getPluginFolderPath() + fileName), new BufferedInputStream(new URL("https://github.com/Zeanon/SchemManager/releases/latest/download/SchemManager.jar").openStream()))) {
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
            if (writeToFile(new File(InternalFileUtils.getPluginFolderPath() + fileName), new BufferedInputStream(new URL("https://github.com/Zeanon/SchemManager/releases/latest/download/SchemManager.jar").openStream()))) {
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
        if (ConfigUtils.getBoolean("Automatic Reload")) {
            PluginManager pm = Bukkit.getPluginManager();
            if (pm.getPlugin("PlugMan") != null && pm.isPluginEnabled(pm.getPlugin("PlugMan"))) {
                PlugManUtils.plugmanReload();
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

            if (writeToFile(new File(SchemManager.getInstance().getDataFolder(), "config.yml"), new BufferedInputStream(Objects.requireNonNull(WorldEditVersionRequestUtils.class.getClassLoader().getResourceAsStream("config.yml"))))) {
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
                    Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    outputStream = new FileOutputStream(file);
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
}
