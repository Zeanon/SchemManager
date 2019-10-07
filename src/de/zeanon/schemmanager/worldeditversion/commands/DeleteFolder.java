package de.zeanon.schemmanager.worldeditversion.commands;

import de.zeanon.schemmanager.globalutils.MessageUtils;
import de.zeanon.schemmanager.worldeditversion.utils.Helper;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class DeleteFolder {

    public static boolean onDeleteFolder(Player p, String[] args) {
        Path schemPath = Helper.getSchemPath();
        File file = schemPath != null ? schemPath.resolve(args[2]).toFile() : null;
        final boolean fileExists = file != null && file.exists() && file.isDirectory();

        if (args.length == 3) {
            if (fileExists) {
                if (Objects.requireNonNull(file.listFiles()).length > 0) {
                    MessageUtils.sendInvertedCommandMessage(ChatColor.RED + " still contains files.", ChatColor.GREEN + args[2], ChatColor.RED + "Open " + ChatColor.GREEN + args[2], "//schem list " + args[2], p);
                }
                MessageUtils.sendBooleanMessage(ChatColor.RED + "Do you really want to delete " + ChatColor.GREEN + args[2] + ChatColor.RED + "?", "//schem delfolder " + args[2] + " confirm", "//schem delfolder " + args[2] + " deny", p);
                Helper.addDeleteFolderRequest(p, args[2]);
                return true;
            } else {
                p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " does not exist.");
                return false;
            }
        } else if (args.length == 4 && Helper.checkDeleteFolderRequest(p, args[2])) {
            if (args[3].equalsIgnoreCase("confirm")) {
                Helper.removeDeleteFolderRequest(p);
                if (fileExists) {
                    try {
                        String parentName = null;
                        FileUtils.deleteDirectory(file);
                        parentName = Rename.getParentName(parentName, file);
                        p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " was deleted successfully.");
                        if (parentName != null) {
                            p.sendMessage(ChatColor.RED + "Folder " + ChatColor.GREEN + parentName + ChatColor.RED + " was deleted sucessfully due to being empty.");
                        }
                        return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " could not be deleted.");
                        return false;
                    }
                } else {
                    p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " does not exist.");
                    return false;
                }
            } else if (args[3].equalsIgnoreCase("deny")) {
                Helper.removeDeleteFolderRequest(p);
                p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " was not deleted.");
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}