package de.zeanon.schemmanager.worldeditversion.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import de.zeanon.schemmanager.worldeditversion.helper.Helper;
import net.md_5.bungee.api.ChatColor;

public class Rename {

    public static boolean onRename(Player p, String[] args) {
        String[] extensions = {"schematic", "schem"};
        ArrayList<File> oldFiles = new ArrayList<>();
        ArrayList<File> newFiles = new ArrayList<>();
        for (String extension : extensions) {
            oldFiles.add(new File(Helper.getSchemPath() + args[2] + extension));
            newFiles.add(new File(Helper.getSchemPath() + args[3] + extension));
        }
        oldFiles = Helper.getExistingFiles(oldFiles);
        newFiles = Helper.getExistingFiles(newFiles);
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
                    return deleteFile(p, args[2], oldFiles, newFiles);
                } else {
                    p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
                    Helper.removeRenameRequest(p);
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


    private static boolean deleteFile(Player p, String fileName, ArrayList<File> oldFiles, ArrayList<File> newFiles) {
        try {
            File destDir = newFiles.get(0).getParentFile();
            for (File file : newFiles) {
                if (!file.delete()) {
                    p.sendMessage(ChatColor.GOLD + fileName + ChatColor.RED + " could not be renamed.");
                    return false;
                }
            }
            for (File file : oldFiles) {
                FileUtils.moveFileToDirectory(file, destDir, true);
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