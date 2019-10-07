package de.zeanon.schemmanager.worldeditversion.commands;

import de.zeanon.schemmanager.globalutils.ConfigUtils;
import de.zeanon.schemmanager.globalutils.MessageUtils;
import de.zeanon.schemmanager.globalutils.ZeanonFileUtils;
import de.zeanon.schemmanager.worldeditversion.utils.Helper;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

public class Rename {

    public static boolean onRename(Player p, String[] args) {
        Path schemPath = Helper.getSchemPath();
        ArrayList<File> oldFiles = schemPath != null ? ZeanonFileUtils.getExistingFiles(schemPath.resolve(args[2])) : null;
        ArrayList<File> newFiles = schemPath != null ? ZeanonFileUtils.getExistingFiles(schemPath.resolve(args[3])) : null;
        final boolean oldFileExists = oldFiles != null && oldFiles.size() > 0;
        final boolean newFileExists = newFiles != null && newFiles.size() > 0;

        if (args.length == 4) {
            if (oldFileExists) {
                if (newFileExists) {
                    p.sendMessage(ChatColor.GOLD + args[3] + ChatColor.RED + " already exists, the file will be overwritten.");
                }

                MessageUtils.sendBooleanMessage(ChatColor.RED + "Do you really want to rename " + ChatColor.GOLD + args[2] + ChatColor.RED + "?", "//schem rename " + args[2] + " " + args[3] + " confirm", "//schem rename " + args[2] + " " + args[3] + " deny", p);
                Helper.addRenameRequest(p, args[2]);
                return true;

            } else {
                p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
                return false;
            }
        } else if (args.length == 5 && Helper.checkRenameRequest(p, args[2])) {
            if (args[4].equalsIgnoreCase("confirm")) {
                Helper.removeRenameRequest(p);
                if (oldFileExists) {
                    return moveFile(p, args[2], oldFiles, newFiles, schemPath.resolve(args[3]));
                } else {
                    p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
                    return false;
                }
            } else if (args[4].equalsIgnoreCase("deny")) {
                Helper.removeRenameRequest(p);
                p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " was not renamed.");
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    private static boolean moveFile(Player p, String fileName, ArrayList<File> oldFiles, ArrayList<File> newFiles, Path destPath) {
        try {
            if (newFiles != null) {
                for (File file : newFiles) {
                    if (!file.delete()) {
                        p.sendMessage(ChatColor.GOLD + fileName + ChatColor.RED + " could not be renamed.");
                        return false;
                    }
                }
            }
            String parentName = null;
            for (File file : oldFiles) {
                if (ConfigUtils.getStringList("File Extensions").stream().noneMatch(ZeanonFileUtils.getExtension(destPath.toString())::equals)) {
                    FileUtils.moveFile(file, new File(destPath.toString() + ZeanonFileUtils.getExtension(file.getName())));
                    parentName = getParentName(parentName, file);
                } else {
                    FileUtils.moveFile(file, destPath.toFile());
                    parentName = getParentName(parentName, file);
                }
            }
            p.sendMessage(ChatColor.GOLD + fileName + ChatColor.RED + " was renamed successfully.");
            if (parentName != null) {
                p.sendMessage(ChatColor.RED + "Folder " + ChatColor.GREEN + parentName + ChatColor.RED + " was deleted sucessfully due to being empty.");
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            p.sendMessage(ChatColor.GOLD + fileName + ChatColor.RED + " could not be renamed.");
            return false;
        }
    }

    static String getParentName(String parentName, File file) {
        if (ConfigUtils.getBoolean("Delete empty Folders") && !file.getParentFile().equals(Helper.getSchemFolder())) {
            parentName = Objects.requireNonNull(file.getParentFile().listFiles()).length > 0 ? null : ZeanonFileUtils.deleteEmptyParent(file);
        }
        return parentName;
    }
}