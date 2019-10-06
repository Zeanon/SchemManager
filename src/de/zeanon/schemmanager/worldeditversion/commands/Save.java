package de.zeanon.schemmanager.worldeditversion.commands;

import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.LocalSession;
import de.zeanon.schemmanager.globalutils.DefaultHelper;
import de.zeanon.schemmanager.worldeditversion.utils.Helper;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Save {

    public static boolean onSave(Player p, String[] args) {
        try {
            File file = args[2].endsWith(".schem") ? Helper.getSchemPath().resolve(args[2]).toFile() : Helper.getSchemPath().resolve(args[2] + ".schem").toFile();
            final boolean fileExists = file.exists() && !file.isDirectory();
            LocalSession session = Helper.we.getSession(p);

            if (args.length == 3) {
                try {
                    session.getClipboard();
                    Helper.addOverwriteRequest(p, args[2]);
                    if (fileExists) {
                        p.sendMessage(ChatColor.RED + "The schematic " + ChatColor.GOLD + args[2] + ChatColor.RED + " already exists.");
                        DefaultHelper.sendBooleanMessage(ChatColor.RED + "Do you want to overwrite " + ChatColor.GOLD + args[2] + ChatColor.RED + "?", "//schem save " + args[2] + " confirm", "//schem save " + args[2] + " deny", p);
                        return true;
                    } else {
                        p.performCommand("/schem save -f " + args[2]);
                        return true;
                    }
                } catch (EmptyClipboardException e) {
                    p.sendMessage(ChatColor.RED + "Your clipboard is empty. Use //copy first.");
                    return false;
                }
            } else {
                if (args[3].equals("confirm") && Helper.checkOverWriteRequest(p, args[2])) {
                    Helper.removeOverWriteRequest(p);
                    p.performCommand("/schem save -f " + args[2]);
                    return true;
                } else if (args[3].equals("deny") && Helper.checkOverWriteRequest(p, args[2])) {
                    Helper.removeOverWriteRequest(p);
                    p.sendMessage(ChatColor.LIGHT_PURPLE + args[2] + " was not overwritten.");
                    return true;
                } else {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            p.sendMessage(ChatColor.RED + "Could not find Schematic folder.");
            return false;
        }
    }
}