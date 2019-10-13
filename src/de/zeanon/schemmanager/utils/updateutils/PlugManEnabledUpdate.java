package de.zeanon.schemmanager.utils.updateutils;

import com.rylinaux.plugman.util.PluginUtil;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.utils.InternalFileUtils;
import de.zeanon.schemmanager.worldeditversion.WorldEditVersionMain;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

@SuppressWarnings("Duplicates")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlugManEnabledUpdate {

    public static boolean updatePlugin(final boolean autoReload) {
        System.out.println(SchemManager.getInstance().getName() + " is updating...");
        String fileName;
        try {
            fileName = new File(WorldEditVersionMain.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath())
                    .getName();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println(SchemManager.getInstance().getName() + " could not be updated.");
            return false;
        }
        try {
            if (UpdateUtils.writeToFile(new File(InternalFileUtils.getPluginFolderPath() + fileName), new BufferedInputStream(new URL("https://github.com/Zeanon/SchemManager/releases/latest/download/SchemManager.jar").openStream()))) {
                System.out.println(SchemManager.getInstance().getName() + " was updated successfully.");
                if (autoReload) {
                    PluginUtil.reload(SchemManager.getInstance());
                }
                return true;
            } else {
                System.out.println(SchemManager.getInstance().getName() + " could not be updated.");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(SchemManager.getInstance().getName() + " could not be updated.");
            return false;
        }
    }

    public static boolean updatePlugin(final Player p, final boolean autoReload) {
        p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName() + ChatColor.RED + " is updating...");
        String fileName;
        try {
            fileName = new File(WorldEditVersionMain.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath())
                    .getName();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName() + ChatColor.RED + " could not be updated.");
            return false;
        }
        try {
            if (UpdateUtils.writeToFile(new File(InternalFileUtils.getPluginFolderPath() + fileName), new BufferedInputStream(new URL("https://github.com/Zeanon/SchemManager/releases/latest/download/SchemManager.jar").openStream()))) {
                p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName() + ChatColor.RED + " was updated successfully.");
                if (autoReload) {
                    p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName() + ChatColor.RED + " is reloading.");
                    PluginUtil.reload(SchemManager.getInstance());
                }
                return true;
            } else {
                p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName() + ChatColor.RED + " could not be updated.");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName() + ChatColor.RED + " could not be updated.");
            return false;
        }
    }
}