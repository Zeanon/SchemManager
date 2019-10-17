package de.zeanon.schemmanager.utils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.utils.updateutils.DefaultUpdate;
import de.zeanon.schemmanager.utils.updateutils.PlugManEnabledUpdate;
import de.zeanon.schemmanager.utils.updateutils.UpdateUtils;
import de.zeanon.schemmanager.worldeditversion.utils.WorldEditVersionRequestUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

import java.io.BufferedInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Update {

    static void updatePlugin() {
        if (SchemManager.getPluginManager().getPlugin("PlugMan") != null && SchemManager.getPluginManager().isPluginEnabled(SchemManager.getPluginManager().getPlugin("PlugMan"))) {
            PlugManEnabledUpdate.updatePlugin(ConfigUtils.getBoolean("Automatic Reload"));
        } else {
            DefaultUpdate.updatePlugin(ConfigUtils.getBoolean("Automatic Reload"));
        }
    }

    static void updatePlugin(final Player p) {
        if (SchemManager.getPluginManager().getPlugin("PlugMan") != null && SchemManager.getPluginManager().isPluginEnabled(SchemManager.getPluginManager().getPlugin("PlugMan"))) {
            PlugManEnabledUpdate.updatePlugin(p, ConfigUtils.getBoolean("Automatic Reload"));
        } else {
            DefaultUpdate.updatePlugin(p, ConfigUtils.getBoolean("Automatic Reload"));
        }
    }


    public static boolean updateConfig(final boolean force) {
        if (force || !SchemManager.config.hasKey("Plugin Version")
                || !SchemManager.config.getString("Plugin Version").equals(SchemManager.getInstance().getDescription().getVersion())
                || !SchemManager.config.hasKey("File Extensions")
                || !SchemManager.config.hasKey("Listmax")
                || !SchemManager.config.hasKey("Space Lists")
                || !SchemManager.config.hasKey("Delete empty Folders")
                || !SchemManager.config.hasKey("Save Function Override")
                || !SchemManager.config.hasKey("Stoplag Override")
                || !SchemManager.config.hasKey("Automatic Reload")) {

            List<String> fileExtensions = SchemManager.config.hasKey("File Extensions") ? SchemManager.config.getStringList("File Extensions") : Arrays.asList("schem", "schematic");
            int listmax = SchemManager.config.hasKey("Listmax") ? SchemManager.config.getInt("Listmax") : 10;
            boolean spaceLists = !SchemManager.config.hasKey("Space Lists") || SchemManager.config.getBoolean("Space Lists");
            boolean deleteEmptyFolders = !SchemManager.config.hasKey("Delete empty Folders") || SchemManager.config.getBoolean("Delete empty Folders");
            boolean saveOverride = !SchemManager.config.hasKey("Save Function Override") || SchemManager.config.getBoolean("Save Function Override");
            boolean stoplagOverride = !SchemManager.config.hasKey("Stoplag Override") || SchemManager.config.getBoolean("Stoplag Override");
            boolean autoReload = !SchemManager.config.hasKey("Automatic Reload") || SchemManager.config.getBoolean("Automatic Reload");

            if (UpdateUtils.writeToFile(SchemManager.config.getFile(), new BufferedInputStream(Objects.requireNonNull(WorldEditVersionRequestUtils.class.getClassLoader().getResourceAsStream("resources/config.yml"))))) {
                SchemManager.config.reload(true);

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
        } else {
            return true;
        }
    }
}