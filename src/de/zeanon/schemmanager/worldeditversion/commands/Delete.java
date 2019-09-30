package de.zeanon.schemmanager.worldeditversion.commands;

import java.io.File;
import java.util.ArrayList;

import de.zeanon.schemmanager.worldeditversion.helper.Helper;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Delete {

    public static boolean onDelete(Player p, String[] args) {
        String[] extensions = {"schematic", "schem"};
        ArrayList<File> files = new ArrayList<>();
        for (String extension : extensions) {
            files.add(new File(Helper.getSchemPath() + args[2] + extension));
        }
        files = Helper.getExistingFiles(files);
        final boolean fileExists = files.size() > 0;

        if (args.length == 3) {
            if (fileExists) {
                Helper.sendBooleanMessage(ChatColor.RED + "Do you really want to delete " + ChatColor.GOLD + args[2] + ChatColor.RED + "?", "//schem del " + args[2] + " confirm", "//schem del " + args[2] + " deny", p);
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
                    for (File file : files) {
                        if (!file.delete()) {
                            p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " could not be deleted.");
                            Helper.removeDeleteRequest(p);
                            return false;
                        }
                    }
                    p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " was deleted successfully.");
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