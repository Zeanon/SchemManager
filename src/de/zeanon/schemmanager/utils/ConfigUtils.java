package de.zeanon.schemmanager.utils;

import de.zeanon.schemmanager.SchemManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigUtils {

    /**
     * get an int from the config
     *
     * @param path the yaml path
     * @return value
     */
    public static int getInt(final String path) {
        if (SchemManager.config.hasKey(path)) {
            return SchemManager.config.getInt(path);
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Update.updateConfig(true);
                }
            }.runTaskAsynchronously(SchemManager.getInstance());
            return (int) getDefaultValue(path);
        }
    }

    /**
     * get a boolean from the config
     *
     * @param path the yaml path
     * @return value
     */
    public static boolean getBoolean(final String path) {
        if (SchemManager.config.hasKey(path)) {
            return SchemManager.config.getBoolean(path);
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Update.updateConfig(true);
                }
            }.runTaskAsynchronously(SchemManager.getInstance());
            return (boolean) getDefaultValue(path);
        }
    }

    /**
     * get a StringList from the config
     *
     * @param path the yaml path
     * @return value
     */
    @SuppressWarnings("unchecked")
    public static List<String> getStringList(final String path) {
        if (SchemManager.config.hasKey(path)) {
            return SchemManager.config.getStringList(path);
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Update.updateConfig(true);
                }
            }.runTaskAsynchronously(SchemManager.getInstance());
            return (List<String>) getDefaultValue(path);
        }
    }

    private static Object getDefaultValue(final String path) {
        switch (path) {
            case "Space Lists":
                return true;
            case "Delete empty Folders":
                return true;
            case "Listmax":
                return 10;
            case "Save Function Override":
                return true;
            case "Stoplag Override":
                return true;
            case "Automatic Reload":
                return true;
            case "File Extensions":
                return Arrays.asList("schem", "schematic");
            case "Plugin Version":
                return SchemManager.getInstance().getDescription().getVersion();
            default:
                return null;
        }
    }
}