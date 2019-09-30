package de.zeanon.schemmanager.worldeditversion.commands;

import de.zeanon.schemmanager.worldeditversion.helper.Helper;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Rename {

    public static boolean onRename(Player p, String[] args) {
        String schemPath = Helper.getSchemPath();
        ArrayList<File> oldFiles = Helper.getExistingFiles(schemPath + args[2]);
        ArrayList<File> newFiles = Helper.getExistingFiles(schemPath + args[3]);
        final boolean oldFileExists = oldFiles.size() > 0;
        final boolean newFileExists = newFiles.size() > 0;

        if (args.length == 4) {
            if (oldFileExists) {
                if (newFileExists) {
                    p.sendMessage(ChatColor.GOLD + args[3] + ChatColor.RED + " already exists, the file will be overwritten.");
                }

                Helper.sendBooleanMessage(ChatColor.RED + "Do you really want to rename " + ChatColor.GOLD + args[2] + ChatColor.RED + "?", "//schem rename " + args[2] + " " + args[3] + " confirm", "//schem rename " + args[2] + " " + args[3] + " deny", p);
                Helper.addRenameRequest(p, args[2]);
                return true;

            } else {
                p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
                return false;
            }
        } else if (args.length == 5 && Helper.checkRenameRequest(p, args[2])) {
            if (args[4].equals("confirm")) {
                Helper.removeRenameRequest(p);
                if (oldFileExists) {
                    return deleteFile(p, args[2], oldFiles, newFiles, schemPath + args[3]);
                } else {
                    p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
                    return false;
                }
            } else if (args[4].equals("deny")) {
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


    private static boolean deleteFile(Player p, String fileName, ArrayList<File> oldFiles, ArrayList<File> newFiles, String destPath) {
        try {
            for (File file : newFiles) {
                if (!file.delete()) {
                    p.sendMessage(ChatColor.GOLD + fileName + ChatColor.RED + " could not be renamed.");
                    return false;
                }
            }
            for (File file : oldFiles) {
                FileUtils.moveFile(file, new File(destPath + "." + FilenameUtils.getExtension(file.getName())));
            }
            p.sendMessage(ChatColor.GOLD + fileName + ChatColor.RED + " was renamed successfully.");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            p.sendMessage(ChatColor.GOLD + fileName + ChatColor.RED + " could not be renamed.");
            return false;
        }
    }
}