package de.zeanon.schemmanager.worldeditversion.commands;

import de.zeanon.schemmanager.globalutils.DefaultHelper;
import de.zeanon.schemmanager.worldeditversion.helper.Helper;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Rename {

    public static boolean onRename(Player p, String[] args) {
        String schemPath = Helper.getSchemPath();
        ArrayList<File> oldFiles = DefaultHelper.getExistingFiles(schemPath + args[2]);
        ArrayList<File> newFiles = DefaultHelper.getExistingFiles(schemPath + args[3]);
        final boolean oldFileExists = oldFiles.size() > 0;
        final boolean newFileExists = newFiles.size() > 0;

        if (args.length == 4) {
            if (oldFileExists) {
                if (newFileExists) {
                    p.sendMessage(ChatColor.GOLD + args[3] + ChatColor.RED + " already exists, the file will be overwritten.");
                }

                DefaultHelper.sendBooleanMessage(ChatColor.RED + "Do you really want to rename " + ChatColor.GOLD + args[2] + ChatColor.RED + "?", "//schem rename " + args[2] + " " + args[3] + " confirm", "//schem rename " + args[2] + " " + args[3] + " deny", p);
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
                    return moveFile(p, args[2], oldFiles, newFiles, schemPath + args[3]);
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


    private static boolean moveFile(Player p, String fileName, ArrayList<File> oldFiles, ArrayList<File> newFiles, String destPath) {
        try {
            for (File file : newFiles) {
                if (!file.delete()) {
                    p.sendMessage(ChatColor.GOLD + fileName + ChatColor.RED + " could not be renamed.");
                    return false;
                }
            }
            for (File file : oldFiles) {
                if (DefaultHelper.getExtension(destPath).isEmpty()) {
                    System.out.println("Hi");
                }
                if (DefaultHelper.getExtension(destPath).equals("")) {
                    FileUtils.moveFile(file, new File(destPath + "." + DefaultHelper.getExtension(file.getName())));
                } else {
                    FileUtils.moveFile(file, new File(destPath));
                }
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