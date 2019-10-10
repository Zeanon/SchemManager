package de.zeanon.schemmanager.worldeditversion.utils;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.worldeditversion.WorldEditVersionMain;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class WorldEditVersionSchemUtils {

    private static WorldEditPlugin we;
    private static File schemFolder;
    private static Path schemFolderPath;

    public static void initWorldEditPlugin() {
        we = (WorldEditPlugin) SchemManager.getPluginManager().getPlugin("WorldEdit");
    }

    public static WorldEditPlugin getWorldEditPlugin() {
        return we;
    }

    public static Path getSchemPath() {
        if (WorldEditVersionMain.weConfig.hasNotChanged()) {
            return schemFolderPath;
        } else {
            try {
                initSchemPath();
                return schemFolderPath;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static File getSchemFolder() {
        return schemFolder;
    }

    public static void initSchemPath() throws FileNotFoundException {
        Path tempPath = Paths.get(WorldEditVersionMain.weConfig.getString("saving.dir"));
        if (tempPath.isAbsolute()) {
            schemFolderPath = tempPath.normalize();
            schemFolder = schemFolderPath.toFile();
            if (!schemFolder.exists()) {
                if (!schemFolder.mkdirs()) {
                    throw new FileNotFoundException();
                }
            }
        } else {
            schemFolderPath = Objects.requireNonNull(SchemManager.getPluginManager().getPlugin("WorldEdit")).getDataFolder().toPath().resolve(tempPath).normalize();
            schemFolder = schemFolderPath.toFile();
            if (!schemFolder.exists()) {
                if (!schemFolder.mkdirs()) {
                    throw new FileNotFoundException();
                }
            }
        }
    }
}
