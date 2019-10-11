package de.zeanon.schemmanager.globalutils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.globalutils.updateutils.DefaultUpdate;
import de.zeanon.schemmanager.globalutils.updateutils.PlugManEnabledUpdate;
import de.zeanon.schemmanager.globalutils.updateutils.UpdateUtils;
import de.zeanon.schemmanager.worldeditversion.utils.WorldEditVersionRequestUtils;
import org.bukkit.entity.Player;

import java.io.BufferedInputStream;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Update {

    static boolean updatePlugin() {
        if (SchemManager.getPluginManager().getPlugin("PlugMan") != null && SchemManager.getPluginManager().isPluginEnabled(SchemManager.getPluginManager().getPlugin("PlugMan"))) {
            return PlugManEnabledUpdate.updatePlugin(ConfigUtils.getBoolean("Automatic Reload"));
        } else {
            return DefaultUpdate.updatePlugin(ConfigUtils.getBoolean("Automatic Reload"));
        }
    }

    static boolean updatePlugin(final Player p) {
        if (SchemManager.getPluginManager().getPlugin("PlugMan") != null && SchemManager.getPluginManager().isPluginEnabled(SchemManager.getPluginManager().getPlugin("PlugMan"))) {
            return PlugManEnabledUpdate.updatePlugin(p, ConfigUtils.getBoolean("Automatic Reload"));
        } else {
            return DefaultUpdate.updatePlugin(p, ConfigUtils.getBoolean("Automatic Reload"));
        }
    }


    public static boolean updateConfig(final boolean force) {
        if (force || !SchemManager.config.contains("File Extensions")
                || !SchemManager.config.contains("Listmax")
                || !SchemManager.config.contains("Space Lists")
                || !SchemManager.config.contains("Delete empty Folders")
                || !SchemManager.config.contains("Save Function Override")
                || !SchemManager.config.contains("Stoplag Override")
                || !SchemManager.config.contains("Automatic Reload")
                || !SchemManager.config.contains("Plugin Version")
                || !SchemManager.config.getString("Plugin Version").equals(SchemManager.getInstance().getDescription().getVersion())) {
            List<String> fileExtensions = SchemManager.config.contains("File Extensions") ? SchemManager.config.getStringList("File Extensions") : Arrays.asList("schem", "schematic");
            int listmax = SchemManager.config.contains("Listmax") ? SchemManager.config.getInt("Listmax") : 10;
            boolean spaceLists = !SchemManager.config.contains("Space Lists") || SchemManager.config.getBoolean("Space Lists");
            boolean deleteEmptyFolders = !SchemManager.config.contains("Delete empty Folders") || SchemManager.config.getBoolean("Delete empty Folders");
            boolean saveOverride = !SchemManager.config.contains("Save Function Override") || SchemManager.config.getBoolean("Save Function Override");
            boolean stoplagOverride = !SchemManager.config.contains("Stoplag Override") || SchemManager.config.getBoolean("Stoplag Override");
            boolean autoReload = !SchemManager.config.contains("Automatic Reload") || SchemManager.config.getBoolean("Automatic Reload");

            if (UpdateUtils.writeToFile(new File(SchemManager.getInstance().getDataFolder(), "config.yml"), new BufferedInputStream(Objects.requireNonNull(WorldEditVersionRequestUtils.class.getClassLoader().getResourceAsStream("resources/config.yml"))))) {
                SchemManager.config.update();

                SchemManager.config.set("Plugin Version", SchemManager.getInstance().getDescription().getVersion());
                SchemManager.config.set("File Extensions", fileExtensions);
                SchemManager.config.set("Listmax", listmax);
                SchemManager.config.set("Space Lists", spaceLists);
                SchemManager.config.set("Delete empty Folders", deleteEmptyFolders);
                SchemManager.config.set("Save Function Override", saveOverride);
                SchemManager.config.set("Stoplag Override", stoplagOverride);
                SchemManager.config.set("Automatic Reload", autoReload);

                System.out.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> " + SchemManager.config.getFile().getName() + " updated");
                return true;
            } else {
                System.out.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> " + SchemManager.config.getFile().getName() + " could not be updated");
                return false;
            }
        }
        return true;
    }
}