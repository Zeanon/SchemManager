package de.zeanon.schemmanager.worldeditversion.utils;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import de.zeanon.schemmanager.worldeditversion.WorldEditVersionMain;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class SchemUtils {

    public final static WorldEditPlugin we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
    private static File schemFolder;
    private static Path schemFolderPath;

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
            schemFolderPath = Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("WorldEdit")).getDataFolder().toPath().resolve(tempPath).normalize();
            schemFolder = schemFolderPath.toFile();
            if (!schemFolder.exists()) {
                if (!schemFolder.mkdirs()) {
                    throw new FileNotFoundException();
                }
            }
        }
    }
}
