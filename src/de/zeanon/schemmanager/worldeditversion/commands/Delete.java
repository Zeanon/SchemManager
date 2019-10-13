package de.zeanon.schemmanager.worldeditversion.commands;

import de.zeanon.schemmanager.utils.ConfigUtils;
import de.zeanon.schemmanager.utils.InternalFileUtils;
import de.zeanon.schemmanager.utils.MessageUtils;
import de.zeanon.schemmanager.worldeditversion.utils.WorldEditVersionRequestUtils;
import de.zeanon.schemmanager.worldeditversion.utils.WorldEditVersionSchemUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Delete {

    public static boolean onDelete(final Player p, final String[] args) {
        Path schemPath = WorldEditVersionSchemUtils.getSchemPath();
        ArrayList<File> files = schemPath != null ? InternalFileUtils.getExistingFiles(schemPath.resolve(args[2])) : null;
        final boolean fileExists = files != null && files.size() > 0;

        if (args.length == 3) {
            if (fileExists) {
                MessageUtils.sendBooleanMessage(ChatColor.RED + "Do you really want to delete " + ChatColor.GOLD + args[2] + ChatColor.RED + "?", "//schem del " + args[2] + " confirm", "//schem del " + args[2] + " deny", p);
                WorldEditVersionRequestUtils.addDeleteRequest(p, args[2]);
                return true;
            } else {
                p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
                return false;
            }
        } else if (args.length == 4 && WorldEditVersionRequestUtils.checkDeleteRequest(p, args[2])) {
            if (args[3].equalsIgnoreCase("confirm")) {
                WorldEditVersionRequestUtils.removeDeleteRequest(p);
                if (fileExists) {
                    String parentName = null;
                    for (File file : files) {
                        if (!file.delete()) {
                            p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " could not be deleted.");
                            return false;
                        } else {
                            if (ConfigUtils.getBoolean("Delete empty Folders") && !file.getParentFile().equals(WorldEditVersionSchemUtils.getSchemFolder())) {
                                parentName = Objects.requireNonNull(file.getParentFile().listFiles()).length > 0 ? null : InternalFileUtils.deleteEmptyParent(file);
                            }
                        }
                    }
                    p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " was deleted successfully.");
                    if (parentName != null) {
                        p.sendMessage(ChatColor.RED + "Folder " + ChatColor.GREEN + parentName + ChatColor.RED + " was deleted sucessfully due to being empty.");
                    }
                    return true;
                } else {
                    p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
                    return false;
                }

            } else if (args[3].equalsIgnoreCase("deny")) {
                WorldEditVersionRequestUtils.removeDeleteRequest(p);
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