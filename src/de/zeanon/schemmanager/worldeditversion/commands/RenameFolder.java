package de.zeanon.schemmanager.worldeditversion.commands;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import de.zeanon.schemmanager.worldeditversion.helper.Helper;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class RenameFolder {

    public static boolean onRenameFolder(Player p, String[] args) {
        String schemFolderPath = Helper.getSchemPath();
        File file_old = new File(schemFolderPath + args[2]);
        File file_new = new File(schemFolderPath + args[3]);

        if (args.length == 4) {
            if (!file_old.exists() || !file_old.isDirectory()) {
                p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
                return false;
            } else if (file_new.exists() && file_new.isDirectory()) {
                p.sendMessage(ChatColor.GOLD + args[3] + ChatColor.RED + " already exists, the folders will be merged.");
                int id = 0;
                String[] extension = {"schematic", "schem"};
                for (File oldFile : FileUtils.listFiles(file_old, extension, true)) {
                    for (File newFile : FileUtils.listFiles(file_new, extension, true)) {
                        System.out.println(oldFile.getName());
                        System.out.println(newFile.getName());
                        final String rawNewFileName = newFile.getName().replaceAll("\\.schematic", "").replaceAll("\\.schem", "");
                        if (rawNewFileName
                                .equals(oldFile.getName().replaceAll("\\.schematic", "").replaceAll("\\.schem", ""))
                                && newFile.toPath().relativize(file_new.toPath())
                                .equals(oldFile.toPath().relativize(file_old.toPath()))) {
                            if (id == 0) {
                                p.sendMessage(ChatColor.RED + "These schematics already exist in " + ChatColor.GOLD
                                        + args[3] + ChatColor.RED + ", they will be overwritten.");
                            }
                            String path = newFile.getAbsolutePath().replaceAll(schemFolderPath, "")
                                    .replaceAll("\\.schematic", "").replaceAll("\\.schem", "").replaceAll("\\\\", "/");
                            Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ",
                                    ChatColor.GOLD + rawNewFileName + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path
                                            + ChatColor.DARK_GRAY + "]",
                                    ChatColor.RED + "Lade " + ChatColor.GOLD + rawNewFileName + ChatColor.RED
                                            + " to your clipboard",
                                    "//schem load " + path, p);
                            id++;
                        }
                    }
                }

                int i = 0;
                for (File oldFolder : Helper.getFolders(file_old, true)) {
                    for (File newFolder : Helper.getFolders(file_new, true)) {
                        if (newFolder.getName().equals(oldFolder.getName())
                                && newFolder.toPath().relativize(file_new.toPath())
                                .equals(oldFolder.toPath().relativize(file_old.toPath()))) {
                            if (i == 0) {
                                p.sendMessage(ChatColor.RED + "These folders already exist in " + ChatColor.GOLD
                                        + args[3] + ChatColor.RED + ", they will be merged.");
                            }
                            String name = newFolder.getName();
                            String path = newFolder.getAbsolutePath().replaceAll(schemFolderPath, "")
                                    .replaceAll("\\\\", "/");
                            Helper.sendCommandMessage(ChatColor.RED + Integer.toString(i + 1) + ": ",
                                    ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path
                                            + ChatColor.DARK_GRAY + "]",
                                    ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
                            i++;
                        }
                    }
                }
                if (id > 1 && i > 1) {
                    p.sendMessage(ChatColor.RED + "There are already " + ChatColor.DARK_PURPLE + id + ChatColor.RED
                            + " schematics and " + ChatColor.DARK_PURPLE + i + ChatColor.RED
                            + " folders with the same name in " + ChatColor.GOLD + args[3] + ChatColor.RED + ".");
                } else if (id > 1) {
                    p.sendMessage(ChatColor.RED + "There are already " + ChatColor.DARK_PURPLE + id + ChatColor.RED
                            + " schematics with the same name in " + ChatColor.GOLD + args[3] + ChatColor.RED + ".");
                } else if (i > 1) {
                    p.sendMessage(ChatColor.RED + "There are already " + ChatColor.DARK_PURPLE + i + ChatColor.RED
                            + " folders with the same name in " + ChatColor.GOLD + args[3] + ChatColor.RED + ".");
                }
            }
            Helper.sendBooleanMessage(
                    ChatColor.RED + "Do you really want to rename " + ChatColor.GOLD + args[2] + ChatColor.RED + "?",
                    "//schem renamefolder " + args[2] + " " + args[3] + " confirm",
                    "//schem renamefolder " + args[2] + " " + args[3] + " deny", p);
            Helper.addRenameFolderRequest(p, args[2]);
            return true;
        } else {
            if (args[4].equals("confirm")) {
                if (!file_old.exists() || !file_old.isDirectory()) {
                    p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " could not be renamed.");
                    Helper.removeRenameFolderRequest(p);
                    return false;
                } else {
                    if (deepMerge(file_old, file_new)) {
                        try {
                            FileUtils.deleteDirectory(file_old);
                            p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " was renamed successfully.");
                            return true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " could not be renamed.");
                            Helper.removeRenameFolderRequest(p);
                            return false;
                        }
                    } else {
                        p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " could not be renamed.");
                        Helper.removeRenameFolderRequest(p);
                        return false;
                    }
                }
            } else if (args[4].equals("deny")) {
                if (!file_old.exists() || !file_old.isDirectory()) {
                    return false;
                }
                Helper.removeRenameFolderRequest(p);
                p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " was not renamed");
                return true;
            } else {
                return false;
            }
        }
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static boolean deepMerge(File oldFile, File newFile) {
        if (Objects.requireNonNull(oldFile.listFiles()).length == 0) {
            return true;
        } else {
            for (File tempFile : Objects.requireNonNull(oldFile.listFiles())) {
                try {
                    if (new File(newFile, tempFile.getName()).exists()) {
                        if (tempFile.isDirectory()) {
                            if (!deepMerge(tempFile, new File(newFile, tempFile.getName()))) {
                                return false;
                            }
                        } else {
                            new File(newFile, tempFile.getName()).delete();
                            FileUtils.moveToDirectory(tempFile, newFile, true);
                        }
                    } else {
                        FileUtils.moveToDirectory(tempFile, newFile, true);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }
    }
}