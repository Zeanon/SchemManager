package de.zeanon.schemmanager.worldeditversion.commands;

import de.zeanon.schemmanager.globalutils.DefaultHelper;
import de.zeanon.schemmanager.worldeditversion.helper.Helper;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;

public class Delete {

    public static boolean onDelete(Player p, String[] args) {
        ArrayList<File> files = DefaultHelper.getExistingFiles(Helper.getSchemPath() + args[2]);
        final boolean fileExists = files.size() > 0;

        if (args.length == 3) {
            if (fileExists) {
                DefaultHelper.sendBooleanMessage(ChatColor.RED + "Do you really want to delete " + ChatColor.GOLD + args[2] + ChatColor.RED + "?", "//schem del " + args[2] + " confirm", "//schem del " + args[2] + " deny", p);
                Helper.addDeleteRequest(p, args[2]);
                return true;
            } else {
                p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
                return false;
            }
        } else if (args.length == 4 && Helper.checkDeleteRequest(p, args[2])) {
            if (args[3].equals("confirm")) {
                Helper.removeDeleteRequest(p);
                if (fileExists) {
                    String parentName = null;
                    for (File file : files) {
                        if (!file.delete()) {
                            p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " could not be deleted.");
                            return false;
                        } else {
                            if (DefaultHelper.getBoolean("Delete empty Folders") && !file.getParentFile().equals(new File(Helper.getSchemPath()))) {
                                if (file.getParentFile().delete()) {
                                    parentName = file.getParentFile().getName();
                                }
                            }
                        }
                    }
                    p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " was deleted successfully.");
                    if (parentName != null) {
                        p.sendMessage(ChatColor.GOLD + "Folder " + ChatColor.GREEN + parentName + ChatColor.RED + " was deleted sucessfully due to being empty.");
                    }
                    return true;
                } else {
                    p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
                    return false;
                }

            } else if (args[3].equals("deny")) {
                Helper.removeDeleteRequest(p);
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
