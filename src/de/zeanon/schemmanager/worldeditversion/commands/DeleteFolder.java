package de.zeanon.schemmanager.worldeditversion.commands;

import de.zeanon.schemmanager.globalutils.DefaultHelper;
import de.zeanon.schemmanager.worldeditversion.helper.Helper;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class DeleteFolder {

    public static boolean onDeleteFolder(Player p, String[] args) {
        File file = new File(Helper.getSchemPath() + args[2]);
        final boolean fileExists = file.exists() && file.isDirectory();

        if (args.length == 3) {
            if (fileExists) {
                if (file.listFiles().length > 0) {
                    p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " still contains files.");
                }
                DefaultHelper.sendBooleanMessage(ChatColor.RED + "Do you really want to delete " + ChatColor.GOLD + args[2] + ChatColor.RED + "?", "//schem delfolder " + args[2] + " confirm", "//schem delfolder " + args[2] + " deny", p);
                Helper.addDeleteFolderRequest(p, args[2]);
                return true;
            } else {
                p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
                return false;
            }
        } else if (args.length == 4 && Helper.checkDeleteFolderRequest(p, args[2])) {
            if (args[3].equals("confirm")) {
                Helper.removeDeleteFolderRequest(p);
                if (fileExists) {
                    try {
                        FileUtils.deleteDirectory(file);
                        p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " was deleted successfully.");
                        return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " could not be deleted.");
                        return false;
                    }
                } else {
                    p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
                    return false;
                }
            } else if (args[3].equals("deny")) {
                Helper.removeDeleteFolderRequest(p);
                p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " was not deleted.");
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}